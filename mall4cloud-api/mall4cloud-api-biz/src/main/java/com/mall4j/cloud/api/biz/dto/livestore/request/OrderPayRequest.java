
package com.mall4j.cloud.api.biz.dto.livestore.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OrderPayRequest {

    /**
     * 类型，默认1:支付成功,2:支付失败,3:用户取消,4:超时未支付;5:商家取消;10:其他原因取消
     */
    @JsonProperty("action_type")
    private Long actionType;
    /**
     * 用户的openid
     */
    private String openid;
    /**
     * 订单ID
     */
    @JsonProperty("order_id")
    private Long orderId;
    /**
     * 商家自定义订单ID，与 order_id 二选一
     */
    @JsonProperty("out_order_id")
    private String outOrderId;
    /**
     * 支付完成时间，action_type=1时必填，yyyy-MM-dd HH:mm:ss
     */
    @JsonProperty("pay_time")
    private String payTime;
    /**
     * 支付订单号，action_type=1且order/add时传的pay_method_type=0时必填
     */
    @JsonProperty("transaction_id")
    private String transactionId;


}
