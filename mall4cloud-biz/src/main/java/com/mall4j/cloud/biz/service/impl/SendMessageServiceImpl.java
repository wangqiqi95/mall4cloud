
package com.mall4j.cloud.biz.service.impl;

import cn.binarywang.wx.miniapp.bean.WxMaTemplateData;
import cn.binarywang.wx.miniapp.bean.WxMaUniformMessage;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.api.platform.config.FeignShopConfig;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.biz.constant.NotifyType;
import com.mall4j.cloud.biz.model.NotifyLog;
import com.mall4j.cloud.biz.service.NotifyLogService;
import com.mall4j.cloud.biz.service.NotifyTemplateService;
import com.mall4j.cloud.biz.service.SendMessageService;
import com.mall4j.cloud.biz.service.SmsLogService;
import com.mall4j.cloud.biz.vo.NotifyParamVO;
import com.mall4j.cloud.biz.vo.NotifyTemplateVO;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.constant.SendTypeEnum;
import com.mall4j.cloud.common.constant.StatusEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * 统一发送通知的事件
 * @author lhd
 */
@Service
public class SendMessageServiceImpl implements SendMessageService {

    private static final Logger logger = LoggerFactory.getLogger(SendMessageServiceImpl.class);

    @Autowired
    private NotifyLogService notifyLogService;
    @Autowired
    private SmsLogService smsLogService;
    @Autowired
    private FeignShopConfig feignShopConfig;
    @Autowired
    private WxConfig wxConfig;
    @Autowired
    private NotifyTemplateService notifyTemplateService;

    @Autowired
    private UserFeignClient userFeignClient;

    @Override
    public void sendMsg(NotifyParamVO notifyParamVO) {
        if(Objects.isNull(notifyParamVO)){
            return;
        }
        String shopName = null;
        String contactPhone = null;
        notifyParamVO.setShopName(shopName);
        // 判断是什么类型，如果是发送给商家的，则修改手机号码，并且只需要推送手机号码即可
        SendTypeEnum sendTypeEnum = SendTypeEnum.instance(notifyParamVO.getSendType());
        if(Objects.nonNull(sendTypeEnum) && Objects.equals(sendTypeEnum.getType(),SendTypeEnum.RECEIPT_ORDER.getType())){
            notifyParamVO.setMobile(contactPhone);
        }
        //获取模板的发送方式，获取商家选择的发送方式
        NotifyTemplateVO template = notifyTemplateService.getBySendType(notifyParamVO.getSendType());
        if(Objects.isNull(template) || Objects.equals(template.getStatus(), StatusEnum.DISABLE.value())){
            return;
        }
        // 截取字符串,处理过长商品名称
        if(StrUtil.isNotBlank(notifyParamVO.getSpuName())) {
            notifyParamVO.setSpuName(notifyParamVO.getSpuName().substring(0, Math.min(14, notifyParamVO.getSpuName().length() - 1)) + "等商品");
        }
        // 处理数据
        notifyParamVO.setHour(Objects.isNull(notifyParamVO.getHour()) ? 0: notifyParamVO.getHour());
        notifyParamVO.setProdNum(Objects.isNull(notifyParamVO.getProdNum()) ? 0: notifyParamVO.getProdNum());
        notifyParamVO.setGroupCount(Objects.isNull(notifyParamVO.getGroupCount()) ? 0:notifyParamVO.getGroupCount());
        notifyParamVO.setPrice(Objects.isNull(notifyParamVO.getPrice()) ? 0.0:notifyParamVO.getPrice());
        notifyParamVO.setPayType(Objects.isNull(notifyParamVO.getPayType())? 1:notifyParamVO.getPayType());
        notifyParamVO.setDvyTime(Objects.isNull(notifyParamVO.getDvyTime())? new Date():notifyParamVO.getDvyTime());
        ServerResponseEntity<UserApiVO> userData = userFeignClient.getUserAndOpenIdsByUserId(notifyParamVO.getUserId());
        UserApiVO user = userData.getData();
        String openId = "";
        if(Objects.nonNull(user)) {
            if (CollectionUtils.isNotEmpty(user.getBizUserIdList())) {
                openId = user.getBizUserIdList().get(0);
            }
            notifyParamVO.setNickName(user.getNickName());
            // 截取字符串,处理过长名称
            if (StrUtil.isNotBlank(user.getNickName())) {
                user.setNickName(user.getNickName().substring(0, Math.min(19, user.getNickName().length() - 1)));
            }
        }
        // 分别根据平台和商家的配置进行设置可以发送通知的类型
        // 设置平台的消息通知类型
        setNotifyType(template);
        // 获取对应map
        Map<String, String> smsParam = getSmsParam(notifyParamVO, shopName);
        String content = replaceContent(smsParam, template.getMessage());
        // 保存的通知集合
        List<NotifyLog> notifyLogs = new ArrayList<>();
        if(template.getSms()) {
            if(Objects.nonNull(sendTypeEnum) && Objects.equals(sendTypeEnum.getType(),SendTypeEnum.RECEIPT_ORDER.getType()) && StrUtil.isBlank(notifyParamVO.getMobile())){
                logger.warn(shopName +"店铺暂未配置接收通知的手机号，无法推送消息");
                return;
            }
            Boolean isSend = smsLogService.sendMsgSms(template.getTemplateCode(),notifyParamVO.getMobile(),smsParam);
            // 替换消息内容
            addNotifyLog(notifyParamVO,content,template,isSend, NotifyType.SMS.value(),notifyLogs);
        }
        if(template.getApp()) {
            notifyParamVO.setShopName(shopName);
            addNotifyLog(notifyParamVO,content,template,false, NotifyType.APP.value(),notifyLogs);
        }
        // 公众号
        if(template.getSub() && StrUtil.isNotBlank(openId)) {
            sendMessage(template,notifyParamVO,openId,shopName);
            addNotifyLog(notifyParamVO,content,template,true, NotifyType.MP.value(),notifyLogs);
        }

        if(CollectionUtils.isNotEmpty(notifyLogs)){
            notifyLogService.saveBatch(notifyLogs);
        }

    }

