package com.mall4j.cloud.api.user.dto;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author lhd
 */
public class CustomerReqDTO {
    /**
     * 时间类型 1今日实时 2 近7天 3 近30天 4自然日 5自然月
     */
    @ApiModelProperty("时间类型 1今日实时 2 近7天 3 近30天 4自然日 5自然月")
    private Integer dateType;
    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dayStartTime;
    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    /**
     * 时间
     */
    @ApiModelProperty("不传字段")
    private Date dateTime;

    /**
     * 店铺id
     */
    @ApiModelProperty("不传字段")
    private Long shopId;

    /**
     * 第三方系统id 1：微信小程序
     */
    @ApiModelProperty("不传字段")
    private Integer appId;

    /**
     * 粉丝=付费会员
     */
    private Integer member;

    public Date getDayStartTime() {
        return dayStartTime;
    }

    public void setDayStartTime(Date dayStartTime) {
        this.dayStartTime = dayStartTime;
    }

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

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public Integer getMember() {
        return member;
    }

    public void setMember(Integer member) {
        this.member = member;
    }

    @Override
    public String toString() {
        return "CustomerReqDTO{" +
                "dateType=" + dateType +
                ", startTime=" + startTime +
                ", dayStartTime=" + dayStartTime +
                ", endTime=" + endTime +
                ", dateTime=" + dateTime +
                ", shopId=" + shopId +
                ", appId=" + appId +
                ", member=" + member +
                '}';
    }
}
