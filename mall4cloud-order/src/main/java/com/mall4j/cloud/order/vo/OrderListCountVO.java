package com.mall4j.cloud.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author YXF
 */
@ApiModel("订单列表数量")
public class OrderListCountVO {

    @ApiModelProperty(value = "所有订单数量")
    private Integer allCount;

    @ApiModelProperty(value = "部分订单数量")
    private Integer count;

    public Integer getAllCount() {
        return allCount;
    }

    public void setAllCount(Integer allCount) {
        this.allCount = allCount;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "OrderListCountVO{" +
                "allCount=" + allCount +
                ", count=" + count +
                '}';
    }
}
