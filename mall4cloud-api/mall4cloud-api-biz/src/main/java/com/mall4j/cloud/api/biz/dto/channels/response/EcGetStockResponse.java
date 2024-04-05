package com.mall4j.cloud.api.biz.dto.channels.response;

import com.mall4j.cloud.api.biz.dto.channels.EcSkuStocks;
import lombok.Data;

@Data
public class EcGetStockResponse extends EcBaseResponse {
    private EcSkuStocks data;
}
