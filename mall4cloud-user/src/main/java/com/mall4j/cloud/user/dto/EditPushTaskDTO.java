package com.mall4j.cloud.user.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class EditPushTaskDTO {


    @ApiModelProperty(value = "群发任务ID")
    private Long groupPushTaskId;

    @ApiModelProperty(value = "任务名称")
    private String taskName;

    @ApiModelProperty(value = "是否全部门店标识，1全部，0指定")
    private Integer isAllShop;

    @ApiModelProperty(value = "门店列表")
    private List<Long> storeIdList;

    @ApiModelProperty(value = "标签ID")
    private Long tagId;

    @ApiModelProperty(value = "编辑用户ID（后台交互参数，不需要传）")
    private Long updateUserId;

    @ApiModelProperty(value = "取法推送列表")
    private List<EditPushSonTaskDTO> pushSonTaskList;


}
