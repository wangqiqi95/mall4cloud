package com.mall4j.cloud.user.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StaffSonTaskSendDataVO {

    @ApiModelProperty("主任务ID")
    private Long taskId;

    @ApiModelProperty("子任务ID")
    private Long sonTaskId;

    @ApiModelProperty("完成推送个数")
    private Integer sendCount;

}
