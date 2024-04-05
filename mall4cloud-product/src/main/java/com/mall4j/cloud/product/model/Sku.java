package com.mall4j.cloud.product.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * sku信息
 *
 * @author FrozenWatermelon
 * @date 2020-12-08 15:54:22
 */
@Data
public class Sku extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 属性id
     */
    @TableId(type = IdType.AUTO)
    private Long skuId;

    /**
     * SPU id
     */
    private Long spuId;

    /**
     * 多个销售属性值id逗号分隔
     */
//    private String attrs;

    /**
     * sku名称
     */
    private String name;

    /**
     * sku图片
     */
    private String imgUrl;

    /**
     * 售价，整数方式保存
     */
    private Long priceFee;

    /**
     * 市场价，整数方式保存
     */
    private Long marketPriceFee;

    /**
     * 市场价，整数方式保存(上一次)
     */
    private Long pastMarketPriceFee;

    /**
     * 活动价
     */
    private Long activityPrice;

    /**
     * 渠道价
     */
    private Long channelPrice;

    /**
     * 铺货价
     */
    private Long phPrice;

    /**
     * 暂时无用途，目前做数据对比参考 2022-05-16 11:22 gmq
     */
    private Long protectPrice;

    /**
     * 积分价格
     */
    private Long scoreFee;

    /**
     * 商品编码
     */
    private String partyCode;

    /**
     * 商品条形码
     */
    private String modelId;

    /**
     * 商品重量
     */
    private BigDecimal weight;

    /**
     * 商品体积
     */
    private BigDecimal volume;

    /**
     * 状态 1:enable, 0:disable, -1:deleted
     */
    private Integer status;

    private String skuCode;

    private  String colorCode;

    private String intscode;

    private String forcode;

    private String priceCode;

    private String styleCode;

    private String sex;

    @ApiModelProperty(value = "商品渠道（R线下渠道 T电商渠道 L清货渠道）")
    private String channelName;

    @ApiModelProperty(value = "折扣等级（错误数据或者-1,默认为0）")
    private String channelDiscount;

    @TableField(exist = false)
    private Long storeId;

    @TableField(exist = false)
    private String spuCode;


    /**
     * erp同步商品基础数据拆分入库 spu_sku_attr_value 使用
     * 默认属性： 颜色 ，尺寸
     * 颜色：colorName
     * 规格：sizeName
     * spu_sku_attr_value 对应 每个sku 两条数据
     * attr_name= 规格名 （颜色 ，尺寸）
     * attr_value_name = 具体属性（colorName，sizeName）
     */
    @TableField(exist = false)
    private String sizeName;
    @TableField(exist = false)
    private String colorName;

}
