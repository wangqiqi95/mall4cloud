package com.mall4j.cloud.distribution.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StoreActivityProvinceVO {

    @ApiModelProperty("编码")
    private String code;
    @ApiModelProperty("名称")
    private String name;

}
