package com.mall4j.cloud.api.order.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @description 线上订单状态变更-发货
 * @date 2021/12/28 17:49：22
 */
public class OrderDeliveryDto implements Serializable {

    private static final long serialVersionUID = 1312637693659471097L;
    //    @ApiModelProperty(value = "小程序商城订单号")
//    private Long orderNo;
    @ApiModelProperty(value = "小程序商城订单号(对本应系统订单编号:order_number)")
    private String orderNo;

    @ApiModelProperty(value = "物流单号")
    private String logisticNo;

    @ApiModelProperty(value = "物流公司名称")
    private String logisticsName;

    @ApiModelProperty(value = "物流公司代码")
    private String deliveryCode;

    @ApiModelProperty(value = "物流公司编码类型（1-阿里 2-快递鸟 3-快递100 4-未知）")
    private Integer deliveryType;

    private List<DeliveryOrderItemDTO> selectOrderItems;


    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getLogisticNo() {
        return logisticNo;
    }

    public void setLogisticNo(String logisticNo) {
        this.logisticNo = logisticNo;
    }

    public String getLogisticsName() {
        return logisticsName;
    }

    public void setLogisticsName(String logisticsName) {
        this.logisticsName = logisticsName;
    }

    public String getDeliveryCode() {
        return deliveryCode;
    }

    public void setDeliveryCode(String deliveryCode) {
        this.deliveryCode = deliveryCode;
    }

    public Integer getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(Integer deliveryType) {
        this.deliveryType = deliveryType;
    }

    public List<DeliveryOrderItemDTO> getSelectOrderItems() {
        return selectOrderItems;
    }

    public void setSelectOrderItems(List<DeliveryOrderItemDTO> selectOrderItems) {
        this.selectOrderItems = selectOrderItems;
    }

    @Override
    public String toString() {
        return "OrderDeliveryDto{" + "orderNo=" + orderNo + ", logisticNo='" + logisticNo + '\'' + ", logisticsName='" + logisticsName + '\''
                + ", deliveryCode='" + deliveryCode + '\'' + ", deliveryType=" + deliveryType + ", selectOrderItems=" + selectOrderItems + '}';
    }
}
