package com.mall4j.cloud.group.dto.questionnaire;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ShippingAddressDTO {
    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("")
    private Long activityId;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("收货人")
    private String consignee;

    @ApiModelProperty("省")
    private String province;

    @ApiModelProperty("城市")
    private String city;

    @ApiModelProperty("区")
    private String area;

    @ApiModelProperty("地址")
    private String addr;

    @ApiModelProperty("手机")
    private String mobile;

}
