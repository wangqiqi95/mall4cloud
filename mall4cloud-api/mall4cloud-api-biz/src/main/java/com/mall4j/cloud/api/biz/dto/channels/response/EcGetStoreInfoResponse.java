package com.mall4j.cloud.api.biz.dto.channels.response;

import com.mall4j.cloud.api.biz.dto.channels.EcStore;
import lombok.Data;

@Data
public class EcGetStoreInfoResponse extends EcBaseResponse {
    private EcStore info;
}
