package com.mall4j.cloud.openapi.service.erp;

import org.springframework.web.bind.annotation.RequestBody;

import com.mall4j.cloud.api.openapi.skq_erp.dto.ProductPriceSyncDto;
import com.mall4j.cloud.api.openapi.skq_erp.response.ErpResponse;

public interface ErpProductService {

	ErpResponse productPriceSync(ProductPriceSyncDto productPriceSyncDto);
}
