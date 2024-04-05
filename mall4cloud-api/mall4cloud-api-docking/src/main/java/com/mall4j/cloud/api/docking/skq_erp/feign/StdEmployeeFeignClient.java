package com.mall4j.cloud.api.docking.skq_erp.feign;

import com.mall4j.cloud.api.docking.skq_erp.dto.GetEmployeeInfoDto;
import com.mall4j.cloud.api.docking.skq_erp.vo.EmployeeInfoVo;
import com.mall4j.cloud.api.docking.skq_erp.vo.StdPageResult;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 类描述：员工信息
 *
 * @date 2022/1/9 11:27：25
 */
@FeignClient(value = "mall4cloud-docking",contextId = "std-employee")
public interface StdEmployeeFeignClient {

	/**
	 * 方法描述：调用中台接口获取员工信息
	 * @param getEmployeeInfoDto
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity<com.mall4j.cloud.api.docking.skq_erp.vo.StdPageResult<com.mall4j.cloud.api.docking.skq_erp.vo.EmployeeInfoVo>>
	 * @date 2022-01-10 15:42:14
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/std/shop/getEmployeeInfo")
	ServerResponseEntity<StdPageResult<EmployeeInfoVo>> getShopInfo(@RequestBody GetEmployeeInfoDto getEmployeeInfoDto);
}
