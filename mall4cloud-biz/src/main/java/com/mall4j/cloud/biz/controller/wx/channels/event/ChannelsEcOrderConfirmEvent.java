package com.mall4j.cloud.biz.controller.wx.channels.event;

import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.biz.controller.wx.live.event.INotifyEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

/**
 *订单确认收货
 *
 */
@Slf4j
@Service
public class ChannelsEcOrderConfirmEvent implements INotifyEvent, InitializingBean {

    private static final String method = "channels_ec_order_confirm";


    /**
     * https://developers.weixin.qq.com/doc/channels/API/order/callback/channels_ec_order_confirm.html
     *
     * {
     *     "ToUserName": "gh_*",
     *     "FromUserName": "OPENID",
     *     "CreateTime": 1662480000,
     *     "MsgType": "event",
     *     "Event": "channels_ec_order_confirm",
     *     "order_info": {
     *         "order_id": 3705115058471208928,
     *         "confirm_type": 1
     *     }
     * }
     *
     * @param postData
     * @return
     * @throws Exception
     */
    @Override
    public String doEvent(String postData) throws Exception {
        log.info("订单确认收货回调，输入参数：{}", postData);
        JSONObject jsonObject =  JSONObject.parseObject(postData);

        JSONObject order_infoJson = jsonObject.getJSONObject("order_info");
        Long outOrderId = order_infoJson.getLongValue("order_id");
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
