package com.mall4j.cloud.flow.dto;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author Pineapple
 * @date 2021/6/3 15:35
 */
public class UserVisitDTO {

    /**
     * 时间类型 1.日 2.周 3.月 4.今日实时 5.近七天 6.近30天 7.自定义
     * FlowTimeTypeEnum
     */
    @ApiModelProperty("时间类型 1.日 3.月 4.今日实时 5.近七天 6.近30天")
    private Integer dateType;
    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    public Integer getDateType() {
        return dateType;
    }

    public void setDateType(Integer dateType) {
        this.dateType = dateType;
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
        return "UserVisitDTO{" +
                "dateType=" + dateType +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
