package com.mall4j.cloud.flow.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户流量记录
 *
 * @author YXF
 * @date 2020-07-13 13:18:33
 */
public class FlowLogDTO implements Serializable{

    @ApiModelProperty("uuid")
    private String uuid;

    @ApiModelProperty("系统类型 1:pc  2:h5  3:小程序 4:安卓  5:ios")
    private Integer systemType;

    @ApiModelProperty("页面id")
    private Integer pageId;

    @ApiModelProperty("1:页面访问  2:分享访问  3:页面点击 4:加购")
    private Integer visitType;

    @ApiModelProperty("业务数据（商品页：商品id;支付界面：订单编号数组；支付成功界面：订单编号数组）")
    private String bizData;

    @ApiModelProperty("用户操作步骤数")
    private Integer step;

    @ApiModelProperty("用户操作数量")
    private Integer nums;

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户登陆ip
     */
    private String ip;

    /**
     * 创建时间
     */
    private Date createTime;

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getSystemType() {
        return systemType;
    }

    public void setSystemType(Integer systemType) {
        this.systemType = systemType;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPageId() {
        return pageId;
    }

    public void setPageId(Integer pageId) {
        this.pageId = pageId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getVisitType() {
        return visitType;
    }

    public void setVisitType(Integer visitType) {
        this.visitType = visitType;
    }

    public String getBizData() {
        return bizData;
    }

    public void setBizData(String bizData) {
        this.bizData = bizData;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public Integer getNums() {
        return nums;
    }

    public void setNums(Integer nums) {
        this.nums = nums;
    }


    @Override
    public String toString() {
        return "FlowLogDTO{" +
                "uuid='" + uuid + '\'' +
                ", userId='" + userId + '\'' +
                ", systemType=" + systemType +
                ", ip='" + ip + '\'' +
                ", pageId=" + pageId +
                ", createTime=" + createTime +
                ", visitType=" + visitType +
                ", bizData='" + bizData + '\'' +
                ", step=" + step +
                ", nums=" + nums +
                '}';
    }
}
