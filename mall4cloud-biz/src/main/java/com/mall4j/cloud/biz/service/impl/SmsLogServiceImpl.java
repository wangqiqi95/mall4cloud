package com.mall4j.cloud.biz.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.mall4j.cloud.api.platform.config.FeignShopConfig;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.biz.mapper.SmsLogMapper;
import com.mall4j.cloud.biz.model.SmsLog;
import com.mall4j.cloud.biz.service.NotifyTemplateService;
import com.mall4j.cloud.biz.service.SmsLogService;
import com.mall4j.cloud.biz.vo.SmsCodeTemplateVO;
import com.mall4j.cloud.common.bean.DaYu;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.constant.SendTypeEnum;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.util.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 短信记录表
 *
 * @author lhd
 * @date 2021-01-04 13:36:52
 */
@Service
public class SmsLogServiceImpl implements SmsLogService {

    /**
     * 当天最大验证码短信发送量
     */
    private static final int TODAY_MAX_SEND_VALID_SMS_NUMBER = 10;

    /**
     * 一段时间内短信验证码的最大验证次数
     */
    private static final int TIMES_CHECK_VALID_CODE_NUM = 10;
    /**
     * 产品域名,开发者无需替换
     */
    private static final String DOMAIN = "dysmsapi.aliyuncs.com";
    /**
     * 产品RegionId,开发者无需替换
     */
    private static final String REGION_ID = "cn-hangzhou";

    /**
     * 产品version,开发者无需替换
     */
    private static final String VERSION = "2017-05-25";

    /**
     * 产品Action,开发者无需替换
     */
    private static final String ACTION = "SendSms";
    /**
     * 短信验证码的前缀
     */
    private static final String CHECK_VALID_CODE_NUM_PREFIX = "checkValidCodeNum_";

    private static final Logger logger = LoggerFactory.getLogger(SmsLogServiceImpl.class);

    @Autowired
    private SmsLogMapper smsLogMapper;

    @Autowired
    private FeignShopConfig feignShopConfig;

    @Autowired
    private NotifyTemplateService notifyTemplateService;

    @Autowired
    private UserFeignClient userFeignClient;

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void sendSmsCode(SendTypeEnum sendType, String mobile, Map<String, String> params) {
        if (Objects.equals(SendTypeEnum.UPDATE_PASSWORD, sendType)) {
            Integer user = userFeignClient.countUserByMobile(mobile).getData();
            if (Objects.equals(user, 0)) {
                throw new LuckException("该手机号码未注册");
            }
        }
        SmsLog smsLog = new SmsLog();
        boolean isTrue = Objects.equals(SendTypeEnum.REGISTER, sendType) || Objects.equals(SendTypeEnum.LOGIN, sendType)
                || Objects.equals(SendTypeEnum.UPDATE_PASSWORD, sendType) || Objects.equals(SendTypeEnum.VALID, sendType);

        SmsCodeTemplateVO smsCodeTemplateVO = notifyTemplateService.getSmsCodeTemplateBySendType(sendType.getValue());

        if (isTrue) {
            List<SmsLog> smsLogList = smsLogMapper.listByMobileAndTypeAndToday(DateUtil.beginOfDay(new Date()), DateUtil.endOfDay(new Date()), mobile, sendType.getValue());
            if (smsLogList.size() >= TODAY_MAX_SEND_VALID_SMS_NUMBER) {
                // 今日发送短信验证码次数已达到上限
                throw new LuckException("今日发送短信验证码次数已达到上限");
            }

            if (smsLogList.size() > 0) {
                SmsLog smsLogLast = smsLogList.get(0);
                long currentTimeMillis = System.currentTimeMillis();
                long timeDb = DateUtil.offsetSecond(smsLogLast.getCreateTime(), 60).getTime();
                if (currentTimeMillis < timeDb) {
                    // 一分钟内只能发送一次验证码
                    throw new LuckException("一分钟内只能发送一次验证码");
                }
            }
            // 将上一条验证码失效
            smsLogMapper.invalidSmsByMobileAndType(mobile, sendType.getValue());
            String code = RandomUtil.randomNumbers(6);
            params.put("code", code);
        }
        smsLog.setType(sendType.getValue());
        smsLog.setMobileCode(params.get("code"));
        smsLog.setStatus(1);
        smsLog.setUserPhone(mobile);
        smsLog.setContent(formatContent(smsCodeTemplateVO.getMessage(), params));
        smsLogMapper.save(smsLog);

        Boolean flag = this.sendSms(mobile, smsCodeTemplateVO.getTemplateCode(), params);
        if (!flag) {
            // 发送短信失败，请稍后再试
            throw new LuckException("发送短信失败，请稍后再试");
        }
    }


