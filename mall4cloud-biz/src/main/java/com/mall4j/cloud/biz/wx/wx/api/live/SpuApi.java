package com.mall4j.cloud.biz.wx.wx.api.live;

import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitClient;
import com.mall4j.cloud.api.biz.dto.livestore.request.*;
import com.mall4j.cloud.api.biz.dto.livestore.response.*;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;


/**
 * 添加商品接口集合
 *
 * @author liutuo
 */
@RetrofitClient(baseUrl = "https://api.weixin.qq.com")
public interface SpuApi {
    /**
     * 上传图片
     */
    @POST("/shop/img/upload")
    MediaResponse imgUpload(@Query("access_token") String authorizerAccessToken, @Query("resp_type") Integer respType, @Query("upload_type") Integer uploadType, @Query("img_url") String imgUrl);

    /**
     * 全量获取商品类目
     * @link https://developers.weixin.qq.com/miniprogram/dev/platform-capabilities/business-capabilities/ministore/minishopopencomponent2/API/cat/get_children_cateogry.html
     */
    @POST("/shop/cat/get")
    CategoryResponse getAllCategory(@Query("access_token") String authorizerAccessToken, @Body String body);

    /**
     * 品牌审核
     * @link https://developers.weixin.qq.com/miniprogram/dev/platform-capabilities/business-capabilities/ministore/minishopopencomponent2/API/audit/audit_brand.html
     */
    @POST("/shop/audit/audit_brand")
    AuditResponse auditBrand(@Query("access_token") String authorizerAccessToken, @Body BrandRequest brandRequest);

    /**
     * 类目审核
     * @link https://developers.weixin.qq.com/miniprogram/dev/platform-capabilities/business-capabilities/ministore/minishopopencomponent2/API/audit/audit_category.html
     */
    @POST("/shop/audit/audit_category")
    AuditResponse auditCategory(@Query("access_token") String authorizerAccessToken, @Body CategoryRequest categoryRequest);

    /**
     * 添加商品
     * @link https://developers.weixin.qq.com/miniprogram/dev/platform-capabilities/business-capabilities/ministore/minishopopencomponent2/API/SPU/add_spu.html
     */
    @POST("/shop/spu/add")
    SpuAuditResponse addSpu(@Query("access_token") String authorizerAccessToken, @Body SpuRequest spuRequest);

    /**
     * 商品更新
     */
    @POST("/shop/spu/update")
    SpuAuditResponse updateSpu(@Query("access_token") String authorizerAccessToken, @Body SpuRequest spuRequest);

    /**
     * 获取商品列表
     */
    @POST("/shop/spu/get_list")
    SpuListResponse spuList(@Query("access_token") String authorizerAccessToken, @Body SpuListRequest spuListRequest);

    /**
     * 获取商品信息
     */
    @POST("/shop/spu/get")
    SpuResponse spu(@Query("access_token") String authorizerAccessToken, @Body SpuQueryRequest spuQueryRequest);

    /**
     * 商品上架
     */
    @POST("/shop/spu/listing")
    SpuAuditResponse onsale(@Query("access_token") String authorizerAccessToken, @Body SpuRequest spuRequest);

    /**
     * 商品下架
     */
    @POST("/shop/spu/delisting")
    SpuAuditResponse offsale(@Query("access_token") String authorizerAccessToken, @Body SpuRequest spuRequest);

    /**
     * 删除商品
     */
    @POST("/shop/spu/del")
    SpuAuditResponse delSpu(@Query("access_token") String authorizerAccessToken, @Body SpuRequest spuRequest);


    /**
     * 物流公司列表
     */
    @POST("/shop/delivery/get_company_list")
    LogisticCompanyResponse logsticsList(@Query("access_token") String authorizerAccessToken, @Body String body);


}
