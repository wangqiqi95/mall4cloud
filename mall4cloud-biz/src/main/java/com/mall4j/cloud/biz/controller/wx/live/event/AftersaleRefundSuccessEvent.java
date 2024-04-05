package com.mall4j.cloud.biz.controller.wx.live.event;

import cn.hutool.core.map.MapUtil;
import com.mall4j.cloud.biz.wx.wx.util.WechatUtils;
import com.mall4j.cloud.biz.wx.wx.api.live.OrderApi;
import com.mall4j.cloud.biz.config.WxConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 平台退款成功
 */
@Service
@Slf4j
public class AftersaleRefundSuccessEvent implements INotifyEvent, InitializingBean {

    private static final String method = "aftersale_refund_success";
    @Autowired
    OrderApi orderApi;
    @Autowired
    WxConfig wxConfig;
    /**
     *<xml>
     *      <ToUserName>gh_abcdefg</ToUserName>
     *      <FromUserName>oABCD</FromUserName>
     *      <CreateTime>12344555555</CreateTime>
     *      <MsgType>event</MsgType>
     *      <Event>aftersale_refund_success</Event>
     *      <aftersale_info>
     *           <aftersale_id>123456</aftersale_id>
     *           <out_aftersale_id>1234567</order_id>
     *      </aftersale_info>
     * </xml>
     *
     *
     * @param postData
     * @return
     */
    @Override
    public String doEvent(String postData) throws Exception {
        log.info("平台退款成功，输入参数：{}",postData);
        Map<String, String> resultMap = WechatUtils.xmlToMap(postData);


        Long aftersaleId = MapUtil.getLong(resultMap, "aftersale_id");
        String outAftersaleId = MapUtil.getStr(resultMap, "out_aftersale_id");

        // 根据售后单号查询售后数据

        return "success";
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.register(method,this);
    }

}
