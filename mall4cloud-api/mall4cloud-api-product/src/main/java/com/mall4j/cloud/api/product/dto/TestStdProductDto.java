package com.mall4j.cloud.api.product.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 类描述：商品基础信息同步
 *
 * @date 2022/1/16 9:28：49
 */
@Data
public class TestStdProductDto implements Serializable {


	@ApiModelProperty(value = "商品编码")
	private String no;

	@ApiModelProperty(value = "款号")
	private String style;

	@ApiModelProperty(value = "类别")
	private String styleType;

	@ApiModelProperty(value = "短描述")
	private String describeshort;

	@ApiModelProperty(value = "款+色")
	private String name;

	@ApiModelProperty(value = "性别")
	private String sex;

	@ApiModelProperty(value = "年龄组")
	private String ageGroup;

	@ApiModelProperty(value = "年龄范围")
	private String ageRank;

	@ApiModelProperty(value = "国际条码")
	private String forcode;

	@ApiModelProperty(value = "国标码")
	private String intscode;

	@ApiModelProperty(value = "颜色名称")
	private String colorName;

	@ApiModelProperty(value = "颜色编号")
	private String colorCode;

	@ApiModelProperty(value = "尺码名称")
	private String sizeName;

	@ApiModelProperty(value = "尺码编号")
	private String sizeCode;

	@ApiModelProperty(value = "吊牌价")
	private Integer pricelist;

	@ApiModelProperty(value = "修改日期")
	private String modifieddate;

	@ApiModelProperty(value = "是否有效")
	private String isactive;

	@ApiModelProperty(value = "商品编码")
	private String spuCode;

	@ApiModelProperty(value = "商品渠道（R线下渠道 T电商渠道 L清货渠道）")
	private String channelName;

	@ApiModelProperty(value = "折扣等级（错误数据或者-1,默认为0）")
	private String discount;

}
