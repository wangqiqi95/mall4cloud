package com.mall4j.cloud.product.model;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * spu信息
 *
 * @author FrozenWatermelon
 * @date 2020-12-08 15:54:23
 */
@Data
public class Spu extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * spu id
     */
    @TableId(type = IdType.AUTO)
    private Long spuId;

    /**
     * 品牌ID
     */
    private Long brandId;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 店铺分类ID
     */
    private Long shopCategoryId;

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * spu名称
     */
    private String name;

    /**
     * 卖点
     */
//    private String sellingPoint;

    /**
     * 主图
     */
    private String mainImgUrl;

    /**
     * 商品图片 多个图片逗号分隔
     */
    private String imgUrls;

    /**
     * 商品视频
     */
    private String video;

    /**
     * 售价，整数方式保存
     */
    private Long priceFee;

    /**
     * 市场价，整数方式保存
     */
    private Long marketPriceFee;

    /**
     * 渠道价
     */
    private Long channelPrice;

    /**
     * 铺货价
     */
    private Long phPrice;

    /**
     * 积分价格
     */
    private Long scoreFee;

    /**
     * 状态 1:enable, 0:disable, -1:deleted
     */
    private Integer status;

    /**
     * 配送方式json见TransportModeVO
     */
    private String deliveryMode;

    /**
     * 运费模板id
     */
    private Long deliveryTemplateId;

    /**
     *
     */
    private Integer hasSkuImg;

    /**
     *是否爱普货: 0否 1是
     */
    private Integer iphStatus;

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

    /**
     * 是否置顶，1.置顶 0.不置顶
     */
    private Integer isTop;

    @ApiModelProperty("是否限购  默认不限购 0-不限购 1- 限购")
    private Boolean isLimit;
    @ApiModelProperty("限制数量")
    private Integer limitNumber;
    @ApiModelProperty("限制方式 1-每人限购 2-每次限购")
    private Integer limitType;

    private String spuCode;

    private String styleCode;

    private String sellingPoint;

    @ApiModelProperty(value = "平台一级分类id")
    private Long primaryCategoryId;

    @ApiModelProperty(value = "平台二级分类id")
    private Long secondaryCategoryId;

    @ApiModelProperty(value = "商品渠道（R线下渠道 T电商渠道 L清货渠道）")
    private String channelName;

    @ApiModelProperty(value = "折扣等级（错误数据或者-1,默认为0）")
    private String channelDiscount;

    @ApiModelProperty(value = "商品所属性别")
    private String sex;

    @ApiModelProperty(value = "商品简称")
    private String abbr;

}