    private void setNotifyType(NotifyTemplateVO template) {
        String[] templateCodes = template.getNotifyTypes().split(StrUtil.COMMA);
        List<String> asList = Arrays.asList(templateCodes);
        List<Integer> templates = new ArrayList<>();
        for (String templateStr : asList) {
            templates.add(Integer.valueOf(templateStr));
        }
        template.setSms(false);
        template.setSub(false);
        template.setApp(false);
        for (Integer type : templates) {
            template.setSms(Objects.equals(type,NotifyType.SMS.value()) || template.getSms());
            template.setSub(Objects.equals(type,NotifyType.MP.value()) || template.getSub());
            template.setApp(Objects.equals(type,NotifyType.APP.value()) || template.getApp());
        }
    }

    /**
     * 发送微信模板信息
     */
    private void sendMessage(NotifyTemplateVO template,NotifyParamVO notifyParamVO, String bizUserId,String shopName) {
        WxMaUniformMessage wxMaUniformMessage = new WxMaUniformMessage();
        wxMaUniformMessage.setMpTemplateMsg(true);
        wxMaUniformMessage.setToUser(bizUserId);
        wxMaUniformMessage.setAppid(feignShopConfig.getWxMp().getAppId());
        //TODO 公众号模板消息所要跳转的小程序暂无
//        WxMaUniformMessage.MiniProgram miniProgram = new WxMaUniformMessage.MiniProgram();
//        miniProgram.setAppid(feignShopConfig.getWxMp().getAppId());
//        miniProgram.setPagePath("pages/user/user");
//        wxMaUniformMessage.setMiniProgram(miniProgram);
        //模板id
        wxMaUniformMessage.setTemplateId(template.getMpCode());
        //模板数据集合
        wxMaUniformMessage.setData(getWxMaTemplateDataList(notifyParamVO,shopName));
        try {
            wxConfig.getWxMaService().getMsgService().sendUniformMsg(wxMaUniformMessage);
        } catch (WxErrorException e) {
            //用户未关注公众号，导致的消息发送失败异常
            if (Objects.nonNull(e.getError()) && e.getError().getErrorCode() == Constant.WX_MAX_NOT_FOLLOW){
                logger.info("用户（" + notifyParamVO.getUserId() + "）没有关注公众号，导致消息发送失败");
            }
            //其他异常
            else {
                e.printStackTrace();
            }
        }
    }


