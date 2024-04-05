package com.mall4j.cloud.biz.wx.wx.api.live;


import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitClient;
import com.mall4j.cloud.api.biz.dto.livestore.request.EmptyRequst;
import com.mall4j.cloud.api.biz.dto.livestore.response.PaymentParamsResponse;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

@RetrofitClient(baseUrl = "https://api.weixin.qq.com")
public interface AccountApi {
    /**
     * https://developers.weixin.qq.com/miniprogram/dev/platform-capabilities/business-capabilities/ministore/minishopopencomponent2/API/order/getpaymentparams.html
     * 生成支付参数
     * @param authorizerAccessToken
     * @return
     */
    @POST("/shop/account/get_info")
    PaymentParamsResponse getinfo(@Query("access_token") String authorizerAccessToken, @Body EmptyRequst emptyRequst);
}
