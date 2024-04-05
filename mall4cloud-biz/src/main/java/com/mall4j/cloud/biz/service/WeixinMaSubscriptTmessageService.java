package com.mall4j.cloud.biz.service;

import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptTmessageSendVO;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptTmessageVO;
import com.mall4j.cloud.biz.dto.WeixinMaSubscriptTmessageDTO;
import com.mall4j.cloud.biz.dto.WeixinSubTmessageStatusDTO;
import com.mall4j.cloud.biz.dto.WeixinTmessageStatusDTO;
import com.mall4j.cloud.biz.vo.WeixinMaSubscriptTmessageTypeVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;

import java.util.List;

/**
 * 微信小程序订阅消息service
 *
 * @luzhengxiang
 * @create 2022-03-05 4:45 PM
 **/
public interface WeixinMaSubscriptTmessageService {

    List<WeixinMaSubscriptTmessageTypeVO> getList(String appId,Integer businessType,Integer sendType);

    ServerResponseEntity<Void> saveTo(WeixinMaSubscriptTmessageDTO subscriptTmessageDTO);

    ServerResponseEntity<Void> updateTo(WeixinMaSubscriptTmessageDTO subscriptTmessageDTO);

    void updateStatus(WeixinSubTmessageStatusDTO statusDTO);

    void deleteById(String id);

    void sendMessageToUser(List<WeixinMaSubscriptTmessageSendVO> sendVOList);

     ServerResponseEntity<WeixinMaSubscriptTmessageVO> getSubscriptTmessage(Integer sendType);
}