    /**
     * 获取微信通知模板列表
     * @param notifyParamVO 通知内容
     * @param shopName 店铺名称
     * @return 微信通知模板列表
     */
    private List<WxMaTemplateData> getWxMaTemplateDataList(NotifyParamVO notifyParamVO, String shopName) {
        Map<Integer, List<WxMaTemplateData>> mpMap = new HashMap<>(24);
        List<WxMaTemplateData> dataList = new ArrayList<>();
        String priceStr = notifyParamVO.getPrice() + "元";
        // 订单微信通知模板
        orderWxMaTemplate(notifyParamVO, shopName, mpMap, priceStr);
        // 团购微信通知模板
        groupWxMaTemplate(notifyParamVO, mpMap, priceStr);
        // 商家微信通知模板
        multishopWxMaTemplate(notifyParamVO, mpMap, priceStr);
        // 会员升级微信通知模板
        dataList = new ArrayList<>();
        dataList.add(new WxMaTemplateData("first", "会员升级通知"));
        dataList.add(new WxMaTemplateData("keyword1",notifyParamVO.getLevelName()));
        dataList.add(new WxMaTemplateData("keyword2","成功！"));
        dataList.add(new WxMaTemplateData("remark","感谢您的支持！"));
        mpMap.put(SendTypeEnum.MEMBER_LEVEL.getValue(),dataList);

        return mpMap.get(notifyParamVO.getSendType());
    }

    /**
     *
     * @param notifyParamVO 通知信息
     * @param shopName 店铺名称
     * @param mpMap 模板map
     * @param priceStr 价格
     */
    private void orderWxMaTemplate(NotifyParamVO notifyParamVO, String shopName, Map<Integer, List<WxMaTemplateData>> mpMap, String priceStr) {
        List<WxMaTemplateData> dataList = new ArrayList<>();
        String firstData = "亲爱的" + notifyParamVO.getNickName() + ",您在商城购买的宝贝还未付款哦！为确保您心爱的宝贝早日启程、顺利抵达，记得尽早付款哦！";
        dataList.add(new WxMaTemplateData("first", firstData));
        dataList.add(new WxMaTemplateData("keyword1",notifyParamVO.getBizId().toString()));
        dataList.add(new WxMaTemplateData("keyword2", DateUtil.format(notifyParamVO.getCreateTime(), DatePattern.NORM_DATETIME_PATTERN)));
        dataList.add(new WxMaTemplateData("keyword3",notifyParamVO.getSpuName()));
        dataList.add(new WxMaTemplateData("remark","请您及时付款以免订单过期。"));
        mpMap.put(SendTypeEnum.PRESS_PAY.getValue(),dataList);

        dataList = new ArrayList<>();
        dataList.add(new WxMaTemplateData("first", "你好，你的订单已付款成功。"));
        dataList.add(new WxMaTemplateData("keyword1",notifyParamVO.getBizId().toString()));
        dataList.add(new WxMaTemplateData("keyword2",notifyParamVO.getSpuName()));
        dataList.add(new WxMaTemplateData("keyword3",DateUtil.format(new Date(),DatePattern.NORM_DATETIME_PATTERN)));
        dataList.add(new WxMaTemplateData("keyword4",priceStr));
        dataList.add(new WxMaTemplateData("remark","感谢您的支持！"));
        mpMap.put(SendTypeEnum.PAY_SUCCESS.getValue(),dataList);

        dataList = new ArrayList<>();
        dataList.add(new WxMaTemplateData("first", "您的退款申请商家已同意"));
        dataList.add(new WxMaTemplateData("keyword1",notifyParamVO.getBizId().toString()));
        dataList.add(new WxMaTemplateData("keyword2",notifyParamVO.getSpuName()));
        dataList.add(new WxMaTemplateData("keyword3", priceStr));
        dataList.add(new WxMaTemplateData("remark","请及时登录商城，查看退款信息"));
        mpMap.put(SendTypeEnum.AGREE_REFUND.getValue(),dataList);

        dataList = new ArrayList<>();
        dataList.add(new WxMaTemplateData("first", "你的退款申请被商家驳回，可与商家协商沟通"));
        dataList.add(new WxMaTemplateData("keyword1",notifyParamVO.getBizId().toString()));
        dataList.add(new WxMaTemplateData("keyword2",priceStr));
        dataList.add(new WxMaTemplateData("keyword3",notifyParamVO.getRejectMessage()));
        dataList.add(new WxMaTemplateData("remark","请及时登录商城，查看退款信息"));
        mpMap.put(SendTypeEnum.REFUSE_REFUND.getValue(),dataList);

        dataList = new ArrayList<>();
        dataList.add(new WxMaTemplateData("first", "尊敬的客户，您的订单提货成功啦。"));
        dataList.add(new WxMaTemplateData("keyword1",notifyParamVO.getSpuName()));
        dataList.add(new WxMaTemplateData("keyword2",priceStr));
        dataList.add(new WxMaTemplateData("keyword3",notifyParamVO.getProdNum().toString()));
        dataList.add(new WxMaTemplateData("keyword4",DateUtil.format(new Date(),DatePattern.NORM_DATETIME_PATTERN)));
        dataList.add(new WxMaTemplateData("remark","商城感谢您的支持！"));
        mpMap.put(SendTypeEnum.WRITE_OFF.getValue(),dataList);

        dataList = new ArrayList<>();
        dataList.add(new WxMaTemplateData("first", "商品已在送往客官的路上"));
        dataList.add(new WxMaTemplateData("keyword1",notifyParamVO.getSpuName()));
        dataList.add(new WxMaTemplateData("keyword2","已发货"));
        dataList.add(new WxMaTemplateData("keyword3", notifyParamVO.getDvyName()));
        dataList.add(new WxMaTemplateData("keyword4",notifyParamVO.getDvyFlowId()));
        dataList.add(new WxMaTemplateData("remark",DateUtil.format(new Date(),DatePattern.NORM_DATETIME_PATTERN)));
        mpMap.put(SendTypeEnum.DELIVERY.getValue(),dataList);

        dataList = new ArrayList<>();
        dataList.add(new WxMaTemplateData("first", "您有退款单即将超时，请及时联系商家处理"));
        dataList.add(new WxMaTemplateData("keyword1",shopName));
        dataList.add(new WxMaTemplateData("keyword2","1笔"));
        dataList.add(new WxMaTemplateData("remark","如有疑问请及时与客服联系！"));
        mpMap.put(SendTypeEnum.REFUND_OUT_TIME.getValue(),dataList);
    }

