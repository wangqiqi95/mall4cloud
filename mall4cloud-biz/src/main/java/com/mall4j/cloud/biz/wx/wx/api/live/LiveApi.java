package com.mall4j.cloud.biz.wx.wx.api.live;

import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitClient;
import com.mall4j.cloud.biz.dto.live.LiveMediaResponse;
import okhttp3.MultipartBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


/**
 * 直播接口集合
 *
 * @author liutuo
 */
@RetrofitClient(baseUrl = "https://api.weixin.qq.com")
public interface LiveApi {
    /**
     * 新增临时素材
     * @link https://developers.weixin.qq.com/doc/offiaccount/Asset_Management/New_temporary_materials.html
     */
    @Multipart
    @POST("/cgi-bin/media/upload")
    LiveMediaResponse upload(
            @Query("access_token") String authorizerAccessToken,
            @Query("type") String type,
            @Part MultipartBody.Part file
    );



}
