package com.mall4j.cloud.biz.wx.wx.channels;

import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitClient;
import com.mall4j.cloud.api.biz.dto.channels.request.*;
import com.mall4j.cloud.api.biz.dto.channels.response.*;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;


/**
 * 视频号4。0接口集合
 *
 */
@RetrofitClient(baseUrl = "https://api.weixin.qq.com")
public interface EcProductApi {

    /**
     * 添加商品
     * https://developers.weixin.qq.com/doc/channels/API/product/add.html
     */
    @POST("/channels/ec/product/add")
    EcAddProductResponse add(@Query("access_token") String authorizerAccessToken, @Body EcAddProductRequest ecAddProductRequest);

    /**
     * 更新商品
     * @link https://developers.weixin.qq.com/doc/channels/API/product/update.html
     */
    @POST("/channels/ec/product/update")
    EcAddProductResponse update(@Query("access_token") String authorizerAccessToken, @Body EcAddProductRequest ecAddProductRequest);


    /**
     * 删除商品
     * https://developers.weixin.qq.com/doc/channels/API/product/delete.html
     */
    @POST("/channels/ec/product/delete")
    EcBaseResponse delete(@Query("access_token") String authorizerAccessToken, @Body EcDeleteProductRequest ecDeleteProductRequest);

    /**
     * 获取商品
     * @link https://developers.weixin.qq.com/doc/channels/API/product/get.html
     */
    @POST("/channels/ec/product/get")
    EcProductResponse get(@Query("access_token") String authorizerAccessToken, @Body EcGetProductRequest ecGetProductRequest);

    /**
     * 获取商品列表
     * @link https://developers.weixin.qq.com/doc/channels/API/product/list_get.html
     */
    @POST("/channels/ec/product/list/get")
    EcProductListResponse list(@Query("access_token") String authorizerAccessToken, @Body EcProductListRequest ecProductListRequest);

    /**
     * 上架商品
     * @link https://developers.weixin.qq.com/doc/channels/API/product/listing.html
     */
    @POST("/channels/ec/product/listing")
    EcBaseResponse listing(@Query("access_token") String authorizerAccessToken, @Body EcProductListingRequest ecProductListingRequest);


    /**
     * 下架商品
     * @link https://developers.weixin.qq.com/doc/channels/API/product/delisting.html
     */
    @POST("/channels/ec/product/delisting")
    EcBaseResponse delisting(@Query("access_token") String authorizerAccessToken, @Body EcProductDelistingRequest ecProductDelistingRequest);

    /**
     * 撤回商品审核
     * @link https://developers.weixin.qq.com/doc/channels/API/product/audit_cancel.html
     */
    @POST("/channels/ec/product/audit/cancel")
    EcBaseResponse auditCancel(@Query("access_token") String authorizerAccessToken, @Body EcProductAuditCancelRequest ecProductAuditCancelRequest);

    /**
     * 获取实时库存
     * @link https://developers.weixin.qq.com/doc/channels/API/product/get_stock.html
     */
    @POST("/channels/ec/product/stock/get")
    EcGetStockResponse getStock(@Query("access_token") String authorizerAccessToken, @Body EcGetStockRequest ecGetStockRequest);

    /**
     * 快速更新库存
     * @link https://developers.weixin.qq.com/doc/channels/API/product/stock_update.html
     */
    @POST("/channels/ec/product/stock/update")
    EcUpdateStockResponse updateStock(@Query("access_token") String authorizerAccessToken, @Body EcUpdateStockRequest ecUpdateStockRequest);



}
