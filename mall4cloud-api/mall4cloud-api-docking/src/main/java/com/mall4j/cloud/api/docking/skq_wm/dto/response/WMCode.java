package com.mall4j.cloud.api.docking.skq_wm.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author ty
 * @ClassName WMCode
 * @description
 * @date 2023/5/4 13:07
 */
@Data
public class WMCode {

    /**
     * 会员卡过期时间，格式：时间戳。
     */
    @JsonProperty("bizErrmsg")
    private String bizErrMsg;

    /**
     * 请求返回的状态码。
     */
    @JsonProperty("errcode")
    private String errCode;

    /**
     * 请求返回的信息
     */
    @JsonProperty("errmsg")
    private String errMsg;

}
