package com.mall4j.cloud.distribution.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mall4j.cloud.common.order.vo.OrderItemVO;
import com.mall4j.cloud.common.serializer.ImgJsonSerializer;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 分销员推广订单对象
 * @author Citrus
 * @date 2021/8/19 15:27
 */
public class DistributionUserIncomeOrderVO {

    @ApiModelProperty("订单id")
    private Long orderId;

    @ApiModelProperty("收入金额")
    private Double incomeAmount;

    @ApiModelProperty("实际支付")
    private Long actualTotal;

    @ApiModelProperty("订单状态(0:待支付 1:待结算 2:已结算 -1:订单失效)")
    private Integer state;

    @ApiModelProperty("订单项id")
    private Long orderItemId;

    @ApiModelProperty("商品名称")
    private String spuName;

    @ApiModelProperty("商品价格")
    private Long price;

    @ApiModelProperty("商品图片")
    @JsonSerialize(using = ImgJsonSerializer.class)
    private String pic;

    @ApiModelProperty("商品数量")
    private Integer spuCount;

    @ApiModelProperty("规格名称")
    private String skuName;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Double getIncomeAmount() {
        return incomeAmount;
    }

    public void setIncomeAmount(Double incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    public Long getActualTotal() {
        return actualTotal;
    }

    public void setActualTotal(Long actualTotal) {
        this.actualTotal = actualTotal;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getSpuName() {
        return spuName;
    }

    public void setSpuName(String spuName) {
        this.spuName = spuName;
    }

    public Integer getSpuCount() {
        return spuCount;
    }

    public void setSpuCount(Integer spuCount) {
        this.spuCount = spuCount;
    }

    @Override
    public String toString() {
        return "DistributionUserIncomeOrderVO{" +
                "orderId=" + orderId +
                ", incomeAmount=" + incomeAmount +
                ", actualTotal=" + actualTotal +
                ", state=" + state +
                ", orderItemId=" + orderItemId +
                ", spuName='" + spuName + '\'' +
                ", price=" + price +
                ", pic='" + pic + '\'' +
                ", spuCount=" + spuCount +
                ", skuName='" + skuName + '\'' +
                '}';
    }
}
