package com.mall4j.cloud.biz.service.channels;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.biz.dto.channels.request.EcDeliverySendRequest;
import com.mall4j.cloud.api.biz.dto.channels.request.EcGetOrderListRequest;
import com.mall4j.cloud.api.biz.dto.channels.request.EcGetOrderRequest;
import com.mall4j.cloud.api.biz.dto.channels.request.EcOrderUpdatePriceRequest;
import com.mall4j.cloud.api.biz.dto.channels.response.EcBaseResponse;
import com.mall4j.cloud.api.biz.dto.channels.response.EcOrderListResponse;
import com.mall4j.cloud.api.biz.dto.channels.response.EcOrderResponse;
import com.mall4j.cloud.biz.wx.wx.channels.EcOrderApi;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.common.exception.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EcOrderService {

    @Autowired
    EcOrderApi ecOrderApi;
    @Autowired
    WxConfig wxConfig;

    /**
     * 查询订单详情
     * @param orderId
     * @return
     */
    public EcOrderResponse get(String orderId){
        log.info("EcOrderService查询订单详情,参数：{}",orderId);
        EcGetOrderRequest ecGetOrderRequest = new EcGetOrderRequest();
        ecGetOrderRequest.setOrder_id(orderId);
        EcOrderResponse ecOrderResponse = ecOrderApi.get(wxConfig.getWxEcToken(),ecGetOrderRequest);
        log.info("EcOrderService查询订单详情,参数：{},查询结果：{}",orderId, JSONObject.toJSONString(ecOrderResponse));
        return ecOrderResponse;
    }

    /**
     * 查询订单列表
     * @param ecGetOrderListRequest
     * @return
     */
    public EcOrderListResponse list(EcGetOrderListRequest ecGetOrderListRequest){
        log.info("EcOrderService查询订单列表,参数：{}",JSONObject.toJSONString(ecGetOrderListRequest));
        EcOrderListResponse ecOrderListResponse = ecOrderApi.list(wxConfig.getWxEcToken(),ecGetOrderListRequest);
        log.info("EcOrderService查询订单详情,参数：{},查询结果：{}",JSONObject.toJSONString(ecGetOrderListRequest), JSONObject.toJSONString(ecOrderListResponse));
        return ecOrderListResponse;
    }

    /**
     * 修改订单价格
     * @param ecOrderUpdatePriceRequest
     * @return
     */
    public EcBaseResponse updatePrice(EcOrderUpdatePriceRequest ecOrderUpdatePriceRequest){
        log.info("EcOrderService修改订单价格,参数：{}",JSONObject.toJSONString(ecOrderUpdatePriceRequest));
        EcBaseResponse ecBaseResponse = ecOrderApi.updatePrice(wxConfig.getWxEcToken(),ecOrderUpdatePriceRequest);
        log.info("EcOrderService修改订单价格,参数：{},查询结果：{}",JSONObject.toJSONString(ecOrderUpdatePriceRequest), JSONObject.toJSONString(ecBaseResponse));
        return ecBaseResponse;
    }

    /**
     * 订单发货
     * @return
     */
    public EcBaseResponse deliverysend(EcDeliverySendRequest request){
        EcBaseResponse response = ecOrderApi.deliverysend(wxConfig.getWxEcToken(), request);
        log.info("EcOrderService订单发货,参数：{},查询结果：{}",JSONObject.toJSONString(request), JSONObject.toJSONString(response));
        if(response==null || response.getErrcode()!=0){
            Assert.faild(StrUtil.format("订单发货失败，错误码:{},失败原因:{}",response.getErrcode(),response.getErrmsg()));
        }
        return response;
    }

}
