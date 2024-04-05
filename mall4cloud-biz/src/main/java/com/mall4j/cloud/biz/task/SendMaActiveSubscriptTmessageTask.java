package com.mall4j.cloud.biz.task;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;

import com.mall4j.cloud.api.biz.feign.WxMaApiFeignClient;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptTmessageSendVO;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptTmessageVO;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptUserRecordVO;
import com.mall4j.cloud.api.platform.feign.ConfigFeignClient;
import com.mall4j.cloud.biz.service.WeixinMaSubscriptTmessageService;
import com.mall4j.cloud.biz.service.WeixinMaSubscriptUserRecordService;
import com.mall4j.cloud.common.constant.ConfigNameConstant;
import com.mall4j.cloud.common.constant.MaSendTypeEnum;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.DateUtils;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description 发送活动订阅消息
 * @Author axin
 * @Date 2023-04-14 14:04
 **/
@Component
@Slf4j
public class SendMaActiveSubscriptTmessageTask {
    @Autowired
    private WeixinMaSubscriptTmessageService weixinMaSubscriptTmessageService;

    @Autowired
    private ConfigFeignClient configFeign;

    @Autowired
    private WeixinMaSubscriptUserRecordService weixinMaSubscriptUserRecordService;

    private final static String H5_ACTIVITY_MESSAGE_CONTENT="H5_ACTIVITY_MESSAGE_CONTENT";


    @XxlJob("sendMaActiveMsg")
    public void sendMaActiveMsg()  {
        long startTime=System.currentTimeMillis();
        log.info("--发送抽奖活动消息开始");
        DateTime dateTime = DateUtil.offsetDay(new Date(), -1);

        WeixinMaSubscriptTmessageVO weixinMaSubscriptTmessageVO = weixinMaSubscriptUserRecordService.queryMaSubscriptUserRecordBySendType(MaSendTypeEnum.ACTIVITY_BEGIN_H5.getValue(),
                DateUtil.beginOfDay(dateTime), DateUtil.endOfDay(dateTime));
        ServerResponseEntity<String> config = configFeign.getConfig(H5_ACTIVITY_MESSAGE_CONTENT);
        if(config.isFail() && StringUtils.isBlank(config.getData())){
            throw new LuckException("未获到活动消息配置");
        }
        Map<String,String> configInfoMap = JSON.parseObject(config.getData(), Map.class);

        log.info("抽奖活动人员信息:{}", JSONObject.toJSONString(weixinMaSubscriptTmessageVO));
        if (Objects.nonNull(weixinMaSubscriptTmessageVO)) {
            List<WeixinMaSubscriptUserRecordVO> userRecords = weixinMaSubscriptTmessageVO.getUserRecords();
            if (!CollectionUtils.isEmpty(userRecords)) {
                List<WeixinMaSubscriptTmessageSendVO> notifyList = new ArrayList<>();
                Map<String,String> paramMap = new HashMap(16);
                paramMap.put("{activityName}", configInfoMap.get("activityName"));
                paramMap.put("{activityRemark}", configInfoMap.get("activityRemark"));
                paramMap.put("{startTime}", configInfoMap.get("startTime"));
                paramMap.put("{remark}", configInfoMap.get("remark"));
                for (WeixinMaSubscriptUserRecordVO userRecord : userRecords) {
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
                weixinMaSubscriptTmessageService.sendMessageToUser(notifyList);
            }
        }

        log.info("--发送抽奖活动消息结束 耗时：{}ms",System.currentTimeMillis() - startTime);
    }
}
