package com.mall4j.cloud.flow.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotNull;
import java.util.Date;


/**
 * 流量分析—页面数据统计表
 *
 * @author YXF
 * @date 2021-06-02
 */
public class FlowAnalysisDTO {

    @ApiModelProperty("时间类型 1:自然日  2:自然周 3:自然月 4:今日实时 5:近七天 6:近30天")
    @NotNull(message = "时间类型不能为空")
    private Integer timeType;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("开始时间")
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("结束时间")
    private Date endTime;
    /**
     * 开始时间（时间戳）
     */
    private Long start;
    /**
     * 类型 1:页面统计数据  2:商品详情页数据
     */
    private Integer pageType;
    /**
     * 系统类型 0:全部 1:pc  2:h5  3:小程序 4:安卓  5:ios
     */
    private Integer systemType;

    public Integer getTimeType() {
        return timeType;
    }

    public void setTimeType(Integer timeType) {
        this.timeType = timeType;
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

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Integer getSystemType() {
        return systemType;
    }

    public void setSystemType(Integer systemType) {
        this.systemType = systemType;
    }

    @Override
    public String toString() {
        return "FlowAnalysisDTO{" +
                "timeType=" + timeType +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", start=" + start +
                ", systemType=" + systemType +
                '}';
    }
}
