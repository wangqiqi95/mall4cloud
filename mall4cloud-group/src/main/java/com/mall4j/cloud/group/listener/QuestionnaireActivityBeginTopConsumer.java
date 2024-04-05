package com.mall4j.cloud.group.listener;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.biz.feign.WxMaApiFeignClient;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptTmessageSendVO;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptTmessageVO;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptUserRecordVO;
import com.mall4j.cloud.common.constant.MaSendTypeEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.common.util.DateUtils;
import com.mall4j.cloud.group.bo.QuestionnaireActivityNotifyBo;
import com.mall4j.cloud.group.mapper.QuestionnaireGiftMapper;
import com.mall4j.cloud.group.mapper.QuestionnaireMapper;
import com.mall4j.cloud.group.mapper.QuestionnaireUserAnswerRecordMapper;
import com.mall4j.cloud.group.model.Questionnaire;
import com.mall4j.cloud.group.model.QuestionnaireGift;
import com.mall4j.cloud.group.model.QuestionnaireUserAnswerRecord;
import com.mall4j.cloud.group.service.QuestionnaireService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RocketMQMessageListener(topic = RocketMqConstant.QUESTIONNAIRE_ACTIVITY_BEGIN_TOP, consumerGroup = "GID_" + RocketMqConstant.QUESTIONNAIRE_ACTIVITY_BEGIN_TOP)
public class QuestionnaireActivityBeginTopConsumer implements RocketMQListener<QuestionnaireActivityNotifyBo> {

    @Autowired
    QuestionnaireUserAnswerRecordMapper questionnaireUserAnswerRecordMapper;
    @Autowired
    QuestionnaireMapper questionnaireMapper;
    @Autowired
    WxMaApiFeignClient wxMaApiFeignClient;
    @Autowired
    QuestionnaireService questionnaireService;
    @Autowired
    QuestionnaireGiftMapper questionnaireGiftMapper;
    @Resource
    private OnsMQTemplate sendMaTemplateMessage;

    @Override
    public void onMessage(QuestionnaireActivityNotifyBo message) {
        log.info("问卷活动判断用户是否提交问卷1。参数：{}", JSONObject.toJSONString(message));
        QuestionnaireUserAnswerRecord record = questionnaireUserAnswerRecordMapper.getById(message.getUserAnswerRecordId());
        if(record==null){
            return;
        }
        if(record.getSubmitted()==1){
            log.info("问卷活动判断用户是否提交问卷2,当前用户已经提交问卷，不做任何操作。");
            return;
        }
        Questionnaire questionnaire = questionnaireService.getById(record.getActivityId());
        QuestionnaireGift gift = questionnaireGiftMapper.selectByActivityId(questionnaire.getId());

        String giftName = "";
        //礼品类型 0积分 1优惠券 2抽奖 3实物
        if(gift.getGiftType()==0){
            giftName = gift.getGiftId()+" 积分";
        }else if(gift.getGiftType()==1){
            giftName = "优惠券";
        }else if(gift.getGiftType()==2){
            giftName = "抽奖机会";
        }else{
            giftName = gift.getGiftName();
        }
        String tip = StrUtil.format("参与问卷可获得:{}",giftName);
        final String finalGiftName = giftName;
        //查询用户是否接受订阅，并发送订阅消息
        //查询问卷活动开始订阅模版
        List<String> userids = new ArrayList<>();
        userids.add(StrUtil.toString(record.getUserId()));
        ServerResponseEntity<WeixinMaSubscriptTmessageVO> subscriptResp = wxMaApiFeignClient.getinsIderSubscriptTmessage(MaSendTypeEnum.QUESTIONNAIRE_ACTIVITY_BEGIN.getValue(), userids);
        log.info("问卷活动开始: {}", JSONObject.toJSONString(subscriptResp));
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
                     * 当前积分商城 场景下 活动名称{activityName}、开始时间{beginTime}、结束时间{endTime} 温馨提示 {tip} 奖品{prize} 活动内容{activityContent}
                     * 构建参数map.
                     */
                    Map<String, String> paramMap = new HashMap();
                    paramMap.put("{activityName}", questionnaire.getTitle());
                    paramMap.put("{beginTime}", DateUtils.dateToString(questionnaire.getActivityBeginTime()));
                    paramMap.put("{endTime}", DateUtils.dateToString(questionnaire.getActivityEndTime()));
                    paramMap.put("{prize}", finalGiftName);
                    paramMap.put("{activityContent}", tip);
                    paramMap.put("{tip}", questionnaire.getBeginTip());

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
                    String  pageUrl = "packageA/pages/question/question?id="+questionnaire.getId();
                    sendVO.setPage(pageUrl);
                    return sendVO;
                }).collect(Collectors.toList());
                log.info("问卷活动开始，待发送列表集合:{}",JSONObject.toJSONString(sendList));
                sendMaTemplateMessage.syncSend(sendList);
            }
        }else {
            log.error("查询积分活动上新模版失败，返回值为：{}", JSONObject.toJSONString(subscriptResp));
        }

    }

}
