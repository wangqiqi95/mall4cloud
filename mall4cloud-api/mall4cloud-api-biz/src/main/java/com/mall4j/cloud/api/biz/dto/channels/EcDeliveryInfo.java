package com.mall4j.cloud.api.biz.dto.channels;

import lombok.Data;

import java.util.List;

@Data
public class EcDeliveryInfo {
    //地址信息
    private EcAddressInfo address_info;
    //发货物流信息
    private List<EcDeliveryProductInfo> delivery_product_info;
    //发货完成时间，秒级时间戳
    private Long ship_done_time;
    //订单发货方式，0：普通物流；1：虚拟发货，由商品的同名字段决定
    private Integer deliver_method;
}
