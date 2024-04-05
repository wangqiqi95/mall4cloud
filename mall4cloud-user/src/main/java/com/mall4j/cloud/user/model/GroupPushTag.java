package com.mall4j.cloud.user.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_group_push_tag")
public class GroupPushTag extends Model<GroupPushTag> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "group_push_tag_id", type = IdType.AUTO)
    private Long groupPushTagId;

    @ApiModelProperty(value = "群发任务ID")
    private Long groupPushTaskId;

    @ApiModelProperty(value = "id值：若为全部客户触达 则不传 | 若为按客户标签筛选 则传标签id | 若为按客户分组筛选 则为客户分组id | 若为按部门员工筛选 则为部门id")
    private String tagId;

    @ApiModelProperty(value = "name")
    private String tagName;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}
