
package com.mall4j.cloud.biz.service;

import com.mall4j.cloud.biz.vo.NotifyParamVO;


/**
 * 统一发送通知
 * @author lhd
 */
public interface SendMessageService {

    /**
     * 推送通知
     * @param notifyParamVO 统一推送消息所需参数
     */
    void sendMsg(NotifyParamVO notifyParamVO);

}
