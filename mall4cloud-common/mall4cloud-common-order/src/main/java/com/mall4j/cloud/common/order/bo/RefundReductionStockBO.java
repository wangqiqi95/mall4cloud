package com.mall4j.cloud.common.order.bo;

/**
 * 订单退款取消库存
 * @author FrozenWatermelon
 * @date 2020/12/8
 */
public class RefundReductionStockBO {

    private Long spuId;

    private Long skuId;

    private Integer count;

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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "RefundReductionStockBO{" +
                "spuId=" + spuId +
                ", skuId=" + skuId +
                ", count=" + count +
                '}';
    }
}
