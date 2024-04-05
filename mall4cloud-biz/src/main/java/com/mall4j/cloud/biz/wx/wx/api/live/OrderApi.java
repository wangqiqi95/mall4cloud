package com.mall4j.cloud.biz.wx.wx.api.live;

import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitClient;
import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import com.mall4j.cloud.api.biz.dto.livestore.request.*;
import com.mall4j.cloud.api.biz.dto.livestore.response.*;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;


/**
 * 订单接口集合
 *
 * @author liutuo
 */
@RetrofitClient(baseUrl = "https://api.weixin.qq.com")
public interface OrderApi {
    /**
     * https://developers.weixin.qq.com/miniprogram/dev/platform-capabilities/business-capabilities/ministore/minishopopencomponent2/API/order/getpaymentparams.html
     * 生成支付参数
     * @param authorizerAccessToken
     * @return
     */
    @POST("/shop/order/getpaymentparams")
    PaymentParamsResponse getpaymentparams(@Query("access_token") String authorizerAccessToken,@Body PaymentparamsRequest paymentparamsRequest);



    /**
     * 创建订单
     * https://developers.weixin.qq.com/miniprogram/dev/platform-capabilities/business-capabilities/ministore/minishopopencomponent2/API/order/add_order_new.html
     */
    @POST("/shop/order/add")
    OrderAddResponse orderAdd(@Query("access_token") String authorizerAccessToken, @Body OrderAddRequest orderAddRequest);

    /**
     * 获取订单详情
     * https://developers.weixin.qq.com/miniprogram/dev/platform-capabilities/business-capabilities/ministore/minishopopencomponent2/API/order/get_order.html
     */
    @POST("/shop/order/get")
    GetOrderResponse orderGet(@Query("access_token") String authorizerAccessToken, @Body GetOrderRequest getOrderRequest);

    /**
     * 关闭订单
     * https://developers.weixin.qq.com/miniprogram/dev/platform-capabilities/business-capabilities/ministore/minishopopencomponent2/API/order/order_close.html
     */
    @POST("/shop/order/close")
    BaseResponse orderClose(@Query("access_token") String authorizerAccessToken, @Body OrderCloseRequest orderCloseRequest);


    /**
     * 同步订单支付结果
     * @link https://developers.weixin.qq.com/miniprogram/dev/platform-capabilities/business-capabilities/ministore/minishopopencomponent2/API/order/pay_order.html
     */
    @POST("/shop/order/pay")
    BaseResponse orderPay(@Query("access_token") String authorizerAccessToken, @Body OrderPayRequest orderPayRequest);


    /**
     * 用户上传退货物流
     * @link https://developers.weixin.qq.com/miniprogram/dev/platform-capabilities/business-capabilities/ministore/minishopopencomponent2/API/aftersale/cancel.html
     */
    @POST("/shop/aftersale/uploadreturninfo")
    BaseResponse uploadreturninfo(@Query("access_token") String authorizerAccessToken, @Body UploadreturninfoRequest uploadreturninfoRequest);

    /**
     * 订单发货
     */
    @POST("/shop/delivery/send")
    BaseResponse deliverySend(@Query("access_token") String authorizerAccessToken, @Body DeliverySendRequest deliverySendRequest);

    /**
     * 订单确认收货
     */
    @POST("/shop/delivery/recieve")
    BaseResponse deliveryRecieve(@Query("access_token") String authorizerAccessToken, @Body DeliveryRecieveResquest deliveryRecieveResquest);


    /**
     * 创建售后
     * @link https://developers.weixin.qq.com/miniprogram/dev/platform-capabilities/business-capabilities/ministore/minishopopencomponent2/API/aftersale/add.html
     */
    @POST("/shop/ecaftersale/add")
    EcaftersaleAddResponse addAftersale(@Query("access_token") String authorizerAccessToken, @Body EcaftersaleAddRequest ecaftersaleAddRequest);

    /**
     * 更新售后
     * @link https://developers.weixin.qq.com/miniprogram/dev/platform-capabilities/business-capabilities/ministore/minishopopencomponent2/API/aftersale/update.html
     */
    @POST("/shop/ecaftersale/update")
    BaseResponse updateAftersale(@Query("access_token") String authorizerAccessToken, @Body AftersaleRequest aftersaleRequest);

    /**
     * 用户取消售后申请
     * @link https://developers.weixin.qq.com/miniprogram/dev/platform-capabilities/business-capabilities/ministore/minishopopencomponent2/API/aftersale/cancel.html
     */
    @POST("/shop/ecaftersale/cancel")
    BaseResponse cancelAftersale(@Query("access_token") String authorizerAccessToken, @Body CancelAftersaleRequest cancelAftersaleRequest);

    /**
     * 商家同意退款
     * @link https://developers.weixin.qq.com/miniprogram/dev/platform-capabilities/business-capabilities/ministore/minishopopencomponent2/API/aftersale/acceptrefund.html
     */
    @POST("/shop/ecaftersale/acceptrefund")
    BaseResponse acceptrefund(@Query("access_token") String authorizerAccessToken, @Body AcceptrefundRequest acceptrefundRequest);

    /**
     * 商家同意退货
     * @link https://developers.weixin.qq.com/miniprogram/dev/platform-capabilities/business-capabilities/ministore/minishopopencomponent2/API/aftersale/acceptreturn.html
     */
    @POST("/shop/ecaftersale/acceptreturn")
    BaseResponse acceptreturn(@Query("access_token") String authorizerAccessToken, @Body AcceptreturnRequest acceptreturnRequest);

    /**
     * 商家拒绝售后
     * @link https://developers.weixin.qq.com/miniprogram/dev/platform-capabilities/business-capabilities/ministore/minishopopencomponent2/API/aftersale/reject.html
     */
    @POST("/shop/ecaftersale/reject")
    BaseResponse reject(@Query("access_token") String authorizerAccessToken, @Body EcaftersaleRejectRequest ecaftersaleRejectRequest);

    @POST("/shop/ecaftersale/get")
    AfterSaleResponse afterSale(@Query("access_token") String authorizerAccessToken, @Body AfterSaleInfo afterSaleInfo);

    @POST("/shop/ecaftersale/uploadreturninfo")
    BaseResponse aftersaleUploadLogistics(@Query("access_token") String wxMaToken,@Body  AftersaleUploadDliveryRequest aftersaleUploadDliveryRequest);

    /**
     * 商家上传退款凭证
     * @param wxMaToken
     * @param uploadCertificatesRequest
     * @return
     */
    @POST("/shop/ecaftersale/upload_certificates")
    BaseResponse uploadCertificatesRequest(@Query("access_token") String wxMaToken,@Body UploadCertificatesRequest uploadCertificatesRequest);



}
