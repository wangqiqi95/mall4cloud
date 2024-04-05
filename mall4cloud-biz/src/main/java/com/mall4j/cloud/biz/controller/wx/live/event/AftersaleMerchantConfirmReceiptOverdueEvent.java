package com.mall4j.cloud.biz.controller.wx.live.event;

import cn.hutool.core.map.MapUtil;
import com.mall4j.cloud.biz.wx.wx.util.WechatUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 商家确认收货超时
 * 商家确认收货超时会直接直接退款。 这里做跟退款超时一样的操作。
 */
@Slf4j
@Service
public class AftersaleMerchantConfirmReceiptOverdueEvent implements INotifyEvent, InitializingBean {

    private static final String method = "aftersale_merchant_confirm_receipt_overdue";

    /**
     * <xml>
     *      <ToUserName>gh_abcdefg</ToUserName>
     *      <FromUserName>oABCD</FromUserName>
     *      <CreateTime>12344555555</CreateTime>
     *      <MsgType>event</MsgType>
     *      <Event>aftersale_merchant_confirm_receipt_overdue</Event>
     *      <aftersale_info>
     *           <aftersale_id>123456</aftersale_id>
     *           <out_aftersale_id>1234567</out_aftersale_id>
     *      </aftersale_info>
     * </xml>
     *
     * @param postData
     * @return
     */
    @Override
    public String doEvent(String postData) throws Exception {
        log.info("商家确认收货超时，输入参数：{}", postData);

        Map<String, String> resultMap = WechatUtils.xmlToMap(postData);
        Long aftersaleId = MapUtil.getLong(resultMap, "aftersale_id");

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
