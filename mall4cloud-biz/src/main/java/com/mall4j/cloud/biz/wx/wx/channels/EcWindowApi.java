package com.mall4j.cloud.biz.wx.wx.channels;

import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitClient;
import com.mall4j.cloud.api.biz.dto.channels.request.*;
import com.mall4j.cloud.api.biz.dto.channels.response.EcWindowAddProductResponse;
import com.mall4j.cloud.api.biz.dto.channels.response.EcWindowGetProductResponse;
import com.mall4j.cloud.api.biz.dto.channels.response.EcWindowOffProductResponse;
import com.mall4j.cloud.api.biz.dto.channels.response.EcWindowProductListResponse;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 视频号4.0 商品橱窗
 * @date 2023/3/8
 */
@RetrofitClient(baseUrl = "https://api.weixin.qq.com")
public interface EcWindowApi {

    /**
     * 上架商品到橱窗
     */
    @POST("/channels/ec/window/product/add")
    EcWindowAddProductResponse addProduct(@Query("access_token") String authorizerAccessToken,
                                          @Body EcWindowAddProductRequest ecWindowAddProductRequest);

    /**
     * 获取橱窗商品详情
     */
    @POST("/channels/ec/window/product/get")
    EcWindowGetProductResponse getProduct(@Query("access_token") String authorizerAccessToken,
                                          @Body EcWindowGetProductRequest ecWindowGetProductRequest);

    /**
     * 获取已添加到橱窗的商品列表
     */
    @POST("/channels/ec/window/product/list/get")
    EcWindowProductListResponse listProduct(@Query("access_token") String authorizerAccessToken,
                                            @Body EcWindowProductListRequest ecWindowProductListRequest);

    /**
     * 下架橱窗商品
     */
    @POST("/channels/ec/window/product/off")
    EcWindowOffProductResponse offProduct(@Query("access_token") String authorizerAccessToken,
                                          @Body EcWindowOffProductRequest ecWindowOffProductRequest);
}
