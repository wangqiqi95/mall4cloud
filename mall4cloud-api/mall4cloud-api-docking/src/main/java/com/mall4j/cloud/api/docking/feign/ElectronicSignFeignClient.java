package com.mall4j.cloud.api.docking.feign;

import com.mall4j.cloud.api.docking.dto.BatchResultsDto;
import com.mall4j.cloud.api.docking.dto.ElectronicSignDto;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "mall4cloud-docking",contextId = "electronic-sign")
public interface ElectronicSignFeignClient {

	/**
	 * 调用BatchInsertItems批量新增或修改商品
	 * @param electronicSignDto 请求参数dto
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/electronicSign/batchInsertItems")
	ServerResponseEntity<List<BatchResultsDto>>  batchInsertItems(@RequestBody ElectronicSignDto electronicSignDto);
}
