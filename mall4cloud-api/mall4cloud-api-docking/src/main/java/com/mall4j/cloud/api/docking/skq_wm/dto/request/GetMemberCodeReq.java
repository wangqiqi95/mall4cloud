package com.mall4j.cloud.api.docking.skq_wm.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description 查询会员码信息入参
 * @author Peter_Tan
 * @date
 **/
@Data
public class GetMemberCodeReq {

    /**
     * 动态码来源，用于区分本地或渠道。支持的类型包括：1-微盟本地卡动态码；2-微信卡动态码
     */
    private Long codeSource;

    /**
     * 用户 ID，是微盟用户身份的唯一标识
     */
    private Long wid;

    /**
     * 动态码: 本地码：; 微信码：
     */
    private String dynamicCode;
}
