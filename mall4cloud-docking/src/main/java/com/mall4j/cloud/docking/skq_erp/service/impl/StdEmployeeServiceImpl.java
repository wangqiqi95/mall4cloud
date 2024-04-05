package com.mall4j.cloud.docking.skq_erp.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.mall4j.cloud.api.docking.skq_erp.dto.GetEmployeeInfoDto;
import com.mall4j.cloud.api.docking.skq_erp.vo.EmployeeInfoVo;
import com.mall4j.cloud.api.docking.skq_erp.vo.StdPageResult;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.docking.skq_erp.service.IStdEmployeeService;
import com.mall4j.cloud.docking.utils.StdClients;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service("stdEmployeeService")
public class StdEmployeeServiceImpl implements IStdEmployeeService {

	private static final String EMPLOYEE_URI= "/api/ip/std/service";
	private static final String EMPLOYEE_METHOD= "std.universal.employee";

	/**
	 * 方法描述：调用中台接口获取员工信息
	 *
	 * @param getEmployeeInfoDto
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity<com.mall4j.cloud.api.docking.skq_erp.vo.StdPageResult < com.mall4j.cloud.api.docking.skq_erp.vo.EmployeeInfoVo>>
	 * @date 2022-01-10 15:42:14
	 */
	@Override
	public ServerResponseEntity<StdPageResult<EmployeeInfoVo>> getEmployeeInfo(GetEmployeeInfoDto getEmployeeInfoDto) {
		if (null == getEmployeeInfoDto) {
			return ServerResponseEntity.fail(ResponseEnum.DATA_INCOMPLETE);
		}

		String s = StdClients.clients().postStd(EMPLOYEE_URI, EMPLOYEE_METHOD, JSON.toJSONString(getEmployeeInfoDto));
		if (StringUtils.isBlank(s)) {
			ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
			fail.setMsg("调用中台接口获取员工信息接口无响应");
			return fail;
		}

		JSONObject resp = JSON.parseObject(s);
		Boolean isSuccess = resp.getBoolean("isSuccess");
		String errorMsg = resp.getString("errorMsg");
		if (isSuccess != null && isSuccess) {
			String data = resp.getString("data");
			StdPageResult<EmployeeInfoVo> employeeInfoVoStdPageResult = JSONObject.parseObject(data, new TypeReference<StdPageResult<EmployeeInfoVo>>() {
			});
			return ServerResponseEntity.success(employeeInfoVoStdPageResult);
		}
		ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
		fail.setMsg(StringUtils.isBlank(errorMsg) ? "查询员工信息失败" : errorMsg);
		return fail;
	}
}
