package com.mall4j.cloud.api.docking.skq_sqb.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "查询会员码信息VO", description = "从微盟云查询会员码信息的VO")
public class LitePosRefundVO {

    @ApiModelProperty(value = "动态码来源，用于区分本地或渠道。支持的类型包括：1-微盟本地卡动态码；2-微信卡动态码。")
    private Long codeSource;

    @ApiModelProperty(value = "用户 ID，是微盟用户身份的唯一标识")
    private Long wid;

    @ApiModelProperty(value = "动态码: 本地码：; 微信码：")
    private Long dynamicCode;

}
