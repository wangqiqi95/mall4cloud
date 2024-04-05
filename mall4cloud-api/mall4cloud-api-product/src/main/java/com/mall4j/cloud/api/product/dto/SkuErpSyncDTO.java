package com.mall4j.cloud.api.product.dto;

import com.mall4j.cloud.common.product.dto.SkuLangDTO;
import com.mall4j.cloud.common.product.dto.SpuSkuAttrValueDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
@Data
public class SkuErpSyncDTO {


    @ApiModelProperty("sku名称")
    private String skuName;

    @ApiModelProperty("售价，整数方式保存")
    private Long priceFee;

    @ApiModelProperty("市场价，整数方式保存")
    private Long marketPriceFee;

    @ApiModelProperty("状态 1:enable, 0:disable, -1:deleted")
    private Integer status;


    @ApiModelProperty("商品编码")
    private String spuCode;

    @ApiModelProperty("商品条形码")
    private String skuCode;

    @ApiModelProperty(value = "商品渠道（R线下渠道 T电商渠道 L清货渠道）")
    private String channelName;

    @ApiModelProperty(value = "折扣等级（错误数据或者-1,默认为0）")
    private String discount;

    private String colorCode;

    private String colorName;

    private String styleCode;

    private String sizeCode;

    private String sizeName;

    private String ageGroup;

    private String ageRank;

    private String sex;

    private String intsCode;

    private String forcode;

    private String priceCode;
}
