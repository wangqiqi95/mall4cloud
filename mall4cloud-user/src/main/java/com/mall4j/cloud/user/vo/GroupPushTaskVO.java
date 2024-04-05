package com.mall4j.cloud.user.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GroupPushTaskVO {


    @ApiModelProperty(value = "主键")
    private Long groupPushTaskId;

    @ApiModelProperty(value = "任务名称")
    private String taskName;

    @ApiModelProperty(value = "是否全部门店标识，0全部，1指定")
    private Integer isAllShop;

    @ApiModelProperty(value = "标签ID")
    private Long tagId;

    @ApiModelProperty(value = "创建人")
    private Long createUserId;

    @ApiModelProperty(value = "操作状态，0创建中，1可用，2创建失败，3编辑中, 4编辑失败")
    private Integer operateStatus;

    @ApiModelProperty(value = "失败时的参数")
    private String failParam;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新用户ID")
    private Long updateUserId;

    @ApiModelProperty(value = "更新用户ID")
    private LocalDateTime updateTime;
}
