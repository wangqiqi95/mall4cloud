package com.mall4j.cloud.api.biz.dto.channels;

import lombok.Data;

import java.util.List;

@Data
public class EcDeliveryProductInfo {
    //快递单号
    private String waybill_id;
    //快递公司编码
    private String delivery_id;
    //包裹中商品信息
    private List<EcFreightProductInfo> product_infos;
    //快递公司名称
    private String delivery_name;
    //发货时间，秒级时间戳
    private Long delivery_time;
    //配送方式，枚举值见DeliveryType
    /**
     * 枚举值	描述
     * 1	自寄快递
     * 2	在线签约快递单
     * 3	虚拟商品无需物流发货
     * 4	在线快递散单
     */
    private Integer deliver_type;
    //发货地址
    private EcAddressInfo delivery_address;
}
