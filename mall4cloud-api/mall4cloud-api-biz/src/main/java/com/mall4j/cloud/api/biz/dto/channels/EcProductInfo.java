package com.mall4j.cloud.api.biz.dto.channels;

import lombok.Data;

import java.util.List;

@Data
public class EcProductInfo {
    //商品spuid
    private String product_id;
    //商品skuid
    private String sku_id;
    //sku小图
    private String thumb_img;
    //sku数量
    private Integer sku_cnt;
    //售卖价格（单位：分）
    private Long sale_price;
    //商品标题
    private String title;
    //正在售后/退款流程中的 sku 数量
    private Integer on_aftersale_sku_cnt;
    //完成售后/退款的 sku 数量
    private Integer finish_aftersale_sku_cnt;
    //商品编码
    private String sku_code;
    //市场价格（单位：分）
    private Long market_price;

    //sku属性
    private List<EcAttrInfo> sku_attrs;

    //sku实付价格，取estimate_price和change_price中较小值
    private Long real_price;
    //商品外部spuid
    private Long out_product_id;
    //商品外部skuid
    private Long out_sku_id;
    //是否有优惠金额，非必填，默认为false
    private Boolean is_discounted;
    //优惠后 sku 价格，非必填，is_discounted为 true 时有值
    private Long estimate_price;
    //是否修改过价格，非必填，默认为false
    private Boolean is_change_price;
    //改价后 sku 价格，非必填，is_change_price为 true 时有值
    private Long change_price;

}
