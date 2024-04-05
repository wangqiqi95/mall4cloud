package com.mall4j.cloud.group.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class AddPopUpAdPushRuleDTO {

    @ApiModelProperty(value = "开始时间")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime startTime;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime endTime;

}
