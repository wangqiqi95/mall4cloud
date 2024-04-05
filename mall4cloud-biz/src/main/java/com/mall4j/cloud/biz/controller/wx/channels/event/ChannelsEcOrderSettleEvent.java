package com.mall4j.cloud.biz.controller.wx.channels.event;

import com.mall4j.cloud.biz.controller.wx.live.event.INotifyEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

/**
 *订单结算成功
 *
 */
@Slf4j
@Service
public class ChannelsEcOrderSettleEvent implements INotifyEvent, InitializingBean {

    private static final String method = "channels_ec_order_settle";


    /**
     * https://developers.weixin.qq.com/doc/channels/API/order/callback/channels_ec_order_cancel.html
     *
     * {
     *     "ToUserName": "gh_*",
     *     "FromUserName": "OPENID",
     *     "CreateTime": 1662480000,
     *     "MsgType": "event",
     *     "Event": "channels_ec_order_settle",
     *     "order_info": {
     *         "order_id": 3705115058471208928,
     *         "settle_time": 1662480000
     *     }
     * }
     *
     * @param postData
     * @return
     * @throws Exception
     */
    @Override
    public String doEvent(String postData) throws Exception {
        log.info("订单结算成功回调，输入参数：{}", postData);

        return "success";
    }

    @Override
    public void register(String event, INotifyEvent notifyEvent) {
        INotifyEvent.super.register(event, notifyEvent);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.register(method,this);
    }
}
