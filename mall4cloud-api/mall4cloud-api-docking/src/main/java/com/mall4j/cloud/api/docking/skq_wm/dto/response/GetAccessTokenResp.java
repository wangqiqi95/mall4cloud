package com.mall4j.cloud.api.docking.skq_wm.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Description 获取授权令牌出参
 * @author Peter_Tan
 * @date 2023-04-26 11:30
 **/
@Data
public class GetAccessTokenResp{

    /**
     * access_token 的值，是微盟 OpenAPI 的调用凭证，有效期为 2 小时
     */
    @JsonProperty("access_token")
    private String accessToken;

    /**
     * access_token 类型，目前只支持 bearer
     */
    @JsonProperty("token_type")
    private String tokenType;

    /**
     * access_token 的有效期，单位秒
     */
    @JsonProperty("expires_in")
    private Long expiresIn;

    /**
     * 授权范围，默认为 default
     */
    @JsonProperty("scope")
    private String scope;

    /**
     * 微盟商业操作系统 ID（BOSID）
     */
    @JsonProperty("business_operation_system_id")
    private String businessOperationSystemId;

    /**
     * 微盟商家店铺 ID（PID）
     */
    @JsonProperty("public_account_id")
    private String publicAccountId;

    /**
     * 微盟商家公众号 ID（B 端 WID）
     */
    @JsonProperty("business_id")
    private String businessId;

}
