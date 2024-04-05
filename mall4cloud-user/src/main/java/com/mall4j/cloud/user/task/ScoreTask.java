package com.mall4j.cloud.user.task;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.biz.feign.WxMaApiFeignClient;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptTmessageSendVO;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptTmessageVO;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptUserRecordVO;
import com.mall4j.cloud.api.platform.feign.ConfigFeignClient;
import com.mall4j.cloud.api.platform.vo.ScoreExpireConfigVO;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.constant.ConfigNameConstant;
import com.mall4j.cloud.common.constant.MaSendTypeEnum;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.util.DateUtils;
import com.mall4j.cloud.common.util.Json;
import com.mall4j.cloud.user.mapper.scoreConvert.ScoreConvertMapper;
import com.mall4j.cloud.user.model.User;
import com.mall4j.cloud.user.model.scoreConvert.ScoreConvert;
import com.mall4j.cloud.user.service.UserExtensionService;
import com.mall4j.cloud.user.service.UserLevelService;
import com.mall4j.cloud.user.service.UserScoreGetLogService;
import com.mall4j.cloud.user.service.UserService;
import com.mall4j.cloud.user.service.scoreConvert.ScoreCouponService;
import com.mall4j.cloud.user.vo.UserVO;
import com.purgerteam.log.trace.starter.filter.TraceIdUtil;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author cl
 * @date 2021-07-22 16:38:42
 */
@Component
public class ScoreTask {

    private static final Logger log = LoggerFactory.getLogger(ScoreTask.class);

    @Autowired
    private UserExtensionService userExtensionService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserLevelService userLevelService;
    @Autowired
    private UserScoreGetLogService userScoreGetLogService;
    @Autowired
    private ConfigFeignClient configFeignClient;
    @Resource
    private ScoreConvertMapper scoreConvertMapper;
//    @Resource
//    private OnsMQTemplate sendMaSubcriptMessageTemplate;
    @Resource
    private WxMaApiFeignClient wxMaApiFeignClient;
    @Resource
    private ScoreCouponService scoreCouponService;

    /**
     * 将会员设为已过期状态
     * 付费会员过期处理规则：
     * 将付费会员的等级和类型变成
     */
    @XxlJob("vipUserClear")
    public void vipUserClear() {
        log.info("将会员设为已过期状态》》》》》》》》》》》》》》》》》》》》》");
        // 获取截至日期之前的所有付费会员信息
        List<UserVO> users = userService.selectMemberByEndTime(DateUtil.endOfDay(DateUtil.yesterday()));
        if (CollectionUtil.isEmpty(users)) {
            log.info("将会员设为已过期状态》》》》》》》》》》》》》》》》》》》》》");
            return;
        }
        // 会员过期变更规则：将付费会员变成普通会员，会员等级直接变成普通会员的成长值对应的等级
        userLevelService.expireVipUsers(users);

        log.info("将会员设为已过期状态》》》》》》》》》》》》》》》》》》》》》");
    }

    /**
     * 积分过期定时任务
     */
    @XxlJob("scoreExpireTask")
    public void scoreExpireTask() {
        // 0.计算过期时间
        String scoreExpireParam = configFeignClient.getConfig(ConfigNameConstant.SCORE_EXPIRE).getData();
        //计算过期时间
        ScoreExpireConfigVO scoreExpireParamConfigVO = Json.parseObject(scoreExpireParam, ScoreExpireConfigVO.class);
        if (Objects.isNull(scoreExpireParamConfigVO) || !scoreExpireParamConfigVO.getScoreExpireSwitch()) {
            return;
        }
        int year = scoreExpireParamConfigVO.getExpireYear() - 1;
        DateTime dateTime = DateUtil.offset(DateUtil.beginOfDay(new Date()), DateField.YEAR, -year);
        // 1.修改用户过期积分
        userScoreGetLogService.updateExpireScoreDetail(dateTime);
    }

