package com.mall4j.cloud.biz.controller.wx.live.event;

import cn.hutool.core.map.MapUtil;
import com.mall4j.cloud.biz.wx.wx.util.WechatUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 商家处理退款申请超时
 * 商家处理退款申请超时，视频号会认为商家同意而直接同意退款
 * TODO
 *
 */
@Service
@Slf4j
public class AftersaleMerchantHandleRefundOverdueEvent implements INotifyEvent, InitializingBean {
    private static final String method = "aftersale_merchant_handle_refund_overdue";

    /**
     * 48小时，商家不处理退款记录，视频号会自动审核通过执行退款
     * 这里写入refund_info表记录
     *
     * 因为退款超时之后，订单售后成功还有单独的回调回来，在那里会修改订单还有退单的状态。
     * 所以这里只是添加refund_info表记录
     *
     * <xml>
     *      <ToUserName>gh_abcdefg</ToUserName>
     *      <FromUserName>oABCD</FromUserName>
     *      <CreateTime>12344555555</CreateTime>
     *      <MsgType>event</MsgType>
     *      <Event>aftersale_merchant_handle_refund_overdue</Event>
     *      <aftersale_info>
     *           <aftersale_id>123456</aftersale_id>
     *           <out_aftersale_id>1234567</out_aftersale_id>
     *      </aftersale_info>
     * </xml>
     */
    @Override
    public String doEvent(String postData) throws Exception {
        log.info("商家处理退款申请超时，输入参数：{}", postData);
        Map<String, String> resultMap = WechatUtils.xmlToMap(postData);
        Long aftersaleId = MapUtil.getLong(resultMap, "aftersale_id");
        return "success";
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        this.register(method, this);
    }
}
