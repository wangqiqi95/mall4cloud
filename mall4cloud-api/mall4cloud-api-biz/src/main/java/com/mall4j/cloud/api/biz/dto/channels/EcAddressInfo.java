package com.mall4j.cloud.api.biz.dto.channels;

import lombok.Data;

@Data
public class EcAddressInfo {
    //收货人姓名
    private String user_name;
    //邮编
    private String postal_code;
    //省份
    private String province_name;
    //城市
    private String city_name;
    //区
    private String county_name;
    //详细地址
    private String detail_info;
    //国家码
    private String national_code;
    //联系方式
    private String tel_number;
    //门牌号码
    private String house_number;
    //虚拟发货订单联系方式(deliver_method=1时返回)
    private String virtual_order_tel_number;
}
