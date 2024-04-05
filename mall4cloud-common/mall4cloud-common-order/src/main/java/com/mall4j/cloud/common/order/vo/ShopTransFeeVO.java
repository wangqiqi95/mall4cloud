package com.mall4j.cloud.common.order.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * 店铺运费信息
 * @author FrozenWatermelon
 * @date 2020/12/16
 */
public class ShopTransFeeVO {

    @ApiModelProperty(value = "免运费金额", required = true)
    private Long freeTransfee;

    @ApiModelProperty(value = "运费", required = true)
    private Long transfee;

    @ApiModelProperty(value = "运费模板id", required = true)
    private Long deliveryTemplateId;

    public Long getFreeTransfee() {
        return freeTransfee;
    }

    public void setFreeTransfee(Long freeTransfee) {
        this.freeTransfee = freeTransfee;
    }

    public Long getTransfee() {
        return transfee;
    }

    public void setTransfee(Long transfee) {
        this.transfee = transfee;
    }

    public Long getDeliveryTemplateId() {
        return deliveryTemplateId;
    }

    public void setDeliveryTemplateId(Long deliveryTemplateId) {
        this.deliveryTemplateId = deliveryTemplateId;
    }

    @Override
    public String toString() {
        return "ShopTransFeeVO{" +
                "freeTransfee=" + freeTransfee +
                ", transfee=" + transfee +
                ", deliveryTemplateId=" + deliveryTemplateId +
                '}';
    }
}
