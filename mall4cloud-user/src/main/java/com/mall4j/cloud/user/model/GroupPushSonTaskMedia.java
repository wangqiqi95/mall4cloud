package com.mall4j.cloud.user.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 推送子任务素材表
 * </p>
 *
 * @author ben
 * @since 2023-02-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_group_push_son_task_media")
@ApiModel(value="GroupPushSonTaskMedia对象", description="推送子任务素材表")
public class GroupPushSonTaskMedia extends Model<GroupPushSonTaskMedia> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "son_task_media_id", type = IdType.AUTO)
    private Long sonTaskMediaId;

    private Long groupPushTaskId;

    @ApiModelProperty(value = "推送子任务ID")
    private Long groupPushSonTaskId;

    @ApiModelProperty(value = "素材内容")
    private String media;

    @ApiModelProperty(value = "内容类型，image：图片，video：视频，link：H5，miniprogram：小程序")
    private String type;

    @ApiModelProperty(value = "创建人ID")
    private Long createUserId;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


    @Override
    protected Serializable pkVal() {
        return this.sonTaskMediaId;
    }

}
