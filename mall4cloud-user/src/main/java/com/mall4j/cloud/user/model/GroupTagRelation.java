package com.mall4j.cloud.user.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 标签组与标签关联表
 * </p>
 *
 * @author ben
 * @since 2023-02-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_group_tag_relation")
@ApiModel(value="GroupTagRelation对象", description="标签组与标签关联表")
public class GroupTagRelation extends Model<GroupTagRelation> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "group_tag_relation_id", type = IdType.AUTO)
    private Long groupTagRelationId;

    @ApiModelProperty(value = "标签组ID")
    private Long groupId;

    @ApiModelProperty(value = "标签ID")
    private Long tagId;

    @ApiModelProperty(value = "创建人")
    private Long createUser;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


    @Override
    protected Serializable pkVal() {
        return this.groupTagRelationId;
    }

}