    /**
     * 积分活动上新定时任务
     */
    @XxlJob("newScoreActivityTask")
    public void newScoreActivityTask() {
        XxlJobHelper.log("============= 积分活动上新定时任务 =============");

        Date date = new Date();
        Date startTime = DateUtil.offset(date, DateField.HOUR, -24);

        //查询是否有活动上新
        List<ScoreConvert> scoreConverts = scoreConvertMapper.selectNewActivity(startTime, date);
        if (CollectionUtil.isEmpty(scoreConverts)) {
            return;
        }
        scoreConverts.forEach(scoreConvert -> {
            //查询积分活动上新订阅模版
            ServerResponseEntity<WeixinMaSubscriptTmessageVO> subscriptResp = wxMaApiFeignClient.getinsIderSubscriptTmessage(MaSendTypeEnum.POINT_ACTIVITY_CHANGE.getValue(), null);
            log.info("积分活动上新订阅模版: {}", JSONObject.toJSONString(subscriptResp));
            if (subscriptResp.isSuccess()) {
                //获取订阅用户
                WeixinMaSubscriptTmessageVO weixinMaSubscriptTmessageVO = subscriptResp.getData();
                List<WeixinMaSubscriptUserRecordVO> userRecords = weixinMaSubscriptTmessageVO.getUserRecords();
                if (!CollectionUtils.isEmpty(userRecords)){
                    List<WeixinMaSubscriptTmessageSendVO> sendList = userRecords.stream().map(user -> {
                        /**
                         * 值替换
                         * 1会员业务 2优惠券业务 3订单业务 4退单业务 5线下活动  6积分商城 7每日签到 8直播通知 9微客审核
                         * 可以替换的模版为固定的， 根据对应类型在参考 mall4cloud.weixin_ma_subscript_tmessage_optional_value
                         * 当前积分商城 场景下 活动名称{activityName}、开始时间{activityBeginTime}、结束时间{activityEndTime}
                         * 构建参数map.
                         */
                        Map<String, String> paramMap = new HashMap();
                        paramMap.put("{activityName}", scoreConvert.getConvertTitle());
                        paramMap.put("{activityBeginTime}", DateUtils.dateToString(scoreConvert.getStartTime()));
                        paramMap.put("{activityEndTime}", DateUtils.dateToString(scoreConvert.getEndTime()));
                        paramMap.put("{remark}", scoreConvert.getNewReminder());

                        List<WeixinMaSubscriptTmessageSendVO.MsgData> msgDataList = weixinMaSubscriptTmessageVO.getTmessageValues().stream().map(t -> {
                            WeixinMaSubscriptTmessageSendVO.MsgData msgData = new WeixinMaSubscriptTmessageSendVO.MsgData();
                            msgData.setName(t.getTemplateValueName());
                            msgData.setValue(StrUtil.isEmpty(paramMap.get(t.getTemplateValue())) ? t.getTemplateValue() : paramMap.get(t.getTemplateValue()));
                            return msgData;
                        }).collect(Collectors.toList());


                        wxMaApiFeignClient.updateUserRecordId(user.getId());

                        WeixinMaSubscriptTmessageSendVO sendVO = new WeixinMaSubscriptTmessageSendVO();
                        sendVO.setUserSubscriptRecordId(user.getId());
                        sendVO.setData(msgDataList);
                        sendVO.setPage(weixinMaSubscriptTmessageVO.getPage());
                        return sendVO;
                    }).collect(Collectors.toList());
                    log.info("积分活动上新订阅消息，待发送列表集合:{}",JSONObject.toJSONString(sendList));
                    //按照需要发送的消息集合，2000条一批次 拆分发送订阅消息
//                    if(CollectionUtil.isNotEmpty(sendList)){
//                        List<List<WeixinMaSubscriptTmessageSendVO>> sendLists = ListUtil.split(sendList,2000);
//                        for (List<WeixinMaSubscriptTmessageSendVO> list : sendLists) {
//                            sendMaSubcriptMessageTemplate.syncSend(list);
//                        }
//                    }
                }
            }else {
                log.error("查询积分活动上新模版失败，返回值为：{}", JSONObject.toJSONString(subscriptResp));
            }
        });
    }


