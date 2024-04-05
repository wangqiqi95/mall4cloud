package com.mall4j.cloud.biz.wx.wx.channels;

import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitClient;
import com.mall4j.cloud.api.biz.dto.channels.request.EcAddresscodeRequest;
import com.mall4j.cloud.api.biz.dto.channels.request.EcUploadImgRequest;
import com.mall4j.cloud.api.biz.dto.channels.response.*;
import okhttp3.MultipartBody;
import retrofit2.http.*;

@RetrofitClient(baseUrl = "https://api.weixin.qq.com")
public interface EcBasicsApi {

    /**
     * https://developers.weixin.qq.com/doc/channels/API/basics/img_upload.html
     * 注意
     * resp_type=1时将会返回形如mmecimage.cn/p/{appid}/{imgKey}的图片链接，该链接永久有效，同一张图片，无需重复调用该接口重复上传。
     * 接口返回的图片链接有访问频率限制，超出访问频率后，会返回404状态码，请勿将该图片链接用于用户端（访问量较大）展示。
     * 当前访问频率限制规则：
     * appid维度：10000/min。
     * imgKey维度：100/min。
     * 接口返回的图片链接，支持进行图片处理（缩放/裁剪/压缩/水印等），具体使用方法参考文档：https://cloud.tencent.com/document/product/460/53505 ，该文档内的download_url即为当前接口返回的图片链接
     *
     * @param access_token
     * @param upload_type 上传类型。0:二进制流；1:图片url
     * @param resp_type 返回数据类型。0:media_id和pay_media_id；1:图片链接（商品信息相关图片请务必使用此参数得到链接）
     * @param height upload_type=0时必填，图片的高，单位：像素
     * @param width upload_type=0时必填，图片的宽，单位：像素
     * @param file
     * @return
     */
    @Multipart
    @POST("/channels/ec/basics/img/upload")
    EcUploadResponse upload(
            @Query("access_token") String access_token,
            @Query("upload_type") Integer upload_type,
            @Query("resp_type") Integer resp_type,
            @Query("height") Integer height,
            @Query("width") Integer width,
            @Part MultipartBody.Part file
    );

    @POST("/channels/ec/basics/img/upload")
    EcUploadResponse upload(
            @Query("access_token") String access_token,
            @Query("upload_type") Integer upload_type,
            @Query("resp_type") Integer resp_type,
            @Body EcUploadImgRequest ecUploadImgRequest
    );

    /**
     * 获取地址编码
     * https://developers.weixin.qq.com/doc/channels/API/basics/getaddresscode.html
     */
    @POST("/channels/ec/basics/addresscode/get")
    EcAddresscodeResponse getAddresscode(@Query("access_token") String authorizerAccessToken, @Body EcAddresscodeRequest ecAddresscodeRequest);

    /**
     * 获取店铺基础信息
     * https://developers.weixin.qq.com/doc/channels/API/basics/getbasicinfo.html
     */
    @GET("/channels/ec/basics/info/get")
    EcGetStoreInfoResponse getBasicsInfo(@Query("access_token") String authorizerAccessToken);


    /**
     * 上传资质图片
     * https://developers.weixin.qq.com/doc/channels/API/basics/qualificationupload.html
     *
     * @param access_token
     * @param file
     * @return
     */
    @Multipart
    @POST("/channels/ec/basics/qualification/upload")
    EcQualificationUploadResponse qualificationUpload(
            @Query("access_token") String access_token,
            @Part MultipartBody.Part file
    );

}
