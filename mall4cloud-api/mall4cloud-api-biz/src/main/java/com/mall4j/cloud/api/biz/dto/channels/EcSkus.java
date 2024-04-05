package com.mall4j.cloud.api.biz.dto.channels;

import lombok.Data;

import java.util.List;

@Data
public class EcSkus {
    //若填了已存在sku_id，则进行更新 sku 操作，否则新增sku
    private String sku_id;
    //商家自定义sku_id，小店后台不作任何唯一性约束，开发者自行保证，一旦添加成功后该字段无法修改，最多32字符
    private String out_sku_id;
    //sku小图
    private String thumb_img;
    //售卖价格，以分为单位，不超过1000000000（1000万元）
    private Long sale_price;
    //库存
    private Integer stock_num;
    //sku编码，最多20字符
    private String sku_code;
    //销售属性，每个 spu 下面的第一个 sku 的sku_attr顺序决定这个 spu 在商品详情页的排序
    private List<EcAttrInfo> sku_attrs;

}
