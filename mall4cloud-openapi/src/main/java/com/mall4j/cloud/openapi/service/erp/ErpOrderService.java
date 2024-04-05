package com.mall4j.cloud.openapi.service.erp;

import org.springframework.web.bind.annotation.RequestBody;

import com.mall4j.cloud.api.openapi.skq_erp.dto.OrderDeliveryDto;
import com.mall4j.cloud.api.openapi.skq_erp.response.ErpResponse;
import com.mall4j.cloud.api.order.dto.OrderRefundDto;

public interface ErpOrderService {
	ErpResponse orderDelivery(OrderDeliveryDto orderDeliveryDto);

	ErpResponse orderReturnstatus(OrderRefundDto orderRefundParam);
}