    @Override
    public Boolean sendMsgSms(String templateCode, String userMobile, Map<String, String> smsParam) {
        return sendSms(userMobile, templateCode, smsParam);
    }

    @Override
    public Boolean checkValidCode(String mobile, String validCode, SendTypeEnum sendType) {
        long checkValidCodeNum = RedisUtil.incr(CHECK_VALID_CODE_NUM_PREFIX + mobile, 1);
        // 半小时后失效
        RedisUtil.expire(CHECK_VALID_CODE_NUM_PREFIX + mobile, 1800);
        if (checkValidCodeNum >= TIMES_CHECK_VALID_CODE_NUM) {
            throw new LuckException("验证码校验过频繁，请稍后再试");
        }
        SmsLog sms = new SmsLog();
        sms.setUserPhone(mobile);
        sms.setMobileCode(validCode);
        sms.setStatus(1);
        sms.setType(sendType.getValue());

        SmsLog dbSms = smsLogMapper.getByMobileAndCodeAndType(mobile, validCode, sendType.getValue());
        // 没有找到当前的验证码
        if (dbSms == null) {
            RedisUtil.decr(CHECK_VALID_CODE_NUM_PREFIX + mobile, 1);
            return false;
        }
        RedisUtil.del(CHECK_VALID_CODE_NUM_PREFIX + mobile);
        // 标记为失效状态
        dbSms.setStatus(0);
        smsLogMapper.update(dbSms);
        // 验证码已过期
        DateTime offsetMinute = DateUtil.offsetMinute(dbSms.getCreateTime(), 5);
        if (offsetMinute.getTime() < System.currentTimeMillis()) {
            RedisUtil.incr(CHECK_VALID_CODE_NUM_PREFIX + mobile, 1);
            return false;
        }

        return true;
    }

    private Boolean sendSms(String mobile, String templateCode, Map<String, String> params) {
        DaYu aLiDaYu = feignShopConfig.getDaYu();

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile(REGION_ID, aLiDaYu.getAccessKeyId(), aLiDaYu.getAccessKeySecret());
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain(DOMAIN);
        request.setSysVersion(VERSION);
        request.setSysAction(ACTION);
        request.putQueryParameter("RegionId", REGION_ID);
        //必填:待发送手机号
        request.putQueryParameter("PhoneNumbers", mobile);
        //必填:短信签名-可在短信控制台中找到
        request.putQueryParameter("SignName", aLiDaYu.getSignName());
        //必填:短信模板-可在短信控制台中找到
        request.putQueryParameter("TemplateCode", templateCode);
        request.putQueryParameter("TemplateParam", Json.toJsonString(params));

        try {
            CommonResponse response = acsClient.getCommonResponse(request);
            System.out.println(response.getData());
            return true;
        } catch (ClientException e) {
            logger.error("发送短信失败，请稍后再试：" + e.getMessage());
            return false;
        }
    }

    private String formatContent(String content, Map<String, String> params) {

        for (Map.Entry<String, String> element : params.entrySet()) {
            content = content.replace("${" + element.getKey() + "}", element.getValue());
        }
        return content;
    }

}
