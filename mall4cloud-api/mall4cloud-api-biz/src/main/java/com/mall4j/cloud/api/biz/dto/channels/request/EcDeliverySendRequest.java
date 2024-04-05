package com.mall4j.cloud.api.biz.dto.channels.request;

import com.mall4j.cloud.api.biz.dto.channels.EcDeliveryProductInfo;
import lombok.Data;

import java.util.List;

@Data
public class EcDeliverySendRequest {
    //小程序端订单id
    private Long out_order_id;
    //微信端订单id
    private Long order_id;
    //物流信息
    private List<EcDeliveryProductInfo> delivery_list;
}
