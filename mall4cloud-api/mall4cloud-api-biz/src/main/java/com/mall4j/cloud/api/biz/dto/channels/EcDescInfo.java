package com.mall4j.cloud.api.biz.dto.channels;

import lombok.Data;

import java.util.List;

@Data
public class EcDescInfo {
    private List<String> imgs;
    //商品详情文本
    private String desc;
}
