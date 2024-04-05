package com.mall4j.cloud.docking.skq_erp.service;

import com.mall4j.cloud.api.docking.skq_erp.dto.GetEmployeeInfoDto;
import com.mall4j.cloud.api.docking.skq_erp.vo.EmployeeInfoVo;
import com.mall4j.cloud.api.docking.skq_erp.vo.StdPageResult;
import com.mall4j.cloud.common.response.ServerResponseEntity;

/**
 * 类描述：员工信息
 *
 * @date 2022/1/10 15:44：20
 */
public interface IStdEmployeeService {

	/**
	 * 方法描述：调用中台接口获取员工信息
	 *
	 * @param getEmployeeInfoDto
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity<com.mall4j.cloud.api.docking.skq_erp.vo.StdPageResult < com.mall4j.cloud.api.docking.skq_erp.vo.EmployeeInfoVo>>
	 * @date 2022-01-10 15:42:14
	 */
	ServerResponseEntity<StdPageResult<EmployeeInfoVo>> getEmployeeInfo(GetEmployeeInfoDto getEmployeeInfoDto);
}
