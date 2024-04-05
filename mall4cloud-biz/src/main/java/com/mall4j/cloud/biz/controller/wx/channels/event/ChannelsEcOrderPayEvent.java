package com.mall4j.cloud.biz.controller.wx.channels.event;

import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.platform.feign.TentacleContentFeignClient;
import com.mall4j.cloud.biz.controller.wx.live.event.INotifyEvent;
import com.mall4j.cloud.biz.service.channels.ChannelsSharerService;
import com.mall4j.cloud.biz.service.channels.EcOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *订单支付成功
 *
 */
@Slf4j
@Service
public class ChannelsEcOrderPayEvent implements INotifyEvent, InitializingBean {

    private static final String method = "channels_ec_order_pay";
    @Autowired
    EcOrderService ecOrderService;
    @Autowired
    ChannelsSharerService channelsSharerService;
    @Autowired
    TentacleContentFeignClient tentacleContentFeignClient;


    /**
     * https://developers.weixin.qq.com/doc/channels/API/order/callback/channels_ec_order_pay.html
     *
     *{
     *     "ToUserName": "gh_*",
     *     "FromUserName": "OPENID",
     *     "CreateTime": 1662480000,
     *     "MsgType": "event",
     *     "Event": "channels_ec_order_pay",
     *     "order_info": {
     *         "order_id": 3705115058471208928,
     *         "pay_time": 1658509200
     *     }
     * }
     *
     * @param postData
     * @return
     * @throws Exception
     */
    @Override
    public String doEvent(String postData) throws Exception {
        log.info("订单支付成功回调，输入参数：{}", postData);
        /**
         * 扣减商品的总库存，视频号可售卖库存
         * 修改订单状态
         * 优惠券
         *
         */
        JSONObject postDataJson =  JSONObject.parseObject(postData);
        JSONObject orderInfoJson = postDataJson.getJSONObject("order_info");
        Long outOrderId = orderInfoJson.getLongValue("order_id");
        String tentacleNo = "";

//        orderFeignClient.ecOrderPay(outOrderId,tentacleNo);
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
