package com.mall4j.cloud.api.biz.dto.channels;

import lombok.Data;

import java.util.List;

@Data
public class EcMerchantUploadInfo {
    private String reject_reason;
    private List<String> refund_certificates;
}
