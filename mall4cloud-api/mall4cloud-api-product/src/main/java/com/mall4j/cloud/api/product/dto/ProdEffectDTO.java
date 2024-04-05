package com.mall4j.cloud.api.product.dto;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author Pineapple
 * @date 2021/5/21 16:05
 */
public class ProdEffectDTO {
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

    /**
     * limit使用起始页
     */
    @ApiModelProperty("前端不传字段")
    private Integer begin;

    /**
     * limit使用大小
     */
    @ApiModelProperty("前端不传字段")
    private Integer size;

    /**
     * 当前语言
     */
    @ApiModelProperty("当前语言，前端不传")
    private Integer lang;

    /**
     * 店铺id
     */
    @ApiModelProperty("店铺id，前端不传字段")
    private Long shopId;

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

    public Integer getBegin() {
        return begin;
    }

    public void setBegin(Integer begin) {
        this.begin = begin;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getLang() {
        return lang;
    }

    public void setLang(Integer lang) {
        this.lang = lang;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    @Override
    public String toString() {
        return "ProdEffectDTO{" +
                "dateType=" + dateType +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", begin=" + begin +
                ", size=" + size +
                ", lang=" + lang +
                ", shopId=" + shopId +
                '}';
    }
}
