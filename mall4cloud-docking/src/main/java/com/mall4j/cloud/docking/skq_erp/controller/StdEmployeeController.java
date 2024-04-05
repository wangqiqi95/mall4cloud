package com.mall4j.cloud.docking.skq_erp.controller;

import com.mall4j.cloud.api.docking.skq_erp.dto.GetEmployeeInfoDto;
import com.mall4j.cloud.api.docking.skq_erp.feign.StdEmployeeFeignClient;
import com.mall4j.cloud.api.docking.skq_erp.vo.EmployeeInfoVo;
import com.mall4j.cloud.api.docking.skq_erp.vo.StdPageResult;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.docking.skq_erp.service.IStdEmployeeService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * 类描述：员工信息
 *
 * @date 2022/1/10 15:43：03
 */
@Api(tags = "中台-员工信息")
@RestController
public class StdEmployeeController implements StdEmployeeFeignClient {

	@Autowired
	IStdEmployeeService stdEmployeeService;

	/**
	 * 方法描述：调用中台接口获取员工信息
	 *
	 * @param getEmployeeInfoDto
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity<com.mall4j.cloud.api.docking.skq_erp.vo.StdPageResult < com.mall4j.cloud.api.docking.skq_erp.vo.EmployeeInfoVo>>
	 * @date 2022-01-10 15:42:14
	 */
	@Override
	public ServerResponseEntity<StdPageResult<EmployeeInfoVo>> getShopInfo(GetEmployeeInfoDto getEmployeeInfoDto) {
		return stdEmployeeService.getEmployeeInfo(getEmployeeInfoDto);
	}
}
