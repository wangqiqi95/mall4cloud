package com.mall4j.cloud.api.biz.dto.livestore.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OrderAddRequest {

    /**
     * 创建时间，yyyy-MM-dd HH:mm:ss，与微信服务器不得超过5秒偏差
     */
    @JsonProperty("create_time")
    private String createTime;
    /**
     * 商家自定义订单ID(字符集包括大小写字母数字，长度小于128个字符）
     */
    @JsonProperty("out_order_id")
    private String outOrderId;
    /**
     * 用户的openid
     */
    @JsonProperty("openid")
    private String openid;
    /**
     * 订单详情页路径
     */
    @JsonProperty("path")
    private String path;

    /**
     * 订单类型：0，普通单，1，二级商户单
     */
    @JsonProperty("fund_type")
    private Integer fundType;
    /**
     * unix秒级时间戳，订单超时时间，取值：[15min, 1d]
     */
    @JsonProperty("expire_time")
    private long expireTime;
    /**
     * 会影响主播归因、分享员归因等，从下单前置检查获取
     */
    @JsonProperty("trace_id")
    private String traceId;
    /**
     *
     * 取值范围，[7，3 * 365]，单位：天 确认收货后 x 天完成结算，结算后订单不可再发起售后
     */
    @JsonProperty("aftersale_duration")
    private Integer aftersale_duration;

    @JsonProperty("out_user_id")
    private String outUserId;

    /**
     * 订单详细数据
     */
    @JsonProperty("order_detail")
    private OrderDetailInfo orderDetail;

    @JsonProperty("delivery_detail")
    private DeliveryDetail deliveryDetail;
    @JsonProperty("address_info")
    private AddressInfo addressInfo;

//    @JsonProperty("default_receiving_address")
//    private String defaultReceivingAddress;

}
