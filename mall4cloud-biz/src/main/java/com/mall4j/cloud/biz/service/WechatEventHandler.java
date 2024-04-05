package com.mall4j.cloud.biz.service;

import com.mall4j.cloud.biz.dto.WxCpMsgDTO;

/**
 * @Date 2022-01-20
 * @Created by lt
 */
public interface WechatEventHandler {

    String exec(WxCpMsgDTO msgDTO) throws Exception;
}