    /**
     * 团购微信通知模板
     * @param notifyParamVO 通知信息
     * @param mpMap 模板map
     * @param priceStr 价格
     */
    private void groupWxMaTemplate(NotifyParamVO notifyParamVO, Map<Integer, List<WxMaTemplateData>> mpMap, String priceStr) {
        List<WxMaTemplateData> dataList = new ArrayList<>();
        dataList.add(new WxMaTemplateData("first", "您参加的拼团因为有效期内未成团，我们将在3个工作日内为您安排自动退款"));
        dataList.add(new WxMaTemplateData("keyword1",notifyParamVO.getSpuName()));
        dataList.add(new WxMaTemplateData("keyword2",priceStr));
        dataList.add(new WxMaTemplateData("keyword3", "商城"));
        dataList.add(new WxMaTemplateData("remark","点击进入订单详情页，查看退款进度！"));
        mpMap.put(SendTypeEnum.GROUP_FAIL.getValue(),dataList);

        dataList = new ArrayList<>();
        dataList.add(new WxMaTemplateData("first", "恭喜你，拼团已经成功！"));
        dataList.add(new WxMaTemplateData("keyword1",notifyParamVO.getSpuName()));
        dataList.add(new WxMaTemplateData("keyword2",priceStr));
        dataList.add(new WxMaTemplateData("remark","请登录商城，查看订单发货信息"));
        mpMap.put(SendTypeEnum.GROUP_SUCCESS.getValue(),dataList);

        dataList = new ArrayList<>();
        dataList.add(new WxMaTemplateData("first", "恭喜你，开团已经成功！"));
        dataList.add(new WxMaTemplateData("keyword1",notifyParamVO.getSpuName()));
        dataList.add(new WxMaTemplateData("keyword2",priceStr));
        dataList.add(new WxMaTemplateData("remark","快去小程序将参团链接分享给好友进行参团吧！"));
        mpMap.put(SendTypeEnum.GROUP_START.getValue(),dataList);
    }

    /**
     * 商家微信通知模板
     * @param notifyParamVO 通知信息
     * @param mpMap 模板map
     * @param priceStr 价格
     */
    private void multishopWxMaTemplate(NotifyParamVO notifyParamVO, Map<Integer, List<WxMaTemplateData>> mpMap, String priceStr) {
        List<WxMaTemplateData> dataList = new ArrayList<>();
        dataList.add(new WxMaTemplateData("first", "亲，物流显示您的订单买家已经确认收货！"));
        dataList.add(new WxMaTemplateData("keyword1", notifyParamVO.getBizId().toString()));
        dataList.add(new WxMaTemplateData("keyword2",notifyParamVO.getSpuName()));
        dataList.add(new WxMaTemplateData("keyword3","已确认收货"));
        dataList.add(new WxMaTemplateData("remark","如有疑问请及时与客服联系！"));
        mpMap.put(SendTypeEnum.RECEIPT_ORDER.getValue(),dataList);

        dataList = new ArrayList<>();
        dataList.add(new WxMaTemplateData("first", "店铺有新的退款申请，请及时处理"));
        dataList.add(new WxMaTemplateData("keyword1",priceStr));
        dataList.add(new WxMaTemplateData("keyword2",notifyParamVO.getRejectMessage()));
        dataList.add(new WxMaTemplateData("keyword3", DateUtil.format(new Date(), DatePattern.NORM_DATETIME_PATTERN)));
        dataList.add(new WxMaTemplateData("keyword4","退款金额原路返回"));
        dataList.add(new WxMaTemplateData("keyword5",notifyParamVO.getRemark()));
        dataList.add(new WxMaTemplateData("remark","请您尽快请登录商家后台操作处理。"));
        mpMap.put(SendTypeEnum.LAUNCH_REFUND.getValue(),dataList);

        dataList = new ArrayList<>();
        dataList.add(new WxMaTemplateData("first", "你的买家已退货，请及时处理"));
        dataList.add(new WxMaTemplateData("keyword1",notifyParamVO.getBizId().toString()));
        dataList.add(new WxMaTemplateData("keyword2",notifyParamVO.getRemark()));
        dataList.add(new WxMaTemplateData("remark","如有疑问请及时与客服联系！"));
        mpMap.put(SendTypeEnum.RETURN_REFUND.getValue(),dataList);
    }

