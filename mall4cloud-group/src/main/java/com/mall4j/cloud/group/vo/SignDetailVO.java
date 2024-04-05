package com.mall4j.cloud.group.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "签到明细实体")
public class SignDetailVO implements Serializable {
    @ApiModelProperty("签到时间")
    private Date signTime;
    @ApiModelProperty("手机号")
    private String mobile;
    @ApiModelProperty("门店名称")
    private String shopName;
    @ApiModelProperty("门店编码")
    private String shopCode;
    @ApiModelProperty("签到类型 1常规签到 2连续签到")
    private Integer signType;
    @ApiModelProperty("积分数")
    private Integer pointNum;
    @ApiModelProperty("优惠券名称")
    private String couponName;
}
