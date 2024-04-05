package com.mall4j.cloud.biz.wx.wx.api.live;

import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitClient;
import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import com.mall4j.cloud.api.biz.dto.livestore.request.complaint.ComplaintDetailRequest;
import com.mall4j.cloud.api.biz.dto.livestore.request.complaint.ComplaintUploadMaterialRequest;
import com.mall4j.cloud.api.biz.dto.livestore.response.complaint.ComplaintDetailResponse;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

@RetrofitClient(baseUrl = "https://api.weixin.qq.com")
public interface ComplaintApi {

//    @POST("/shop/complaint/get_list")
//    ComplaintListResponse getList(@Query("access_token") String authorizerAccessToken, @Body ComplaintListRequest complaintListRequest);

    @POST("/shop/complaint/get")
    ComplaintDetailResponse get(@Query("access_token") String authorizerAccessToken, @Body ComplaintDetailRequest request);

    @POST("/shop/complaint/upload_material")
    BaseResponse uploadMaterial(@Query("access_token") String authorizerAccessToken, @Body ComplaintUploadMaterialRequest request);

}
