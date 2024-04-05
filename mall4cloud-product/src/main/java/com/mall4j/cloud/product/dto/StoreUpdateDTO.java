package com.mall4j.cloud.product.dto;

import com.mall4j.cloud.common.product.dto.SkuDTO;
import com.mall4j.cloud.common.product.dto.SpuAttrValueDTO;
import com.mall4j.cloud.common.product.dto.SpuLangDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel("门店商品编辑参数")
public class StoreUpdateDTO {

    @ApiModelProperty("spuId")
    private Long spuId;

    @ApiModelProperty("spu名称")
    private String name;

    @ApiModelProperty("卖点")
    private String sellingPoint;

    @NotNull(message = "商品轮播图不能为空")
    @ApiModelProperty("商品介绍主图 多个图片逗号分隔")
    private String imgUrls;

    @NotNull(message = "商品主图不能为空")
    @ApiModelProperty("商品主图")
    private String mainImgUrl;

    @ApiModelProperty("市场价")
    private Long marketPriceFee;

    @NotNull(message = "售价不能为空")
    @ApiModelProperty("售价")
    private Long priceFee;

    @ApiModelProperty("状态 1:enable, 0:disable, -1:deleted")
    private Integer status;

    @ApiModelProperty("商品属性值列表")
    private List<SpuAttrValueDTO> spuAttrValues;

    @NotEmpty(message = "sku信息不能为空")
    @ApiModelProperty("商品规格列表")
    private List<SkuDTO> skuList;

    @ApiModelProperty("商品详情")
    private String detail;

    @ApiModelProperty("商品视频")
    private String video;

    @ApiModelProperty("积分价格")
    private Long scoreFee;

    @ApiModelProperty("sku是否含有图片 0无 1有")
    private Integer hasSkuImg;

}
