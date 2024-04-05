package com.mall4j.cloud.biz.wx.wx.channels;

import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitClient;
import com.mall4j.cloud.api.biz.dto.channels.request.*;
import com.mall4j.cloud.api.biz.dto.channels.response.EcBaseResponse;
import com.mall4j.cloud.api.biz.dto.channels.response.EcOrderListResponse;
import com.mall4j.cloud.api.biz.dto.channels.response.EcOrderResponse;
import com.mall4j.cloud.api.biz.dto.channels.response.EcOrderSearchResponse;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;


/**
 * 视频号4。0 订单接口集合
 *
 */
@RetrofitClient(baseUrl = "https://api.weixin.qq.com")
public interface EcOrderApi {

    /**
     * 获取订单详情
     * https://developers.weixin.qq.com/doc/channels/API/order/get.html
     */
    @POST("/channels/ec/order/get")
    EcOrderResponse get(@Query("access_token") String authorizerAccessToken, @Body EcGetOrderRequest ecGetOrderRequest);

    /**
     * 获取订单列表
     * https://developers.weixin.qq.com/doc/channels/API/order/list_get.html
     */
    @POST("/channels/ec/order/list/get")
    EcOrderListResponse list(@Query("access_token") String authorizerAccessToken, @Body EcGetOrderListRequest ecGetOrderListRequest);

    /**
     * 修改订单价格
     * @link https://developers.weixin.qq.com/doc/channels/API/order/price_update.html
     */
    @POST("/channels/ec/order/price/update")
    EcBaseResponse updatePrice(@Query("access_token") String authorizerAccessToken, @Body EcOrderUpdatePriceRequest ecOrderUpdatePriceRequest);


    /**
     * 修改订单备注
     * @link https://developers.weixin.qq.com/doc/channels/API/order/merchantnotes_update.html
     */
    @POST("/channels/ec/order/merchantnotes/update")
    EcBaseResponse updateMerchantnotes(@Query("access_token") String authorizerAccessToken, @Body EcOrderUpdateMerchantnotesRequest ecOrderUpdateMerchantnotesRequest);

    /**
     * 修改订单收货地址
     */
    @POST("/channels/ec/order/address/update")
    EcBaseResponse updateAddress(@Query("access_token") String authorizerAccessToken, @Body EcOrderUpdateAddressRequest ecOrderUpdateAddressRequest);

    /**
     * 订单搜索
     */
    @POST("/channels/ec/order/search")
    EcOrderSearchResponse search(@Query("access_token") String authorizerAccessToken, @Body EcSearchOrderRequest ecSearchOrderRequest);

    /**
     * 订单发货
     * https://developers.weixin.qq.com/doc/channels/API/order/delivery_send.html
     */
    @POST("/channels/ec/order/delivery/send")
    EcBaseResponse deliverysend(@Query("access_token") String authorizerAccessToken, @Body EcDeliverySendRequest ecDeliverySendRequest);
}
