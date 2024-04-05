package com.mall4j.cloud.product.dto;

import com.mall4j.cloud.product.model.TagActivity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SpuAppPageVO {
    /**
     * spu id
     */
    private Long spuId;

    /**
     * 商品货号
     */
    private String spuCode;


    /**
     * 店铺分类ID
     */
    private Long shopCategoryId;


    /**
     * spu名称
     */
    private String name;

    /**
     * 卖点
     */
    private String sellingPoint;

    /**
     * 主图
     */
    private String mainImgUrl;


    /**
     * 售价，整数方式保存
     */
    private Long priceFee;

    /**
     * 市场价，整数方式保存
     */
    private Long marketPriceFee;

    /**
     * 积分价格
     */
    private Long scoreFee;

    /**
     * 状态 1:enable, 0:disable, -1:deleted
     */
    private Integer status;
    /**
     *
     */
    private Integer hasSkuImg;

    /**
     * 商品类型(0普通商品 1拼团 2秒杀 3积分)
     */
    private Integer spuType;

    /**
     * 活动id(关联prod_type)
     */
    private Long activityId;

    /**
     * 是否为组合商品0普通商品，1组合商品
     */
    private Integer isCompose;

    /**
     * 序号
     */
    private Integer seq;

    private String spuName;

    private String shopCategory;

    private TagActivity tagActivity;

    @ApiModelProperty(value = "是否会员日")
    private Boolean memberPriceFlag=Boolean.FALSE;


}
