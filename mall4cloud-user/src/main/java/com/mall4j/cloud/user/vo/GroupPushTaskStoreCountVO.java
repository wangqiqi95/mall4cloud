package com.mall4j.cloud.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GroupPushTaskStoreCountVO {

    @ApiModelProperty("群发任务ID")
    private Long groupPushTaskId;


    @ApiModelProperty("群发任务ID")
    private Integer storeCount;
}
