package com.mall4j.cloud.api.docking.skq_wm.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "查询会员码信息VO", description = "从微盟云查询会员码信息的VO")
public class MemberCodeVO {

    @ApiModelProperty(value = "客户编号，是用户身份的唯一标识。")
    private Long wid;

    @ApiModelProperty(value = "会员方案 ID")
    private Long membershipPlanId;

    @ApiModelProperty(value = "客户手机号")
    private Long phone;

    @ApiModelProperty(value = "动态码过期时间，格式：时间戳。")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date dynamicExpireAtTime;

    @ApiModelProperty(value = "会员卡过期时间，格式：时间戳。")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date expireTime;

    @ApiModelProperty(value = "微信码")
    private String wxCode;

    @ApiModelProperty(value = "自定义卡号")
    private String customCardNo;

}
