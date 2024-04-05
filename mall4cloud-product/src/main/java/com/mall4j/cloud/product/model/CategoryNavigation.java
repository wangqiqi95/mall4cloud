package com.mall4j.cloud.product.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 分类导航_分类主表
 * @TableName category_navigation
 */
@TableName(value ="category_navigation")
@Data
public class CategoryNavigation implements Serializable {
    /**
     * 分类id
     */
    @TableId(value = "category_id", type = IdType.AUTO)
    private Long categoryId;

    /**
     * 分类名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 父ID
     */
    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * 分类描述
     */
    @TableField(value = "`desc`")
    private String desc;

    /**
     * 分类地址{parent_id}-{child_id},...
     */
    @TableField(value = "`path`")
    private String path;

    /**
     * 分类图标
     */
    @TableField(value = "icon")
    private String icon;

    /**
     * 分类的显示图片
     */
    @TableField(value = "img_url")
    private String imgUrl;

    /**
     * 分类层级 从0开始
     */
    @TableField(value = "`level`")
    private Integer level;

    /**
     * 排序
     */
    @TableField(value = "seq")
    private Integer seq;

    /**
     * 是否最后一级
     */
    @TableField(value = "is_last_level")
    private Integer isLastLevel;

    /**
     * 状态 1:enable, 0:disable
     */
    @TableField(value = "`status`")
    private Integer status;

    /**
     * 是否删除
     */
    @TableField(value = "is_delete")
    private Integer isDelete;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;



    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}