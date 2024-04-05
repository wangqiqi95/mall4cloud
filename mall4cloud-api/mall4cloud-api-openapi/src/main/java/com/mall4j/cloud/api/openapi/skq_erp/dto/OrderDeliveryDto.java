package com.mall4j.cloud.api.openapi.skq_erp.dto;

import com.mall4j.cloud.api.openapi.skq_erp.enums.DeliverType;
import com.mall4j.cloud.api.openapi.skq_erp.response.ErpResponse;
import com.mall4j.cloud.api.openapi.skq_erp.response.ErpResponseEnum;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * @description 线上订单状态变更-发货
 * @date 2021/12/28 17:49：22
 */
public class OrderDeliveryDto implements Serializable, IDataCheck {

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

    @Override
    public ErpResponse check() {
        if (orderNo == null) {
            return ErpResponse.fail(ErpResponseEnum.HTTP_MESSAGE_NOT_READABLE.value(), "小程序商城订单号不能为空");
        }
        if (StringUtils.isBlank(logisticNo)) {
            return ErpResponse.fail(ErpResponseEnum.HTTP_MESSAGE_NOT_READABLE.value(), "物流单号不能为空");
        }
        if (StringUtils.isBlank(logisticsName)) {
            return ErpResponse.fail(ErpResponseEnum.HTTP_MESSAGE_NOT_READABLE.value(), "物流公司名称不能为空");
        }
        if (StringUtils.isBlank(deliveryCode)) {
            return ErpResponse.fail(ErpResponseEnum.HTTP_MESSAGE_NOT_READABLE.value(), "物流公司代码不能为空");
        }
        if (deliveryType == null) {
            return ErpResponse.fail(ErpResponseEnum.HTTP_MESSAGE_NOT_READABLE.value(), "物流公司代码类型不能为空");
        }
        if (!DeliverType.getByCode(deliveryType).isPresent()) {
            return ErpResponse.fail(ErpResponseEnum.HTTP_MESSAGE_NOT_READABLE.value(), "物流公司代码类型不正确");
        }
        return ErpResponse.success();
    }
}
