package com.mall4j.cloud.product.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * spu信息
 *
 * @TableName spu_store
 */
@Data
public class SkuStore {


    @TableId(type = IdType.AUTO)
    private Long skuStoreId;

    private Long skuId;

    private String skuCode;

    private Long storeId;

    private Long spuId;

    private Long priceFee;

    private Long marketPriceFee;

    private Long pastMarketPriceFee;

    private Long activityPrice;

    private Long protectPrice;

    /**
     * 渠道价
     */
    private Long channelPrice;

    /**
     * 铺货价(用于渠道价变更恢复售价=铺货价)
     */
    private Long phPrice;

    private Integer stock;

    private Date createTime;

    private Date updateTime;

    private String priceCode;

    private String em;

    /**
     * 状态 1:enable, 0:disable, -1:deleted
     */
    private Integer status;

    /**
     * sku表
     */
    @TableField(exist = false)
    private String intscode;
    @TableField(exist = false)
    private String forcode;

    /**
     * spu表 商品名称
     */
    @TableField(exist = false)
    private String spuName;
    @TableField(exist = false)
    private String spuCode;
    @TableField(exist = false)
    private String spuStyleCode;

}