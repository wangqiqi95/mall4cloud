package com.mall4j.cloud.api.delivery.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Pineapple
 * @date 2021/7/20 9:14
 */
public class DeliveryCompanyExcelVO {

    @ApiModelProperty("ID")
    private Long deliveryCompanyId;

    @ApiModelProperty("物流公司名称")
    private String name;

    public Long getDeliveryCompanyId() {
        return deliveryCompanyId;
    }

    public void setDeliveryCompanyId(Long deliveryCompanyId) {
        this.deliveryCompanyId = deliveryCompanyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DeliveryCompanyExcelVO{" +
                "deliveryCompanyId=" + deliveryCompanyId +
                ", name='" + name + '\'' +
                '}';
    }
}
