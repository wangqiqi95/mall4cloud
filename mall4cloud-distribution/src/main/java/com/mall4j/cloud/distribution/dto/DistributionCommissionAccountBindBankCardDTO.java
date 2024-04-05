package com.mall4j.cloud.distribution.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class DistributionCommissionAccountBindBankCardDTO {

    @NotBlank(message = "cardNo不能为空")
    @ApiModelProperty("持卡人账号")
    private String cardNo;
}
