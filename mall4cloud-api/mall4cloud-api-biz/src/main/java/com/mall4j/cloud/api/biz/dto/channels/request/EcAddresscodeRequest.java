package com.mall4j.cloud.api.biz.dto.channels.request;

import lombok.Data;

@Data
public class EcAddresscodeRequest {
    //地址行政编码，不填或者填0时，拉取全国的省级行政编码
    private String addr_code;
}
