package com.mall4j.cloud.distribution.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DistributionStoreActivityProvinceCountVO {

    @ApiModelProperty("省份编码")
    private String provinceCode;
    @ApiModelProperty("省份名称")
    private String provinceName;
    @ApiModelProperty("活动数量")
    private Integer count;

}
