package com.mall4j.cloud.common.order.vo;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * 自提订单信息
 * @author FrozenWatermelon
 * @date 2020/12/15
 */
public class OrderSelfStationVO {

    @ApiModelProperty(value = "自提点id")
    private Long stationId;

    @ApiModelProperty(value = "自提点名称")
    private String stationName;

    @ApiModelProperty(value = "自提人的手机")
    private String stationUserMobile;

    @ApiModelProperty(value = "自提人的名字")
    private String stationUserName;

    @ApiModelProperty(value = "自提时间(用户下单时选择)")
    private String stationTime;

    @ApiModelProperty(value = "自提点的地址")
    private String stationAddress;

    @ApiModelProperty(value = "自提点的联系电话")
    private String stationPhone;

    @ApiModelProperty(value = "经度")
    private BigDecimal lng;

    @ApiModelProperty(value = "纬度")
    private BigDecimal lat;

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationUserMobile() {
        return stationUserMobile;
    }

    public void setStationUserMobile(String stationUserMobile) {
        this.stationUserMobile = stationUserMobile;
    }

    public String getStationUserName() {
        return stationUserName;
    }

    public void setStationUserName(String stationUserName) {
        this.stationUserName = stationUserName;
    }

    public String getStationTime() {
        return stationTime;
    }

    public void setStationTime(String stationTime) {
        this.stationTime = stationTime;
    }

    public String getStationAddress() {
        return stationAddress;
    }

    public void setStationAddress(String stationAddress) {
        this.stationAddress = stationAddress;
    }

    public String getStationPhone() {
        return stationPhone;
    }

    public void setStationPhone(String stationPhone) {
        this.stationPhone = stationPhone;
    }

    public BigDecimal getLng() {
        return lng;
    }

    public void setLng(BigDecimal lng) {
        this.lng = lng;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return "OrderSelfStationVO{" +
                "stationId=" + stationId +
                ", stationName='" + stationName + '\'' +
                ", stationUserMobile='" + stationUserMobile + '\'' +
                ", stationUserName='" + stationUserName + '\'' +
                ", stationTime='" + stationTime + '\'' +
                ", stationAddress='" + stationAddress + '\'' +
                ", stationPhone='" + stationPhone + '\'' +
                ", lng=" + lng +
                ", lat=" + lat +
                '}';
    }
}