    /**
     * 添加消息
     * @notifyParamVO notifyParamVO 参数
     * @notifyParamVO content 消息内容
     * @notifyParamVO template 模板
     * @notifyParamVO isSend 发送状态
     * @notifyParamVO value 发送类型
     * @notifyParamVO notifyLogs 消息集合
     */
    private void addNotifyLog(NotifyParamVO notifyParamVO, String content, NotifyTemplateVO template, Boolean isSend, Integer value, List<NotifyLog> notifyLogs) {
        NotifyLog notifyLog = new NotifyLog();
        notifyLog.setCreateTime(new Date());
        notifyLog.setShopId(notifyParamVO.getShopId());
        notifyLog.setShopName(notifyParamVO.getShopName());
        // 如果大于则为店铺自行接收的消息
        if(notifyParamVO.getSendType() >= Constant.MULTI_SHOP_MESSAGE_TYPE) {
            notifyLog.setRemindId(notifyParamVO.getShopId().toString());
        }else{
            notifyLog.setRemindId(notifyParamVO.getUserId().toString());
        }
        notifyLog.setSendType(notifyParamVO.getSendType());
        notifyLog.setBizId(notifyParamVO.getBizId());
        notifyLog.setMessage(content);
        notifyLog.setNickName(notifyParamVO.getNickName());
        notifyLog.setUserMobile(notifyParamVO.getMobile());
        notifyLog.setRemindType(value);
        notifyLog.setTemplateId(template.getTemplateId());
        notifyLog.setStatus(isSend ? 1:0);

        notifyLogs.add(notifyLog);

    }


    private Map<String, String> getSmsParam(NotifyParamVO notifyParamVO, String shopName) {
        Map<String, String> smsParam = new HashMap<>(20);
        // 没有orderNumber用orderId
        smsParam.put("orderNumber",notifyParamVO.getBizId().toString());
        smsParam.put("shopName",shopName);
        smsParam.put("prodName",notifyParamVO.getSpuName());
        if(StrUtil.isNotBlank(notifyParamVO.getNickName())){
            smsParam.put("nickName", notifyParamVO.getNickName());
        }else {
            smsParam.put("nickName","用户");
        }
        smsParam.put("levelName",notifyParamVO.getLevelName());
        smsParam.put("date",DateUtil.format(notifyParamVO.getDvyTime(),
                DatePattern.NORM_DATETIME_PATTERN));
        smsParam.put("cancelTime",notifyParamVO.getCancelTime());
        smsParam.put("price",String.valueOf(notifyParamVO.getPrice()));
        smsParam.put("dvyFlowId",notifyParamVO.getDvyFlowId());
        smsParam.put("stationName",notifyParamVO.getStationName());
        smsParam.put("dvyName",notifyParamVO.getDvyName());
        smsParam.put("dvyTime",DateUtil.format(notifyParamVO.getDvyTime(),
                DatePattern.NORM_DATETIME_PATTERN));
        smsParam.put("day", String.valueOf(Constant.MAX_REFUND_APPLY_TIME));
        smsParam.put("hour", notifyParamVO.getHour().toString());
        return smsParam;
    }



    /**
     * 短信内容替换
     * @notifyParamVO maps 替换内容
     */
    private String replaceContent(Map<String, String> maps,String content) {
        for (Map.Entry<String, String> element : maps.entrySet()) {
            if(StrUtil.isNotBlank(element.getValue())) {
                content = content.replace("${" + element.getKey() + "}", element.getValue());
            }
        }
        return content;
    }

}
