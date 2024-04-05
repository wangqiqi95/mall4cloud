package com.mall4j.cloud.docking.skq_erp.service;

import com.mall4j.cloud.api.docking.skq_erp.dto.PushProductsDto;
import com.mall4j.cloud.common.response.ServerResponseEntity;

import java.util.List;

public interface IStdProductService {

	/**
	 * 售卖商品推送
	 /api/std/data/service
	 * @param pushProductDtos
	 * @return
	 */
	ServerResponseEntity<String> pushProduct(List<PushProductsDto> pushProductDtos);
}
