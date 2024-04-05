package com.mall4j.cloud.biz.wx.wx.api.live;


import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitClient;
import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import com.mall4j.cloud.api.biz.dto.livestore.request.conpon.AddCouponRequest;
import com.mall4j.cloud.api.biz.dto.livestore.request.conpon.GetCouponListRequest;
import com.mall4j.cloud.api.biz.dto.livestore.request.conpon.GetCouponRequest;
import com.mall4j.cloud.api.biz.dto.livestore.response.coupon.GetCouponResponse;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 优惠券接口
 *
 */
@RetrofitClient(baseUrl = "https://api.weixin.qq.com")
public interface CouponApi {
    /**
     * https://developers.weixin.qq.com/miniprogram/dev/platform-capabilities/business-capabilities/ministore/minishopopencomponent2/API/coupon/add_coupon.html
     * 添加优惠券
     * @param authorizerAccessToken
     * @return
     */
    @POST("/shop/coupon/add")
    BaseResponse add(@Query("access_token") String authorizerAccessToken, @Body AddCouponRequest addCouponRequest);


    /**
     * https://developers.weixin.qq.com/miniprogram/dev/platform-capabilities/business-capabilities/ministore/minishopopencomponent2/API/coupon/get_coupon.html
     * 获取优惠券信息
     * @param authorizerAccessToken
     * @return
     */
    @POST("/shop/coupon/get")
    GetCouponResponse get(@Query("access_token") String authorizerAccessToken, @Body GetCouponRequest getCouponRequest);

    /**
     * https://developers.weixin.qq.com/miniprogram/dev/platform-capabilities/business-capabilities/ministore/minishopopencomponent2/API/coupon/get_coupon_list.html
     * 获取优惠券列表
     * @param authorizerAccessToken
     * @return
     */
    @POST("/shop/coupon/get_list")
    GetCouponResponse get_list(@Query("access_token") String authorizerAccessToken, @Body GetCouponListRequest getCouponListRequest);
}
