package com.mall4j.cloud.api.biz.dto.channels;

import lombok.Data;

@Data
public class EcOrder {

    //订单号
    private String order_id;
    /**
     * 订单状态
     * 枚举值	描述
     * 10	待付款
     * 20	待发货
     * 21	部分发货
     * 30	待收货
     * 100	完成
     * 200	全部商品售后之后，订单取消
     * 250	未付款用户主动取消或超时未付款订单自动取消
     */
    private Integer status;
    //秒级时间戳
    private Long create_time;
    //秒级时间戳
    private Long update_time;
    //买家身份标识
    private String openid;

    private String unionid;

    //售后信息
    private EcAftersaleDetail aftersale_detail;

    //订单详细数据信息
    private EcOrderDetail order_detail;


}
