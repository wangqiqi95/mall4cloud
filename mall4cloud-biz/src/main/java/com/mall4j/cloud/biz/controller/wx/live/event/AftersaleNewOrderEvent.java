package com.mall4j.cloud.biz.controller.wx.live.event;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.biz.dto.livestore.request.AfterSaleInfo;
import com.mall4j.cloud.api.biz.dto.livestore.response.AfterSaleResponse;
import com.mall4j.cloud.biz.wx.wx.util.WechatUtils;
import com.mall4j.cloud.biz.wx.wx.api.live.OrderApi;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.common.exception.LuckException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 订单发起售后
 */
@Service
@Slf4j
public class AftersaleNewOrderEvent implements INotifyEvent, InitializingBean {

    private static final String method = "aftersale_new_order";
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
     *      <Event>aftersale_new_order</Event>
     *      <aftersale_info>
     *           <aftersale_id>123456</aftersale_id>
     *           <order_id>1234567</order_id>
     *           <out_order_id>abc1234567</out_order_id>
     *      </aftersale_info>
     * </xml>
     *
     * @param postData
     * @return
     */
    @Override
    public String doEvent(String postData) throws Exception {
        log.info("视频号发起售后回调接收请求，输入参数：{}",postData);
        Map<String, String> resultMap = WechatUtils.xmlToMap(postData);

        String outOrderId = MapUtil.getStr(resultMap, "out_order_id");
        Long aftersaleId = MapUtil.getLong(resultMap, "aftersale_id");

        // 根据售后单号查询售后数据
        AfterSaleInfo afterSaleInfo = new AfterSaleInfo();
        afterSaleInfo.setAftersaleId(aftersaleId);
        AfterSaleResponse afterSaleResponse = orderApi.afterSale(wxConfig.getWxMaToken(),afterSaleInfo);
        log.info("请求售后微信返回数据:{}", JSONObject.toJSONString(afterSaleResponse));
        if (afterSaleResponse == null && afterSaleResponse.getErrcode() != 0 ) {
            throw new LuckException("请求售后数据异常！");
        }

        return "success";
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.register(method,this);
    }

}
