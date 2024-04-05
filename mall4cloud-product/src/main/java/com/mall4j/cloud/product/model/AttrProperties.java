package com.mall4j.cloud.product.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Description
 * @Author axin
 * @Date 2023-06-13
 **/
@Data
@TableName("attr_properties")
public class AttrProperties {
    @TableId
    private Long id;

    /**
     * 是否默认属性 0否 1是
     */
    @TableField(value = "`default_prop`")
    private Boolean defaultProp;

    /**
     * 关联attr ID
     */
    @TableField("`attr_id`")
    private Long attrId;

    /**
     * 是否模糊匹配0否 1是
     */
    @TableField("`vague`")
    private Boolean vague;

    /**
     * 所属类型
     */
    @TableField("`type`")
    private String type;

    /**
     * 所属人群（性别）
     */
    @TableField("`sex`")
    private String sex;
}
