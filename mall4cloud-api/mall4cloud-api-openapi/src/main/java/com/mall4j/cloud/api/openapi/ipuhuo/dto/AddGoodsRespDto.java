package com.mall4j.cloud.api.openapi.ipuhuo.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 类描述：添加商品 响应
 */
public class AddGoodsRespDto implements Serializable, BaseResultDto {

    private static final long serialVersionUID = -7910487934143455708L;

    @ApiModelProperty(value = "商品序号，商城范围内唯一标识", required = true)
    private String itemid;

    @ApiModelProperty(value = "SkuId列表，按传入顺序一致", required = true)
    private Integer[] skuIds;

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public Integer[] getSkuIds() {
        return skuIds;
    }

    public void setSkuIds(Integer[] skuIds) {
        this.skuIds = skuIds;
    }

    @Override
    public String toString() {
        return "AddGoodsRespDto{" + "itemid='" + itemid + '\'' + ", skuIds=" + Arrays.toString(skuIds) + '}';
    }
}
