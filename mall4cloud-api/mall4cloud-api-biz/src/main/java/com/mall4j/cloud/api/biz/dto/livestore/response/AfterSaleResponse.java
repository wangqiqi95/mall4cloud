package com.mall4j.cloud.api.biz.dto.livestore.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import lombok.Data;

/**
 * 退款售后
 * @author oy
 */
@Data
public class AfterSaleResponse extends BaseResponse {

    /**
     * 接收数据
     */
    @JsonProperty("after_sales_order")
    private AfterSalesOrder afterSalesOrder;
}
