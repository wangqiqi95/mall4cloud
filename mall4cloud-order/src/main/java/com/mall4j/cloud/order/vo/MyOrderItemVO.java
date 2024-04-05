package com.mall4j.cloud.order.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mall4j.cloud.common.serializer.ImgJsonSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @author FrozenWatermelon
 */
@ApiModel("我的订单-订单项")
public class MyOrderItemVO {

    @ApiModelProperty(value = "商品图片", required = true)
    @JsonSerialize(using = ImgJsonSerializer.class)
    private String pic;

    @ApiModelProperty(value = "商品名称", required = true)
    private String spuName;

    @ApiModelProperty(value = "订单号",required=true)
    private Long orderId;

    @ApiModelProperty(value = "商品数量", required = true)
    private Integer count;

    @ApiModelProperty(value = "商品价格", required = true)
    private Long price;

    @ApiModelProperty(value = "产品购买花费积分",required=true)
    private Long useScore;

    @ApiModelProperty(value = "skuId", required = true)
    private Long skuId;

    @ApiModelProperty(value = "skuName", required = true)
    private String skuName;

    @ApiModelProperty(value = "订单项id", required = true)
    private Long orderItemId;

    @ApiModelProperty(value = "商品id", required = true)
    private Long spuId;

    @ApiModelProperty(value = "评论状态： 0 未评价  1 已评价", required = true)
    private Integer isComm;

    @ApiModelProperty(value = "评论时间", required = true)
    private Date commTime;

    @ApiModelProperty(value = "退款状态 1.买家申请 2.卖家接受 3.买家发货 4.卖家收货 5.退款成功  -1.退款关闭")
    private Integer returnMoneySts;

    @ApiModelProperty(value = "订单项退款状态（1:申请退款 2:退款成功 3:部分退款成功 4:退款失败）")
    private Integer refundStatus;

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

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getUseScore() {
        return useScore;
    }

    public void setUseScore(Long useScore) {
        this.useScore = useScore;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public Integer getReturnMoneySts() {
        return returnMoneySts;
    }

    public void setReturnMoneySts(Integer returnMoneySts) {
        this.returnMoneySts = returnMoneySts;
    }

    public Integer getIsComm() {
        return isComm;
    }

    public void setIsComm(Integer isComm) {
        this.isComm = isComm;
    }

    public Date getCommTime() {
        return commTime;
    }

    public void setCommTime(Date commTime) {
        this.commTime = commTime;
    }

    public Integer getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(Integer refundStatus) {
        this.refundStatus = refundStatus;
    }

    @Override
    public String toString() {
        return "MyOrderItemVO{" +
                "pic='" + pic + '\'' +
                ", spuName='" + spuName + '\'' +
                ", orderId=" + orderId +
                ", count=" + count +
                ", price=" + price +
                ", useScore=" + useScore +
                ", skuId=" + skuId +
                ", skuName='" + skuName + '\'' +
                ", orderItemId=" + orderItemId +
                ", spuId=" + spuId +
                ", isComm=" + isComm +
                ", commTime=" + commTime +
                ", returnMoneySts=" + returnMoneySts +
                ", refundStatus=" + refundStatus +
                '}';
    }
}
