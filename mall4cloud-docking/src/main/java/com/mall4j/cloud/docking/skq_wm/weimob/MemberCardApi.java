package com.mall4j.cloud.docking.skq_wm.weimob;

import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitClient;
import com.mall4j.cloud.api.docking.skq_wm.dto.request.GetMemberCodeReq;
import com.mall4j.cloud.api.docking.skq_wm.dto.response.GetAccessTokenResp;
import com.mall4j.cloud.api.docking.skq_wm.dto.response.GetMemberCodeResp;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @Description 微盟云会员卡相关接口
 * @author Peter_Tan
 * @date 2023-04-26 11:30
 **/
@RetrofitClient(baseUrl = "https://dopen.weimob.com")
public interface MemberCardApi {

    /**
     * 获取授权令牌 access-token
     * https://dopen.weimob.com/fuwu/b/oauth2/token?grant_type=client_credentials&client_id={client_id}&client_secret={client_secret}&shop_id={shop_id}&shop_type={shop_type}
     * @param grantType OAuth 2.0 的授权类型，自有型应用采用客户端凭证模式，即 client_credentials。
     * @param clientId 产品应用的客户端 ID，是应用发起微盟 OAuth 2.0 授权的凭证
     * @param clientSecret 产品应用的客户端密钥，是应用发起微盟 OAuth 2.0 授权的凭证
     * @param shopId 需要绑定的测试店铺 ID
     * @param shopType 需要绑定的测试店铺类型
     * @return
     */
    @POST("/fuwu/b/oauth2/token")
    GetAccessTokenResp getAccessToken(@Query("grant_type") String grantType, @Query("client_id") String clientId, @Query("client_secret") String clientSecret, @Query("shop_id") String shopId, @Query("shop_type") String shopType);


    /**
     * 查询会员码信息
     * https://dopen.weimob.com/apigw/weimob_crm/v2.0/membercard/membercode/get?accesstoken=ACCESS_TOKEN
     * @param accessToken 授权令牌
     * @param req
     * @return
     */
    @POST("/apigw/weimob_crm/v2.0/membercard/membercode/get")
    GetMemberCodeResp getMemberCode(@Query("accesstoken") String accessToken, @Body GetMemberCodeReq req);

}
