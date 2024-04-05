package com.mall4j.cloud.api.biz.dto.channels;

import lombok.Data;

import java.util.List;

@Data
public class EcCatAttr {
    //是否支持虚拟发货
    private Boolean shop_no_shipment;
    //是否定向准入
    private Boolean access_permit_required;
    //是否支持预售
    private Boolean pre_sale;
    //	类目关联的保证金，单位分
    private Long deposit;
    //定准类目的品牌
    private List<EcBrand> brand_list;

    //产品属性
    private List<EcCatAttrInfo> product_attr_list;
    //销售属性
    private List<EcCatAttrInfo> sale_attr_list;
}
