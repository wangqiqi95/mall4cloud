package com.mall4j.cloud.api.biz.dto.livestore.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ShopUploadCertificates {
    /**
     * 外部售后单号
     */
    @JsonProperty("aftersale_id")
    private String aftersaleId;
    /**
     * 协商文本内容
     */
    @JsonProperty("refund_desc")
    private String refundDesc;
    /**
     * 凭证图片列表
     */
    @JsonProperty("certificates")
    private List<String> certificates;
}
