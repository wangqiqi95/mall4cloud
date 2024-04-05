package com.mall4j.cloud.common.order.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mall4j.cloud.common.serializer.ImgJsonSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author FrozenWatermelon
 * @date 2021/2/5
 */
@Data
public class EsOrderItemVO {
    @ApiModelProperty(value = "商品图片", required = true)
    @JsonSerialize(using = ImgJsonSerializer.class)
    private String pic;

    @ApiModelProperty(value = "商品名称", required = true)
    private String spuName;

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

    @ApiModelProperty(value = "退款状态 1.买家申请 2.卖家接受 3.买家发货 4.卖家收货 5.退款成功  -1.退款关闭")
    private Integer returnMoneySts;

    /**
     * 积分价格（单价）
     */
    private Long scoreFee;

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 用户Id
     */
    private Long userId;

    /**
     * 最终的退款id
     */
    private Long finalRefundId;

    /**
     * 推广员id
     */
    private Long distributionUserId;

    /**
     * 是否以评价(0.未评价1.已评价)
     */
    private Integer isComm;

    /**
     * 评论时间
     */
    private Date commTime;

    /**
     * 订单项退款状态（1:申请退款 2:退款成功 3:部分退款成功 4:退款失败）
     */
    private Integer refundStatus;

    /**
     * 0全部发货 其他数量为剩余待发货数量
     */
    private Integer beDeliveredNum;

    /**
     * 单个orderItem的配送类型 1:快递 2:自提 3：无需快递 4:同城配送
     */
    private Integer deliveryType;

    /**
     * 加入购物车时间
     */
    private Date shopCartTime;

    /**
     * 商品总金额
     */
    private Long spuTotalAmount;

    /**
     * 商品实际金额 = 商品总金额 - 分摊的优惠金额
     */
    private Long actualTotal;

    /**
     * 分摊的优惠金额
     */
    private Long shareReduce;

    /**
     * 平台优惠金额
     */
    private Long platformShareReduce;

    /**
     * 推广员佣金
     */
    private Long distributionAmount;

    /**
     * 上级推广员佣金
     */
    private Long distributionParentAmount;

    /**
     * 获得积分
     */
    private Long gainScore;

    private Integer type;

    /**
     * 订单商品名、sku名
     */
    private List<OrderItemLangVO> orderItemLangList;

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

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getFinalRefundId() {
        return finalRefundId;
    }

    public void setFinalRefundId(Long finalRefundId) {
        this.finalRefundId = finalRefundId;
    }

    public Long getDistributionUserId() {
        return distributionUserId;
    }

    public void setDistributionUserId(Long distributionUserId) {
        this.distributionUserId = distributionUserId;
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

    public Integer getBeDeliveredNum() {
        return beDeliveredNum;
    }

    public void setBeDeliveredNum(Integer beDeliveredNum) {
        this.beDeliveredNum = beDeliveredNum;
    }

    public Integer getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(Integer deliveryType) {
        this.deliveryType = deliveryType;
    }

    public Date getShopCartTime() {
        return shopCartTime;
    }

    public void setShopCartTime(Date shopCartTime) {
        this.shopCartTime = shopCartTime;
    }

    public Long getSpuTotalAmount() {
        return spuTotalAmount;
    }

    public void setSpuTotalAmount(Long spuTotalAmount) {
        this.spuTotalAmount = spuTotalAmount;
    }

    public Long getActualTotal() {
        return actualTotal;
    }

    public void setActualTotal(Long actualTotal) {
        this.actualTotal = actualTotal;
    }

    public Long getShareReduce() {
        return shareReduce;
    }

    public void setShareReduce(Long shareReduce) {
        this.shareReduce = shareReduce;
    }

    public Long getPlatformShareReduce() {
        return platformShareReduce;
    }

    public void setPlatformShareReduce(Long platformShareReduce) {
        this.platformShareReduce = platformShareReduce;
    }

    public Long getDistributionAmount() {
        return distributionAmount;
    }

    public void setDistributionAmount(Long distributionAmount) {
        this.distributionAmount = distributionAmount;
    }

    public Long getDistributionParentAmount() {
        return distributionParentAmount;
    }

    public void setDistributionParentAmount(Long distributionParentAmount) {
        this.distributionParentAmount = distributionParentAmount;
    }

    public Long getGainScore() {
        return gainScore;
    }

    public void setGainScore(Long gainScore) {
        this.gainScore = gainScore;
    }

    public List<OrderItemLangVO> getOrderItemLangList() {
        return orderItemLangList;
    }

    public void setOrderItemLangList(List<OrderItemLangVO> orderItemLangList) {
        this.orderItemLangList = orderItemLangList;
    }

    public Long getScoreFee() {
        return scoreFee;
    }

    public void setScoreFee(Long scoreFee) {
        this.scoreFee = scoreFee;
    }

    @Override
    public String toString() {
        return "EsOrderItemVO{" +
                "pic='" + pic + '\'' +
                ", spuName='" + spuName + '\'' +
                ", count=" + count +
                ", price=" + price +
                ", useScore=" + useScore +
                ", skuId=" + skuId +
                ", skuName='" + skuName + '\'' +
                ", orderItemId=" + orderItemId +
                ", spuId=" + spuId +
                ", returnMoneySts=" + returnMoneySts +
                ", shopId=" + shopId +
                ", userId=" + userId +
                ", finalRefundId=" + finalRefundId +
                ", distributionUserId=" + distributionUserId +
                ", scoreFee=" + scoreFee +
                ", isComm=" + isComm +
                ", commTime=" + commTime +
                ", refundStatus=" + refundStatus +
                ", beDeliveredNum=" + beDeliveredNum +
                ", deliveryType=" + deliveryType +
                ", shopCartTime=" + shopCartTime +
                ", spuTotalAmount=" + spuTotalAmount +
                ", actualTotal=" + actualTotal +
                ", shareReduce=" + shareReduce +
                ", platformShareReduce=" + platformShareReduce +
                ", distributionAmount=" + distributionAmount +
                ", distributionParentAmount=" + distributionParentAmount +
                ", gainScore=" + gainScore +
                ", orderItemLangList=" + orderItemLangList +
                '}';
    }
}
