package com.mall4j.cloud.api.biz.dto.channels;

import lombok.Data;

@Data
public class EcPriceInfo {

    //商品总价，单位为分
    private Long product_price;
    //订单金额，单位为分
    private Long order_price;
    //运费，单位为分
    private Long freight;
    //优惠金额，单位为分
    private Long discounted_price;
    //是否有优惠
    private Boolean is_discounted;
    //订单原始价格，单位为分
    private Long original_order_price;
    //商品预估价格，单位为分
    private Long estimate_product_price;
    //改价后降低金额，单位为分
    private Long change_down_price;
    //改价后运费，单位为分
    private Long change_freight;
    //是否修改运费
    private Boolean is_change_freight;

}
