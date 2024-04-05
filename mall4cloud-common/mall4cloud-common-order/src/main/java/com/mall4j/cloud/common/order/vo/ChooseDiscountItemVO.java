package com.mall4j.cloud.common.order.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 购物车中选中的满减活动项信息
 * @author FrozenWatermelon
 * @date 2020-11-20 15:47:32
 */
public class ChooseDiscountItemVO implements Serializable {

    @ApiModelProperty("满减满折优惠id")
    private Long discountId;

    @ApiModelProperty("活动名称")
    private String discountName;

    @ApiModelProperty("优惠规则(0:满钱减钱 1:满件减钱 2:满钱打折 3:满件打折)")
    private Integer discountRule;

    @ApiModelProperty(value = "优惠项id")
    @JsonIgnore
    private Long discountItemId;

    @ApiModelProperty(value = "所需需要金额")
    private Long needAmount;

    @ApiModelProperty(value = "减免类型 0按满足最高层级减一次 1每满一次减一次")
    private Integer discountType;

    @ApiModelProperty(value = "参与满减满折优惠的商品数量")
    private Integer count;

    @ApiModelProperty(value = "参与满减满折优惠的商品金额")
    private Long prodsPrice;

    @ApiModelProperty(value = "优惠（元/折）")
    private Long discount;

    @ApiModelProperty(value = "参与满减满折优惠的金额")
    private Long reduceAmount;

    public Long getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Long discountId) {
        this.discountId = discountId;
    }

    public String getDiscountName() {
        return discountName;
    }

    public void setDiscountName(String discountName) {
        this.discountName = discountName;
    }

    public Integer getDiscountRule() {
        return discountRule;
    }

    public void setDiscountRule(Integer discountRule) {
        this.discountRule = discountRule;
    }

    public Long getDiscountItemId() {
        return discountItemId;
    }

    public void setDiscountItemId(Long discountItemId) {
        this.discountItemId = discountItemId;
    }

    public Long getNeedAmount() {
        return needAmount;
    }

    public void setNeedAmount(Long needAmount) {
        this.needAmount = needAmount;
    }

    public Integer getDiscountType() {
        return discountType;
    }

    public void setDiscountType(Integer discountType) {
        this.discountType = discountType;
    }

    public Long getProdsPrice() {
        return prodsPrice;
    }

    public void setProdsPrice(Long prodsPrice) {
        this.prodsPrice = prodsPrice;
    }

    public Long getDiscount() {
        return discount;
    }

    public void setDiscount(Long discount) {
        this.discount = discount;
    }

    public Long getReduceAmount() {
        return reduceAmount;
    }

    public void setReduceAmount(Long reduceAmount) {
        this.reduceAmount = reduceAmount;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "ChooseDiscountItemVO{" +
                "discountId=" + discountId +
                ", discountName=" + discountName +
                ", discountRule=" + discountRule +
                ", discountItemId=" + discountItemId +
                ", needAmount=" + needAmount +
                ", discountType=" + discountType +
                ", count=" + count +
                ", prodsPrice=" + prodsPrice +
                ", discount=" + discount +
                ", reduceAmount=" + reduceAmount +
                '}';
    }
}
