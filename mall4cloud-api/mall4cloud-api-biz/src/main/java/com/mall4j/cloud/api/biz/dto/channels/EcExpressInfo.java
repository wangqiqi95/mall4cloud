package com.mall4j.cloud.api.biz.dto.channels;

import lombok.Data;

@Data
public class EcExpressInfo {
    //运费模板ID（先通过获取运费模板接口merchant/getfreighttemplatelist拿到），若deliver_method=1，则不用填写
    private String template_id;
    //商品重量，单位克，若当前运费模版计价方式为[按重量]，则必填
    private Integer weight;
}
