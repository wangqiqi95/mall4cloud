package com.mall4j.cloud.product.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

/**
 * 分类信息
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
@Data
public class Category extends BaseModel implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 分类id
	 */
	@TableId
	private Long categoryId;

	/**
	 * 店铺id
	 */
	private Long shopId;

	/**
	 * 父ID
	 */
	private Long parentId;

	/**
	 * 分类名称
	 */
	@TableField("`name`")
	private String name;

	/**
	 * 分类描述
	 */
	@TableField("`desc`")
	private String desc;

	/**
	 * 分类地址{parent_id}-{child_id},...
	 */
	private String path;

	/**
	 * 状态 1:enable, 0:disable, -1:deleted
	 */
	@TableField("`status`")
	private Integer status;

	/**
	 * 分类图标
	 */
	private String icon;

	/**
	 * 分类的显示图片
	 */
	private String imgUrl;

	/**
	 * 分类层级 从0开始
	 */
	@TableField("`level`")
	private Integer level;

	/**
	 * 分类扣率
	 */
	private Double deductionRate;

	/**
	 * 排序
	 */
	private Integer seq;

}