    /**
     * 积分活动上新定时任务
     */
    @XxlJob("userBirthday")
    public void userBirthday() {
        XxlJobHelper.log("============= 会员生日定时任务 =============");

        List<Long> birthdayUser = userService.getBirthdayUser();

        if (ObjectUtil.isNotEmpty(birthdayUser)){
            //查询积分活动上新订阅模版
            ServerResponseEntity<WeixinMaSubscriptTmessageVO> subscriptResp = wxMaApiFeignClient.getinsIderSubscriptTmessage(MaSendTypeEnum.USER_BIRTHDAY.getValue(), null);
            log.info("会员生日订阅模版: {}", JSONObject.toJSONString(subscriptResp));
            if (subscriptResp.isSuccess()) {
                //获取订阅用户
                WeixinMaSubscriptTmessageVO weixinMaSubscriptTmessageVO = subscriptResp.getData();
                List<WeixinMaSubscriptUserRecordVO> userRecords = weixinMaSubscriptTmessageVO.getUserRecords();
                if (!CollectionUtils.isEmpty(userRecords)){
                    List<WeixinMaSubscriptTmessageSendVO> sendList = userRecords.stream().map(user -> {
                        if (birthdayUser.contains(user.getUserId())) {
                            UserApiVO vo = userService.getByUserId(user.getUserId());
                            /**
                             * 值替换
                             * 1会员业务 2优惠券业务 3订单业务 4退单业务 5线下活动  6积分商城 7每日签到 8直播通知 9微客审核
                             * 可以替换的模版为固定的， 根据对应类型在参考 mall4cloud.weixin_ma_subscript_tmessage_optional_value
                             * 当前会员生日 场景下
                             * 当前等级     {currentLevel}
                             * 备注           {remark}
                             * 会员手机号        {phone}
                             * 会员卡号     {cardNo}
                             * 变更时间     {changeDate}
                             * 会员生日     {birthDay}
                             * 会员姓名     {realName}
                             * 构建参数map.
                             */
                            Map<String, String> paramMap = new HashMap();
                            paramMap.put("{currentLevel}", vo.getLevelName());
                            paramMap.put("{phone}", vo.getPhone());
                            paramMap.put("{cardNo}", vo.getVipcode());
                            paramMap.put("{changeDate}", DateUtils.dateToString(vo.getUpdateTime()));
                            paramMap.put("{realName}", vo.getNickName());
                            paramMap.put("{birthDay}", vo.getBirthDate());

                            List<WeixinMaSubscriptTmessageSendVO.MsgData> msgDataList = weixinMaSubscriptTmessageVO.getTmessageValues().stream().map(t -> {
                                WeixinMaSubscriptTmessageSendVO.MsgData msgData = new WeixinMaSubscriptTmessageSendVO.MsgData();
                                msgData.setName(t.getTemplateValueName());
                                msgData.setValue(StrUtil.isEmpty(paramMap.get(t.getTemplateValue())) ? t.getTemplateValue() : paramMap.get(t.getTemplateValue()));
                                return msgData;
                            }).collect(Collectors.toList());

                            WeixinMaSubscriptTmessageSendVO sendVO = new WeixinMaSubscriptTmessageSendVO();
                            sendVO.setUserSubscriptRecordId(user.getId());
                            sendVO.setData(msgDataList);
                            sendVO.setPage(weixinMaSubscriptTmessageVO.getPage());
                            return sendVO;
                        }
                        return null;
                    }).collect(Collectors.toList());
                    log.info(JSONObject.toJSONString(sendList));
//                    sendMaSubcriptMessageTemplate.syncSend(sendList);
                }
            }else {
                log.error("查询会员生日模版失败，返回值为：{}", JSONObject.toJSONString(subscriptResp));
            }
        }else {
            log.info("会员生日无上新，查询时间为：{}", new Date());
        }
    }



}
