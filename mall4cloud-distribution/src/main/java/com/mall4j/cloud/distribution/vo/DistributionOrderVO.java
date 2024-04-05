package com.mall4j.cloud.distribution.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mall4j.cloud.common.serializer.ImgJsonSerializer;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 分销员推广订单对象
 * @author cl
 * @date 2021-08-16 16:05:56
 */
public class DistributionOrderVO {

    @ApiModelProperty(value = "佣金数额")
    private BigDecimal incomeAmount;

    @ApiModelProperty(value = "佣金状态(0待支付,1用户未收货待结算，2收货已结算 -1订单失效)")
    private Integer state;

    @ApiModelProperty(value = "建单时间")
    private Date createTime;

    @ApiModelProperty(value = "关联订单项order_item_id")
    private Long orderItemId;

    @ApiModelProperty(value = "商品图片")
    @JsonSerialize(using = ImgJsonSerializer.class)
    private String pic;

    @ApiModelProperty(value = "商品名称")
    private String spuName;

    @ApiModelProperty(value = "商品id")
    private Long spuId;

    public BigDecimal getIncomeAmount() {
        return incomeAmount;
    }

    public void setIncomeAmount(BigDecimal incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getSpuName() {
        return spuName;
    }

    public void setSpuName(String spuName) {
        this.spuName = spuName;
    }

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    @Override
    public String toString() {
        return "DistributionOrderVO{" +
                "incomeAmount=" + incomeAmount +
                ", state=" + state +
                ", createTime=" + createTime +
                ", orderItemId=" + orderItemId +
                ", pic='" + pic + '\'' +
                ", spuName='" + spuName + '\'' +
                ", spuId='" + spuId + '\'' +
                '}';
    }
}
