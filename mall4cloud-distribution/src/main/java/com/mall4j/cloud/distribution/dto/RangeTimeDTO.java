package com.mall4j.cloud.distribution.dto;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author cl
 * @date 2021-08-17 09:52:03
 */
public class RangeTimeDTO {

    @ApiModelProperty(value = "起始时间",required=true)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty(value = "结束时间",required=true)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    public RangeTimeDTO() {
    }

    public RangeTimeDTO(Date startTime, Date endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "RangeTimeDTO{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
