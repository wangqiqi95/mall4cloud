package com.mall4j.cloud.common.order.vo;

import com.mall4j.cloud.common.order.vo.OrderItemLangVO;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 退款订单项
 * @author FrozenWatermelon
 */
public class RefundOrderItemVO {

    @ApiModelProperty("订单项目id")
    private Long orderItemId;

    @ApiModelProperty("订单项目id")
    private Long spuId;

    @ApiModelProperty("订单项目id")
    private Long skuId;

    @ApiModelProperty("产品名称")
    private String spuName;

    @ApiModelProperty("sku名称")
    private String skuName;

    @ApiModelProperty("产品图片")
    private String pic;

    @ApiModelProperty("物品数量")
    private Integer count;

    @ApiModelProperty("产品价格")
    private Long price;

    @ApiModelProperty("产品总价格")
    private Long spuTotalAmount;

    @ApiModelProperty("商品实际金额")
    private Long actualTotal;

    @ApiModelProperty("订单级别，运费金额")
    private Long freightAmount;

    public Long getFreightAmount() {
        return freightAmount;
    }

    public void setFreightAmount(Long freightAmount) {
        this.freightAmount = freightAmount;
    }

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    private List<OrderItemLangVO> orderItemLangList;

    public String getSpuName() {
        return spuName;
    }

    public void setSpuName(String spuName) {
        this.spuName = spuName;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
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

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public List<OrderItemLangVO> getOrderItemLangList() {
        return orderItemLangList;
    }

    public void setOrderItemLangList(List<OrderItemLangVO> orderItemLangList) {
        this.orderItemLangList = orderItemLangList;
    }

    @Override
    public String toString() {
        return "RefundOrderItemVO{" +
                "orderItemId=" + orderItemId +
                ", spuName='" + spuName + '\'' +
                ", skuName='" + skuName + '\'' +
                ", pic='" + pic + '\'' +
                ", count=" + count +
                ", price=" + price +
                ", spuTotalAmount=" + spuTotalAmount +
                ", actualTotal=" + actualTotal +
                ", orderItemLangList=" + orderItemLangList +
                '}';
    }
}
