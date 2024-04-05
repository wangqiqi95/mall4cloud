package com.mall4j.cloud.distribution.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class DistributionCommissionAccountAuthDTO {

    @NotBlank(message = "name不能为空")
    @ApiModelProperty("姓名")
    private String name;

    @NotBlank(message = "certNo不能为空")
    @ApiModelProperty("证件号码")
    private String certNo;

    @ApiModelProperty("省")
    private String province;

    @ApiModelProperty("市")
    private String city;

    @ApiModelProperty("区")
    private String district;

    @NotBlank(message = "address不能为空")
    @ApiModelProperty("详细地址")
    private String address;

    @NotBlank(message = "idPhotoFront不能为空")
    @ApiModelProperty("身份证正面照")
    private String idPhotoFront;

    @NotBlank(message = "idPhotoBack不能为空")
    @ApiModelProperty("身份证背面照")
    private String idPhotoBack;

    @ApiModelProperty("电话号码")
    private String telephone;

    private Long userId;
    private Integer identityType;


}
