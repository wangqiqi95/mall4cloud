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
public interface SellerApi {
    /**
     *
     * 获取商户信息
     * https://developers.weixin.qq.com/miniprogram/dev/platform-capabilities/business-capabilities/ministore/minishopopencomponent2/API/account/get_info.html
     * @param authorizerAccessToken
     * @return
     */
    @POST("/shop/account/get_info")
    SellerInfoResponse getSellerInfo(@Query("access_token") String authorizerAccessToken,@Body String body);


    /**
     * 更新商户信息
     * https://developers.weixin.qq.com/miniprogram/dev/platform-capabilities/business-capabilities/ministore/minishopopencomponent2/API/account/update_info.html
     */
    @POST("/shop/account/update_info")
    BaseResponse updateSellerInfo(@Query("access_token") String authorizerAccessToken, @Body SellerInfo sellerInfo);


    /**
     * 获取已申请成功的类类目列表
     * https://developers.weixin.qq.com/miniprogram/dev/platform-capabilities/business-capabilities/ministore/minishopopencomponent2/API/account/category_list.html
     */
    @POST("/shop/account/get_category_list")
    ShopAccountCategoryResponse getCategoryList(@Query("access_token") String authorizerAccessToken,@Body String body);

}
