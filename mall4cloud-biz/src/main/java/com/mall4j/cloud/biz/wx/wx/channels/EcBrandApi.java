package com.mall4j.cloud.biz.wx.wx.channels;

import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitClient;
import com.mall4j.cloud.api.biz.dto.channels.request.EcBrandStoreRequest;
import com.mall4j.cloud.api.biz.dto.channels.request.EcBrandCancelRequest;
import com.mall4j.cloud.api.biz.dto.channels.request.EcPageRequest;
import com.mall4j.cloud.api.biz.dto.channels.request.EcBrandListRequest;
import com.mall4j.cloud.api.biz.dto.channels.request.EcBrandOneRequest;
import com.mall4j.cloud.api.biz.dto.channels.request.EcBrandRequest;
import com.mall4j.cloud.api.biz.dto.channels.response.EcBaseResponse;
import com.mall4j.cloud.api.biz.dto.channels.response.EcBrandListResponse;
import com.mall4j.cloud.api.biz.dto.channels.response.EcBrandOneResponse;
import com.mall4j.cloud.api.biz.dto.channels.response.EcBrandStoreResponse;
import com.mall4j.cloud.api.biz.dto.channels.response.EcAllBrandResponse;
import com.mall4j.cloud.api.biz.dto.livestore.response.AuditResponse;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 视频号4.0品牌接口集合
 *
 */

@RetrofitClient(baseUrl = "https://api.weixin.qq.com")
public interface EcBrandApi {

    @POST("/channels/ec/brand/all")
    EcAllBrandResponse allBrand(EcPageRequest ecPageRequest);
    
    
    /**
     * 获取品牌库列表
     * @link https://developers.weixin.qq.com/doc/channels/API/brand/all_get.html
     */
    @POST("/channels/ec/brand/all")
    EcBrandStoreResponse brandAll(@Query("access_token") String authorizerAccessToken, @Body EcBrandStoreRequest ecBrandStoreRequest);
    
    /**
     * 添加品牌资质
     * @link https://developers.weixin.qq.com/doc/channels/API/brand/add.html
     */
    @POST("/channels/ec/brand/add")
    AuditResponse addBrand(@Query("access_token") String authorizerAccessToken, @Body EcBrandRequest ecBrandRequest);
    
    /**
     * 更新品牌资质
     * @link https://developers.weixin.qq.com/doc/channels/API/brand/update.html
     */
    @POST("/channels/ec/brand/update")
    AuditResponse updateBrand(@Query("access_token") String authorizerAccessToken, @Body EcBrandRequest ecBrandRequest);
    
    /**
     * 撤回品牌资质审核
     * @link https://developers.weixin.qq.com/doc/channels/API/brand/audit_cancel.html
     */
    @POST("/channels/ec/brand/audit/cancel")
    EcBaseResponse cancelBrand(@Query("access_token") String authorizerAccessToken, @Body EcBrandCancelRequest ecBrandCancelRequest);
    
    /**
     * 获取品牌资质申请详情
     * @link https://developers.weixin.qq.com/doc/channels/API/brand/get.html
     */
    @POST("/channels/ec/brand/get")
    EcBrandOneResponse getBrand(@Query("access_token") String authorizerAccessToken, @Body EcBrandOneRequest ecBrandOneRequest);
    
    /**
     * 获取品牌资质申请列表
     * @link https://developers.weixin.qq.com/doc/channels/API/brand/list_get.html
     */
    @POST("/channels/ec/brand/list/get")
    EcBrandListResponse getBrandList(@Query("access_token") String authorizerAccessToken, @Body EcBrandListRequest ecBrandListRequest);

}
