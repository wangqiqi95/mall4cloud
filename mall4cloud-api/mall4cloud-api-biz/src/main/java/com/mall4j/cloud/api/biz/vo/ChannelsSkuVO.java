package com.mall4j.cloud.api.biz.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @date 2023/3/15
 */
@Data
public class ChannelsSkuVO {

    /**
     * 内部spu_id
     */
    private Long spuId;

    /**
     * 视频号spu_id
     */
    private Long outSpuId;

    /**
     * 内部sku_id
     */
    private Long skuId;

    /**
     * 视频号sku_id
     */
    private Long outSkuId;

    /**
     * sku小图
     */
    private String thumbImg;

    /**
     * 发货方式，若为无需快递（仅对部分类目开放），则无需填写运费模版id。0:快递发货；1:无需快递；默认0
     */
    private Integer deliverMethod;

    /**
     * 商品详情信息
     */
    private String descInfo;

    /**
     * 商品封面，存储的是mediaID（mediaID获取后，三天内有效）
     */
    private String converImgUrl;

    /**
     * 视频号价格
     */
    private Long price;

    /**
     * 视频号库存
     */
    private Integer stockNum;

    @ApiModelProperty("总库存")
    private Integer stock;

    @ApiModelProperty("售价，整数方式保存")
    private Long priceFee;

    @ApiModelProperty("市场价，整数方式保存")
    private Long marketPriceFee;

    @ApiModelProperty("官店保护价(用于取价判断)")
    private Long skuProtectPrice;
}
