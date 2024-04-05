package com.mall4j.cloud.biz.wx.wx.channels;

import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitClient;
import com.mall4j.cloud.api.biz.dto.channels.request.*;
import com.mall4j.cloud.api.biz.dto.channels.response.*;
import retrofit2.http.*;

@RetrofitClient(baseUrl = "https://api.weixin.qq.com")
public interface EcCategoryApi {

    @GET("/channels/ec/category/all")
    EcAllCategoryResponse allCategory(@Query("access_token") String authorizerAccessToken);

    @POST("/channels/ec/category/detail")
    EcCategoryDetailResponse detail(@Query("access_token") String authorizerAccessToken, @Body EcCategoryDetailRequest request);

    /**
     * 获取可用的子类目详情
     * 多次调用该接口分别拿到一，二，三级类目，如果某个二级类目下的三级类目是空，则说明这个二级类目下的所有三级类目都需要申请定向准入资质。
     *
     * https://developers.weixin.qq.com/doc/channels/API/category/getavailablesoncategories.html
     *
     * @param request
     * @return
     */
    @POST("/channels/ec/category/availablesoncategories/get")
    EcCategoryDetailResponse getAvailablesoncategories(@Query("access_token") String authorizerAccessToken, @Body EcGetAvailablesoncategoriesRequest request);

    /**
     * 上传类目资质
     *
     * https://developers.weixin.qq.com/doc/channels/API/category/add.html
     *
     * @param request
     * @return
     */
    @POST("/channels/ec/category/add")
    EcAddCategoryResponse add(@Query("access_token") String authorizerAccessToken, @Body EcAddCategoryRequest request);

    /**
     * 获取审核结果
     *
     * https://developers.weixin.qq.com/doc/channels/API/category/audit_get.html
     *
     * @param request
     * @return
     */
    @POST("/channels/ec/category/audit/get")
    EcGetCategoryAuditResultResponse getAuditResult(@Query("access_token") String authorizerAccessToken, @Body EcCategoryAuditCancelRequest request);

    /**
     * 取消类目提审
     *
     * https://developers.weixin.qq.com/doc/channels/API/category/cancelauditcategory.html
     *
     * @param request
     * @return
     */
    @POST("/channels/ec/category/audit/cancel")
    EcBaseResponse cancel(@Query("access_token") String authorizerAccessToken, @Body EcCategoryAuditCancelRequest request);

    /**
     * 获取账号申请通过的类目和资质信息
     * 调用该接口可以获取该用户申请的类目和资质，由于类目的资质要求可能存在运营变化，
     * 所以某个类目有某个资质，只是代表申请时的情况，为类目添加商品时以最新资质要求为准。
     *
     * https://developers.weixin.qq.com/doc/channels/API/category/getavailablebizcat.html
     *
     * @param request
     * @return
     */
    @POST("/channels/ec/category/list/get")
    EcGetCategoryListResponse getCategoryList(@Query("access_token") String authorizerAccessToken, @Body EcCategoryAuditCancelRequest request);

}
