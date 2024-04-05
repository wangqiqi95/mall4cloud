package com.mall4j.cloud.user.dto.scoreConvert;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "添加物流信息入参")
public class AddCourierCodeDTO {
    @ApiModelProperty(value = "活动id")
    private Long id;
    @ApiModelProperty(value = "物流单号")
    private String courierCode;
    @ApiModelProperty(value = "备注")
    private String note;

}
