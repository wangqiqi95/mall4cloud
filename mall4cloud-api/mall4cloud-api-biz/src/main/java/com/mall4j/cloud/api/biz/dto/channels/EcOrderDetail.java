package com.mall4j.cloud.api.biz.dto.channels;

import lombok.Data;

import java.util.List;

@Data
public class EcOrderDetail {
    //商品列表
    private List<EcProductInfo> product_infos;
    //价格信息
    private EcPriceInfo price_info;
    //	支付信息
    private EcPayInfo pay_info;
    //配送信息
    private EcDeliveryInfo delivery_info;
    //优惠券信息
    private EcCouponInfo coupon_info;
    //额外信息
    private EcExtInfo ext_info;
    //分享员信息
    private EcSharerInfo sharer_info;
    //分佣信息
    private List<EcCommissionInfo> commission_infos;
}
