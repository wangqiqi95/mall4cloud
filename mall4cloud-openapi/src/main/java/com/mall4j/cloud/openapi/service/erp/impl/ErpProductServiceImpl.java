package com.mall4j.cloud.openapi.service.erp.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mall4j.cloud.api.openapi.skq_crm.response.CrmResponseEnum;
import com.mall4j.cloud.api.openapi.skq_erp.dto.ProductPriceSyncDto;
import com.mall4j.cloud.api.openapi.skq_erp.response.ErpResponse;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.openapi.service.erp.ErpProductService;

@Service("erpProductService")
public class ErpProductServiceImpl implements ErpProductService {

	private final Logger logger = LoggerFactory.getLogger(ErpProductServiceImpl.class);

	@Override
	@Deprecated
	public ErpResponse productPriceSync(ProductPriceSyncDto productPriceSyncDto) {
		long start = System.currentTimeMillis();
		ErpResponse erpResponse = ErpResponse.fail();
		ServerResponseEntity responseEntity = null;
		try {
			if (productPriceSyncDto == null) {
				erpResponse = ErpResponse.fail(CrmResponseEnum.HTTP_MESSAGE_NOT_READABLE.value(), "请求参数为空");
				return erpResponse;
			}
			return erpResponse;
		} finally {
			logger.info("商品价格同步（门店版）,接收到请求参数：{}，feign调用响应为：{},返回响应为：{}，共耗时：{}", productPriceSyncDto, responseEntity, erpResponse, System.currentTimeMillis() - start);
		}
	}
}
