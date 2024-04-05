package com.mall4j.cloud.common.order.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GiftSkuAttrVO {

    @ApiModelProperty("销售属性名称")
    private String attrName;


    @ApiModelProperty("销售属性值")
    private String attrValueName;
}
