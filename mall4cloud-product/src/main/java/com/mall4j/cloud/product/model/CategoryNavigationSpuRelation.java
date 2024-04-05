package com.mall4j.cloud.product.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 分类导航商品关系表
 * @TableName category_navigation_spu_relation
 */
@TableName(value ="category_navigation_spu_relation")
@Data
public class CategoryNavigationSpuRelation implements Serializable {
    /**
     * 分类ID
     */
    @TableField(value = "category_id")
    private Long categoryId;

    /**
     * 商品SpuId
     */
    @TableField(value = "spu_id")
    private Long spuId;

    /**
     * 是否启用
     */
    @TableField(value = "is_enable")
    private Integer isEnable;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}