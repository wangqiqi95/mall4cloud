package com.mall4j.cloud.biz.wx.wx.channels;

import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitClient;
import com.mall4j.cloud.api.biz.dto.channels.request.*;
import com.mall4j.cloud.api.biz.dto.channels.response.*;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 视频号4。0 售后单接口集合
 *
 */
@RetrofitClient(baseUrl = "https://api.weixin.qq.com")
public interface EcAftersaleApi {

    /**
     * 获取售后单
     * https://developers.weixin.qq.com/doc/channels/API/aftersale/getaftersaleorder.html
     */
    @POST("/channels/ec/aftersale/getaftersaleorder")
    EcGetaftersaleorderResponse getaftersaleorder(@Query("access_token") String authorizerAccessToken, @Body EcGetaftersaleorderRequest ecGetaftersaleorderRequest);


    /**
     * 获取售后单列表
     * https://developers.weixin.qq.com/doc/channels/API/aftersale/getaftersalelist.html
     */
    @POST("/channels/ec/aftersale/getaftersalelist")
    EcGetaftersaleListResponse getaftersalelist(@Query("access_token") String authorizerAccessToken, @Body EcGetaftersaleListRequest ecGetaftersaleListRequest);

    /**
     * 同意售后
     * https://developers.weixin.qq.com/doc/channels/API/aftersale/acceptapply.html
     */
    @POST("/channels/ec/aftersale/acceptapply")
    EcBaseResponse acceptapply(@Query("access_token") String authorizerAccessToken, @Body EcAftersaleAcceptapplyRequest ecAftersaleAcceptapplyRequest);

    /**
     * 拒绝售后
     * https://developers.weixin.qq.com/doc/channels/API/aftersale/rejectapply.html
     */
    @POST("/channels/ec/aftersale/rejectapply")
    EcBaseResponse rejectapply(@Query("access_token") String authorizerAccessToken, @Body EcAftersaleRejectapplyRequest ecAftersaleRejectapplyRequest);

    /**
     * 上传退款凭证
     * https://developers.weixin.qq.com/doc/channels/API/aftersale/uploadrefundcertificate.html
     */
    @POST("/channels/ec/aftersale/uploadrefundcertificate")
    EcBaseResponse uploadrefundcertificate(@Query("access_token") String authorizerAccessToken, @Body EcuploadrefundcertificateRequest ecuploadrefundcertificateRequest);


    /**
     * 纠纷单 商家补充纠纷单留言
     * https://developers.weixin.qq.com/doc/channels/API/complaint/addcomplaintmaterial.html
     */
    @POST("/channels/ec/aftersale/addcomplaintmaterial")
    EcBaseResponse addcomplaintmaterial(@Query("access_token") String authorizerAccessToken, @Body EcAddcomplaintmaterialReqeust reqeust);

    /**
     * 纠纷单 商家举证
     * https://developers.weixin.qq.com/doc/channels/API/complaint/addcomplaintproof.html
     */
    @POST("/channels/ec/aftersale/addcomplaintproof")
    EcBaseResponse addcomplaintproof(@Query("access_token") String authorizerAccessToken, @Body EcAddcomplaintproofRequest request);


    /**
     * 纠纷单 详情
     * https://developers.weixin.qq.com/doc/channels/API/complaint/addcomplaintproof.html
     */
    @POST("/channels/ec/aftersale/getcomplaintorder")
    EcGetcomplaintorderResponse getcomplaintorder(@Query("access_token") String authorizerAccessToken, @Body EcGetcomplaintorderReqeust reqeust);




}
