package com.mall4j.cloud.api.delivery.feign;

import com.mall4j.cloud.api.delivery.dto.DeliveryCompanyDTO;
import com.mall4j.cloud.api.delivery.vo.DeliveryCompanyVO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author FrozenWatermelon
 * @date 2020/12/14
 */
@FeignClient(value = "mall4cloud-delivery",contextId ="delivery-company")
public interface DeliveryCompanyFeignClient {
	/**
	 * 根据快递公司id获取快递公司信息
	 * @param companyId 快递公司id
	 * @return
	 */
	@GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/delivery/getByDeliveryCompanyByCompanyId")
	ServerResponseEntity<String> getByDeliveryCompanyByCompanyId(@RequestParam("companyId") Long companyId);

	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/delivery/listBySearch")
	ServerResponseEntity<List<DeliveryCompanyVO>> listBySearch(@RequestBody DeliveryCompanyDTO deliveryCompanyDTO);
}
