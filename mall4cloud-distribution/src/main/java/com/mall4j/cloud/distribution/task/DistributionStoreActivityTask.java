package com.mall4j.cloud.distribution.task;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.biz.feign.WxMaApiFeignClient;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptTmessageSendVO;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptTmessageVO;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptUserRecordVO;
import com.mall4j.cloud.common.cache.constant.BizCacheNames;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.constant.MaSendTypeEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.util.DateUtils;
import com.mall4j.cloud.distribution.mapper.DistributionStoreActivityMapper;
import com.mall4j.cloud.distribution.model.DistributionStoreActivity;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class DistributionStoreActivityTask {

    @Autowired
    private DistributionStoreActivityMapper distributionStoreActivityMapper;
    @Autowired
    private OnsMQTemplate sendMaSubcriptMessageTemplate;
    @Autowired
    private WxMaApiFeignClient wxMaApiFeignClient;

    /**
     * 门店活动上新提醒
     */
    @XxlJob("storeActivityNewRemind")
    public void storeActivityNewRemind() {
        log.info("开始门店活动上新提醒任务》》》》》》》》》》》》》》》》》》》》》");
        try {
            List<DistributionStoreActivity> distributionStoreActivityList =  distributionStoreActivityMapper.remindStoreActivityList(3);
            if (!CollectionUtils.isEmpty(distributionStoreActivityList)) {
                distributionStoreActivityList.stream().forEach(d -> {
                    sendActivityNewMessage(d);
                });
            }
        } catch (Exception e) {
            log.error("门店活动上新提醒任务失败",e);
        }
        log.info("结束门店活动上新提醒任务》》》》》》》》》》》》》》》》》》》》》");
    }

    /**
     * 门店活动开始前提醒
     */
    @XxlJob("storeActivityStartRemind")
    public void storeActivityStartRemind() {
        log.info("开始门店活动开始前提醒任务》》》》》》》》》》》》》》》》》》》》》");
        try {
            List<DistributionStoreActivity> distributionStoreActivityList =  distributionStoreActivityMapper.remindStoreActivityList(1);
            if (!CollectionUtils.isEmpty(distributionStoreActivityList)) {
                distributionStoreActivityList.stream().forEach(d -> {
                    sendMaSubcriptMessage(d);
                });
            }
        } catch (Exception e) {
            log.error("门店活动开始前提醒任务失败",e);
        }
        log.info("结束门店活动开始前提醒任务》》》》》》》》》》》》》》》》》》》》》");
    }

    /**
     * 门店活动结束后提醒
     */
    @XxlJob("storeActivityEndRemind")
    public void storeActivityEndRemind() {
        log.info("开始门店活动结束后提醒任务》》》》》》》》》》》》》》》》》》》》》");
        try {
            List<DistributionStoreActivity> distributionStoreActivityList =  distributionStoreActivityMapper.remindStoreActivityList(2);
            if (!CollectionUtils.isEmpty(distributionStoreActivityList)) {
                distributionStoreActivityList.stream().forEach(d -> {
                    sendActivityEndMessage(d);
                });

            }
        } catch (Exception e) {
            log.error("门店活动结束后提醒任务失败",e);
        }
        log.info("结束门店活动结束后提醒任务》》》》》》》》》》》》》》》》》》》》》");
    }


    /**
     * 定时任务，修改门店活动排序权重
     */
    @XxlJob("updateStoreActivityWeight")
    public void updateStoreActivityWeight() {
        log.info("修改门店活动排序权重》》》》》》》》》》》》》》》》》》》》》");
        //活动列表排序优先根据权重排序，权重越高越靠前，权重相同，根据活动开始时间再倒序
        //进行中的活动权重给999 最高
        int result = distributionStoreActivityMapper.activityBeginUpdateWeight();
        log.info("进行中的活动更新权重，更新记录数：{}",result);
        //未开始的给99
        result = distributionStoreActivityMapper.activityToBeginUpdateWeight();
        log.info("未开始的活动更新权重，更新记录数：{}",result);
        //已经结束的给1
        result = distributionStoreActivityMapper.activityEndUpdateWeight();
        log.info("已经结束的活动更新权重，更新记录数：{}",result);
        log.info("修改门店活动排序权重》》》》》》》》》》》》》》》》》》》》》");
    }


    private void sendActivityNewMessage(DistributionStoreActivity distributionStoreActivity) {
        List<String> businessIds = new ArrayList<>();
        ServerResponseEntity<WeixinMaSubscriptTmessageVO> subscriptResp = wxMaApiFeignClient.getinsIderSubscriptTmessage(MaSendTypeEnum.ACTIVITY_NEW.getValue(),null);
        log.info("subscriptResp {}", JSONObject.toJSONString(subscriptResp.getData()));
        if (subscriptResp.isSuccess()) {
            WeixinMaSubscriptTmessageVO weixinMaSubscriptTmessageVO = subscriptResp.getData();
            List<WeixinMaSubscriptUserRecordVO> userRecords = weixinMaSubscriptTmessageVO.getUserRecords();
            if (!CollectionUtils.isEmpty(userRecords)) {
                List<WeixinMaSubscriptTmessageSendVO> notifyList = new ArrayList<>();
                for (WeixinMaSubscriptUserRecordVO userRecord : userRecords) {
                    //判断今天是否给当前会员发送订阅消息
                    if(!checkIsCanSend(userRecord.getUserId())){
                        continue;
                    }
                    Map<String,String> paramMap = new HashMap();
                    paramMap.put("{activityName}", distributionStoreActivity.getName());
                    paramMap.put("{activityRemark}", distributionStoreActivity.getAddress());
                    paramMap.put("{startTime}", DateUtils.dateToString(distributionStoreActivity.getStartTime()));
                    paramMap.put("{endTime}", DateUtils.dateToString(distributionStoreActivity.getEndTime()));
                    paramMap.put("{remark}", distributionStoreActivity.getNewReminder());
                    List<WeixinMaSubscriptTmessageSendVO.MsgData> msgDataList = weixinMaSubscriptTmessageVO.getTmessageValues().stream().map(t -> {
                        WeixinMaSubscriptTmessageSendVO.MsgData msgData = new WeixinMaSubscriptTmessageSendVO.MsgData();
                        msgData.setName(t.getTemplateValueName());
                        msgData.setValue(StrUtil.isEmpty(paramMap.get(t.getTemplateValue()))?t.getTemplateValue():paramMap.get(t.getTemplateValue()));
                        return msgData;
                    }).collect(Collectors.toList());
                    WeixinMaSubscriptTmessageSendVO sendVO = new WeixinMaSubscriptTmessageSendVO();
                    sendVO.setUserSubscriptRecordId(userRecord.getId());
                    sendVO.setData(msgDataList);
                    sendVO.setPage(weixinMaSubscriptTmessageVO.getPage());
                    notifyList.add(sendVO);
                }
                sendMaSubcriptMessageTemplate.syncSend(notifyList);
            }
        }
    }

    /**
     * 活动上新，一天，一个会员只可以发送一次订阅消息
     * @param userId
     * @return
     */
    public Boolean checkIsCanSend(Long userId){
        String today = DateUtil.today();
        String userSendKey = BizCacheNames.MA_MESSAGE_ACTIVITY_NEW + today + ":" + userId + ":";
        //不为空，说明当前会员已经发送过订阅消息。
        if(RedisUtil.hasKey(userSendKey)){
            return false;
        }
        RedisUtil.set(userSendKey,userId,60*60*24);
        return true;
    }

    private void sendMaSubcriptMessage(DistributionStoreActivity distributionStoreActivity) {
        List<String> businessIds = new ArrayList<>();
        businessIds.add(distributionStoreActivity.getId().toString());
        ServerResponseEntity<WeixinMaSubscriptTmessageVO> subscriptResp = wxMaApiFeignClient.getinsIderSubscriptTmessage(MaSendTypeEnum.ACTIVITY_BEGIN.getValue(),businessIds);
        log.info("subscriptResp {}", JSONObject.toJSONString(subscriptResp.getData()));
        if (subscriptResp.isSuccess()) {
            WeixinMaSubscriptTmessageVO weixinMaSubscriptTmessageVO = subscriptResp.getData();
            List<WeixinMaSubscriptUserRecordVO> userRecords = weixinMaSubscriptTmessageVO.getUserRecords();
            if (!CollectionUtils.isEmpty(userRecords)) {
                List<WeixinMaSubscriptTmessageSendVO> notifyList = userRecords.stream().map(u -> {
                    Map<String,String> paramMap = new HashMap();
                    paramMap.put("{activityName}", distributionStoreActivity.getName());
                    paramMap.put("{activityRemark}", distributionStoreActivity.getAddress());
                    paramMap.put("{startTime}", DateUtils.dateToString(distributionStoreActivity.getStartTime()));
                    paramMap.put("{endTime}", DateUtils.dateToString(distributionStoreActivity.getEndTime()));
                    paramMap.put("{remark}", distributionStoreActivity.getStartReminder());
                    List<WeixinMaSubscriptTmessageSendVO.MsgData> msgDataList = weixinMaSubscriptTmessageVO.getTmessageValues().stream().map(t -> {
                        WeixinMaSubscriptTmessageSendVO.MsgData msgData = new WeixinMaSubscriptTmessageSendVO.MsgData();
                        msgData.setName(t.getTemplateValueName());
                        msgData.setValue(StrUtil.isEmpty(paramMap.get(t.getTemplateValue()))?t.getTemplateValue():paramMap.get(t.getTemplateValue()));
                        return msgData;
                    }).collect(Collectors.toList());
                    WeixinMaSubscriptTmessageSendVO sendVO = new WeixinMaSubscriptTmessageSendVO();
                    sendVO.setUserSubscriptRecordId(u.getId());
                    sendVO.setData(msgDataList);
                    String pageUrl = StrUtil.format("/packageActivities/pages/activity/detail?id={}",distributionStoreActivity.getId());
                    sendVO.setPage(pageUrl);
                    return sendVO;
                }).collect(Collectors.toList());
                sendMaSubcriptMessageTemplate.syncSend(notifyList);
            }
        }
    }

    private void sendActivityEndMessage(DistributionStoreActivity distributionStoreActivity) {
        List<String> businessIds = new ArrayList<>();
        businessIds.add(distributionStoreActivity.getId().toString());
        ServerResponseEntity<WeixinMaSubscriptTmessageVO> subscriptResp = wxMaApiFeignClient.getinsIderSubscriptTmessage(MaSendTypeEnum.ACTIVITY_END.getValue(),businessIds);
        log.info("subscriptResp {}", JSONObject.toJSONString(subscriptResp.getData()));
        if (subscriptResp.isSuccess()) {
            WeixinMaSubscriptTmessageVO weixinMaSubscriptTmessageVO = subscriptResp.getData();
            List<WeixinMaSubscriptUserRecordVO> userRecords = weixinMaSubscriptTmessageVO.getUserRecords();
            if (!CollectionUtils.isEmpty(userRecords)) {
                List<WeixinMaSubscriptTmessageSendVO> notifyList = userRecords.stream().map(u -> {
                    Map<String,String> paramMap = new HashMap();
                    paramMap.put("{activityName}", distributionStoreActivity.getName());
                    paramMap.put("{activityRemark}", distributionStoreActivity.getAddress());
                    paramMap.put("{startTime}", DateUtils.dateToString(distributionStoreActivity.getStartTime()));
                    paramMap.put("{endTime}", DateUtils.dateToString(distributionStoreActivity.getEndTime()));
                    paramMap.put("{remark}", distributionStoreActivity.getEndReminder());
                    List<WeixinMaSubscriptTmessageSendVO.MsgData> msgDataList = weixinMaSubscriptTmessageVO.getTmessageValues().stream().map(t -> {
                        WeixinMaSubscriptTmessageSendVO.MsgData msgData = new WeixinMaSubscriptTmessageSendVO.MsgData();
                        msgData.setName(t.getTemplateValueName());
                        msgData.setValue(StrUtil.isEmpty(paramMap.get(t.getTemplateValue()))?t.getTemplateValue():paramMap.get(t.getTemplateValue()));
                        return msgData;
                    }).collect(Collectors.toList());
                    WeixinMaSubscriptTmessageSendVO sendVO = new WeixinMaSubscriptTmessageSendVO();
                    sendVO.setUserSubscriptRecordId(u.getId());
                    sendVO.setData(msgDataList);
                    String pageUrl = distributionStoreActivity.getEndRemindUrl();
                    if(StrUtil.isEmpty(pageUrl)){
                        pageUrl = "pages/home/index";
                    }
                    sendVO.setPage(pageUrl);
                    return sendVO;
                }).collect(Collectors.toList());
                sendMaSubcriptMessageTemplate.syncSend(notifyList);
            }
        }
    }

}
