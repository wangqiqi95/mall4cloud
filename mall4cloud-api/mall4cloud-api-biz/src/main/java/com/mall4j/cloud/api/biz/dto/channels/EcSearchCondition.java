package com.mall4j.cloud.api.biz.dto.channels;

import lombok.Data;

@Data
public class EcSearchCondition {
    //商品标题关键词
    private String title;
    //商品编码
    private String sku_code;
    //收件人
    private String user_name;
    //收件人电话
    private String tel_number;
    //选填，只搜一个订单时使用
    private String order_id;
    //商家备注
    private String merchant_notes;
    //买家备注
    private String customer_notes;
}
