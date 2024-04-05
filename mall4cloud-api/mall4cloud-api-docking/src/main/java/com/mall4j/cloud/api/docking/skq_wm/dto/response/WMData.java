package com.mall4j.cloud.api.docking.skq_wm.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author ty
 * @ClassName WMData
 * @description
 * @date 2023/5/4 13:07
 */
@Data
public class WMData {

    /**
     * 客户编号，是用户身份的唯一标识。
     */
    @JsonProperty("wid")
    private Long wid;

    /**
     * 会员方案 ID
     */
    @JsonProperty("membershipPlanId")
    private Long membershipPlanId;

    /**
     * 客户手机号
     */
    @JsonProperty("phone")
    private Long phone;

    /**
     * 动态码过期时间，格式：时间戳。
     */
    @JsonProperty("dynamicExpireAtTime")
    private Date dynamicExpireAtTime;

    /**
     * 会员卡过期时间，格式：时间戳。
     */
    @JsonProperty("expireTime")
    private Date expireTime;

    /**
     * 微信码
     */
    @JsonProperty("wxCode")
    private String wxCode;

    /**
     * 自定义卡号
     */
    @JsonProperty("customCardNo")
    private String customCardNo;

}
