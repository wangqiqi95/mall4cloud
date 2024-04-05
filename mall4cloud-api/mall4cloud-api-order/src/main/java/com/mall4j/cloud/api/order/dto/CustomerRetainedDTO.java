package com.mall4j.cloud.api.order.dto;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author cl
 * @date 2021-05-22 14:28:43
 */
public class CustomerRetainedDTO {
    /**
     * 时间类型 1最近1个月 2最近3个月 3最近6个月 4最近1年
     * RetainedDateType
     */
    @ApiModelProperty("时间类型 1最近三个月 2最近六个月 3最近一年")
    private Integer dateType;

    @ApiModelProperty("留存类型 1访问留存  2成交留存")
    private Integer retainType;
    /**
     *  1月留存 2周留存
     */
    @ApiModelProperty("1月留存 2周留存,暂时不统计周留存")
    private Integer dateRetainType;

    @ApiModelProperty("开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

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

    public Integer getDateType() {
        return dateType;
    }

    public void setDateType(Integer dateType) {
        this.dateType = dateType;
    }

    public Integer getRetainType() {
        return retainType;
    }

    public void setRetainType(Integer retainType) {
        this.retainType = retainType;
    }

    public Integer getDateRetainType() {
        return dateRetainType;
    }

    public void setDateRetainType(Integer dateRetainType) {
        this.dateRetainType = dateRetainType;
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

    @Override
    public String toString() {
        return "CustomerRetainedDTO{" +
                "dateType=" + dateType +
                ", retainType=" + retainType +
                ", dateRetainType=" + dateRetainType +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", dateTime=" + dateTime +
                ", shopId=" + shopId +
                ", appId=" + appId +
                '}';
    }
}
