package com.mall4j.cloud.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mall4j.cloud.common.database.dto.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class QuerySonTaskStaffPageDTO extends PageDTO {

    @ApiModelProperty("员工昵称/编号")
    private String staff;

    @ApiModelProperty("员工ID")
    private List<Long> staffIdList;

    @ApiModelProperty("是否完成转发，0未完成，1完成")
    private Integer sendStatus;

    @NotNull(message = "主任务ID为必传项")
    @ApiModelProperty("主任务ID")
    private Long pushTaskId;

    @NotNull(message = "子任务ID为必传项")
    @ApiModelProperty("子任务ID")
    private Long sonTaskId;

    @ApiModelProperty(value = "开始时间")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

}
