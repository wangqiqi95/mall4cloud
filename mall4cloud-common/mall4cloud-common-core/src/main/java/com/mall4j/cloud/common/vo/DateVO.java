package com.mall4j.cloud.common.vo;

import java.util.Date;

/**
 * 时间参数
 * @author lhd
 */
public class DateVO {

    private Date startTime;

    private Date endTime;

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
        return "DateVO{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
