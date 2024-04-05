package com.mall4j.cloud.api.biz.dto.channels;

import lombok.Data;

@Data
public class EcReturnInfo {
    //快递单号
    private String waybill_id;
    //物流公司id
    private String delivery_id;
    //物流公司名称
    private String delivery_name;
}
