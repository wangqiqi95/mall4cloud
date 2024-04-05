package com.mall4j.cloud.api.biz.dto.channels.request;

import com.mall4j.cloud.api.biz.dto.channels.EcAddressInfo;
import lombok.Data;

@Data
public class EcOrderUpdateAddressRequest {
    //订单id
    private String order_id;
    //新地址
    private EcAddressInfo user_address;
}
