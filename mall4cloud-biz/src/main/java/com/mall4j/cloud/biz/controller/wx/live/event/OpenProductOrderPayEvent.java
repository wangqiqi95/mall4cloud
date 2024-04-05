package com.mall4j.cloud.biz.controller.wx.live.event;

import cn.hutool.core.map.MapUtil;
import com.mall4j.cloud.biz.wx.wx.util.WechatUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 订单支付成功
 * https://developers.weixin.qq.com/miniprogram/dev/platform-capabilities/business-capabilities/ministore/minishopopencomponent2/callback/order_success.html
 */
@Slf4j
@Service
public class OpenProductOrderPayEvent implements INotifyEvent, InitializingBean {

    private static final String method = "open_product_order_pay";
    /**
     *
     * <xml>
     *      <ToUserName>gh_abcdefg</ToUserName>
     *      <FromUserName>oABCD</FromUserName>
     *      <CreateTime>12344555555</CreateTime>
     *      <MsgType>event</MsgType>
     *      <Event>open_product_order_pay</Event>
     *      <order_info>
     *           <out_order_id>123456</out_order_id>
     *           <order_id>1234567</order_id>
     *           <transaction_id>42000000123123</transaction_id>
     *           <pay_time>2021-12-30 22:31:00</pay_time>
     *      </order_info>
     * </xml>
     * @param postData
     * @return
     */
    @Override
    public String doEvent(String postData) throws Exception {
        log.info("视频号支付回调接收请求，输入参数：{}",postData);
        Map<String, String> resultMap = WechatUtils.xmlToMap(postData);

        String outOrderId = MapUtil.getStr(resultMap, "out_order_id");
        String transactionId = MapUtil.getStr(resultMap, "transactionId");
        String payTime = MapUtil.getStr(resultMap, "pay_time");

        return "success";
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.register(method,this);
    }
}
