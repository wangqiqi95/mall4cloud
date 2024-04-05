package com.mall4j.cloud.docking.skq_crm.service;

import com.mall4j.cloud.api.docking.skq_crm.dto.CrmPageResult;
import com.mall4j.cloud.api.docking.skq_crm.dto.OrderSelectDto;
import com.mall4j.cloud.api.docking.skq_crm.vo.OrderSelectVo;
import com.mall4j.cloud.common.response.ServerResponseEntity;

import java.util.List;

public interface ICrmOrderService {

	ServerResponseEntity<CrmPageResult<List<OrderSelectVo>>> orderSelect(OrderSelectDto orderSelectDto);
}
