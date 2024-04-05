package com.mall4j.cloud.docking.skq_crm.controller;

import com.mall4j.cloud.api.docking.skq_crm.dto.CrmPageResult;
import com.mall4j.cloud.api.docking.skq_crm.dto.OrderSelectDto;
import com.mall4j.cloud.api.docking.skq_crm.feign.CrmOrderFeignClient;
import com.mall4j.cloud.api.docking.skq_crm.vo.OrderSelectVo;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.docking.skq_crm.service.ICrmOrderService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "CRM-全渠道订单")
public class CrmOrderController implements CrmOrderFeignClient {

	@Autowired
	ICrmOrderService crmOrderService;

	/**
	 * 方法描述：订单查询
	 *
	 * @param orderSelectDto
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity<java.util.List < com.mall4j.cloud.api.docking.skq_crm.vo.OrderSelectVo>>
	 * @date 2022-01-26 12:37:36
	 */
	@Override
	public ServerResponseEntity<CrmPageResult<List<OrderSelectVo>>> orderSelect(OrderSelectDto orderSelectDto) {
		return crmOrderService.orderSelect(orderSelectDto);
	}
}
