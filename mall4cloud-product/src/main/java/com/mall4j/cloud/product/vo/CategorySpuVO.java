package com.mall4j.cloud.product.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @date 2023/6/15
 */
@Data
public class CategorySpuVO {

    /**
     * 分类id
     */
    @ApiModelProperty("分类ID")
    private Long categoryId;

    /**
     * 分类名称
     */
    @ApiModelProperty("分类名称")
    @TableField(value = "name")
    private String name;

    /**
     * 父ID
     */
    @ApiModelProperty("父ID")
    private Long parentId;

    /**
     * 分类描述
     */
    @ApiModelProperty("分类描述")
    private String desc;

    /**
     * 分类地址{parent_id}-{child_id},...
     */
    @ApiModelProperty("分类地址")
    private String path;

    /**
     * 分类地址字符信息，已转为name
     */
    @ApiModelProperty("分类地址 name形式")
    private String pathStr;

    /**
     * 分类图标
     */
    @ApiModelProperty("分类图标")
    private String icon;

    /**
     * 分类的显示图片
     */
    @ApiModelProperty("分类图片")
    private String imgUrl;

    /**
     * 分类层级 从0开始
     */
    @ApiModelProperty("分类级别")
    private Integer level;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer seq;

    /**
     * 是否最后一级
     */
    @ApiModelProperty("是否最后一级")
    private Integer isLastLevel;

    /**
     * 分类状态
     */
    @ApiModelProperty("分类状态")
    private Integer categoryStatus;

    @ApiModelProperty(value = "商品id")
    private Long spuId;

    @ApiModelProperty(value = "商品名称")
    private String spuName;

    @ApiModelProperty(value = "商品编码")
    private String spuCode;

    @ApiModelProperty("商品介绍主图")
    private String mainImgUrl;

    @ApiModelProperty(value = "商品售价")
    private Long priceFee;

    @ApiModelProperty(value = "市场价，整数方式保存")
    private Long marketPriceFee;

    @ApiModelProperty(value = "销量")
    private Integer saleNum;

    @ApiModelProperty(value = "库存")
    private Integer stock;

    @ApiModelProperty("积分价格")
    private Long scoreFee;

    @ApiModelProperty("erpCategoryName")
    private String erpCategoryName;

    @ApiModelProperty("店铺分类")
    private String shopCategory;

    @ApiModelProperty("商品状态")
    private Integer status;

}
