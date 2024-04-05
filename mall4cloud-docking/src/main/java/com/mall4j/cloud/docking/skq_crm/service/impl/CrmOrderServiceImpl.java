package com.mall4j.cloud.docking.skq_crm.service.impl;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.mall4j.cloud.api.docking.skq_crm.dto.CrmPageResult;
import com.mall4j.cloud.api.docking.skq_crm.dto.OrderSelectDto;
import com.mall4j.cloud.api.docking.skq_crm.enums.CrmMethod;
import com.mall4j.cloud.api.docking.skq_crm.vo.OrderSelectVo;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.docking.skq_crm.service.ICrmOrderService;
import com.mall4j.cloud.docking.utils.CrmClients;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("crmOrderService")
public class CrmOrderServiceImpl implements ICrmOrderService {
	@Override
	public ServerResponseEntity<CrmPageResult<List<OrderSelectVo>>> orderSelect(OrderSelectDto orderSelectDto) {
		if (null == orderSelectDto) {
			return ServerResponseEntity.fail(ResponseEnum.DATA_INCOMPLETE);
		}
		Map<String, Object> stringObjectMap = orderSelectDto.toMap();
		String result = CrmClients.clients().get(CrmMethod.ORDER_SELECT.uri(), stringObjectMap);

		if (StringUtils.isBlank(result)) {
			ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
			fail.setMsg("调用CRM-订单查询接口无响应");
			return fail;
		}
		CrmPageResult<List<OrderSelectVo>> crmPageResult = JSONUtil.toBean(result, new TypeReference<CrmPageResult<List<OrderSelectVo>>>() {
		}, true);

		String errorMsg = "订单查询失败";
		if (crmPageResult != null) {
			if (crmPageResult.success()) {
				return ServerResponseEntity.success(crmPageResult);
			}
			errorMsg = crmPageResult.getMessage();
		}
		ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
		fail.setMsg(errorMsg);
		return fail;
	}
}
