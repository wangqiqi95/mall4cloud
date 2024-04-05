package com.mall4j.cloud.api.docking.skq_crm.feign;

import com.mall4j.cloud.api.docking.skq_crm.dto.CrmPageResult;
import com.mall4j.cloud.api.docking.skq_crm.dto.OrderSelectDto;
import com.mall4j.cloud.api.docking.skq_crm.vo.OrderSelectVo;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 类描述：CRM 订单接口
 *
 * @date 2022/1/26 12:35：35
 */
@FeignClient(value = "mall4cloud-docking",contextId = "crm-order")
public interface CrmOrderFeignClient {

	/**
	 * 方法描述：订单查询
	 * @param orderSelectDto
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity<java.util.List<com.mall4j.cloud.api.docking.skq_crm.vo.OrderSelectVo>>
	 * @date 2022-01-26 12:37:36
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/crm/order/get")
	ServerResponseEntity<CrmPageResult<List<OrderSelectVo>>> orderSelect(@RequestBody OrderSelectDto orderSelectDto);


}
