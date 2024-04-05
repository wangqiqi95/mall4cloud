package com.mall4j.cloud.biz.controller.wx.live.event;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.biz.dto.livestore.request.AfterSaleInfo;
import com.mall4j.cloud.api.biz.dto.livestore.response.AfterSaleResponse;
import com.mall4j.cloud.api.biz.dto.livestore.response.AfterSalesOrder;
import com.mall4j.cloud.biz.wx.wx.util.WechatUtils;
import com.mall4j.cloud.biz.wx.wx.api.live.OrderApi;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.biz.service.WechatLiveLogisticService;
import com.mall4j.cloud.common.exception.LuckException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 用户更新售后单据回调
 */
@Service
@Slf4j
public class AftersaleUpdateOrderEvent implements INotifyEvent, InitializingBean {

    private static final String method = "aftersale_update_order";
    @Autowired
    OrderApi orderApi;
    @Autowired
    WxConfig wxConfig;
    @Autowired
    private WechatLiveLogisticService wechatLiveLogisticService;

    /**
     *
     * <xml>
     *      <ToUserName>gh_abcdefg</ToUserName>
     *      <FromUserName>oABCD</FromUserName>
     *      <CreateTime>12344555555</CreateTime>
     *      <MsgType>event</MsgType>
     *      <Event>aftersale_update_order</Event>
     *      <aftersale_info>
     *           <aftersale_id>123456</aftersale_id>
     *           <out_aftersale_id>1234567</order_id>
     *      </aftersale_info>
     * </xml>
     *
     * @param postData
     * @return
     */
    @Override
    public String doEvent(String postData) throws Exception {
        log.info("用户更新售后单据回调，输入参数：{}", postData);
        Map<String, String> resultMap = WechatUtils.xmlToMap(postData);


        Long aftersaleId = MapUtil.getLong(resultMap, "aftersale_id");
        String outAftersaleId = MapUtil.getStr(resultMap, "out_aftersale_id");

        // 根据售后单号查询售后数据
        AfterSaleInfo afterSaleInfo = new AfterSaleInfo();
        afterSaleInfo.setAftersaleId(aftersaleId);
        AfterSaleResponse afterSaleResponse = orderApi.afterSale(wxConfig.getWxMaToken(), afterSaleInfo);
        log.info("用户更新售后单据回调，查询售后信息参数:{},返回对象:{}", JSONObject.toJSONString(afterSaleInfo),JSONObject.toJSONString(afterSaleResponse));
        if (afterSaleResponse == null || afterSaleResponse.getErrcode() > 0) {
            throw new LuckException("用户更新售后单据回调，请求售后数据异常！");
        }
        
        AfterSalesOrder afterSalesOrder = afterSaleResponse.getAfterSalesOrder();

        return "success";
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.register(method, this);
    }

}
