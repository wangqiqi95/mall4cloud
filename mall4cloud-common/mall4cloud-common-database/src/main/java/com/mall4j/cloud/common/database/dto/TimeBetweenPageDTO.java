package com.mall4j.cloud.common.database.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 带时间范围查询的分页参数基类
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TimeBetweenPageDTO extends PageDTO {

    @ApiModelProperty("开始时间")
    private Date startTime;

    @ApiModelProperty("结束时间")
    private Date endTime;
}
