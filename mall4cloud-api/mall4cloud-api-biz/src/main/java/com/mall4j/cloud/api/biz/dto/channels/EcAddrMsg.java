package com.mall4j.cloud.api.biz.dto.channels;

import lombok.Data;

@Data
public class EcAddrMsg {
    //地址名称
    private String name;
    //地址行政编码
    private Long code;
    //地址级别 1-省级 2-市级 3-区县级 4-街道
    private Integer level;
}
