package com.mall4j.cloud.common.product.vo.app;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mall4j.cloud.common.serializer.ImgJsonSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分类信息VO
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
@Data
public class CategoryAppVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("分类id")
    private Long categoryId;

    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty("父ID")
    private Long parentId;

    @ApiModelProperty("分类名称")
    private String name;

    @ApiModelProperty("父分类名称")
	private String parentName;

	@JsonSerialize(using = ImgJsonSerializer.class)
    @ApiModelProperty("分类图标")
    private String icon;

	@JsonSerialize(using = ImgJsonSerializer.class)
    @ApiModelProperty("分类的显示图片")
    private String imgUrl;

    @ApiModelProperty("分类层级 从0开始")
    private Integer level;

	@ApiModelProperty("上级/子分类列表")
	private List<CategoryAppVO> categories;

	@ApiModelProperty("平台一级分类id")
	private Long primaryCategoryId;

	@ApiModelProperty("分类扣率")
	private Double deductionRate;

	@ApiModelProperty("分类状态")
	private Integer status;

	/**
	 * 序号
	 */
	private Integer seq;

}
