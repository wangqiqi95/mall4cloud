package com.mall4j.cloud.product.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 分类导航节点关系表
 * @TableName category_navigation_relation
 */
@TableName(value ="category_navigation_relation")
@Data
public class CategoryNavigationRelation implements Serializable {
    /**
     * 祖节点ID
     */
    @TableField(value = "ancestor_category_id")
    private Long ancestorCategoryId;

    /**
     * 子节点ID
     */
    @TableField(value = "descendant_category_id")
    private Long descendantCategoryId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}