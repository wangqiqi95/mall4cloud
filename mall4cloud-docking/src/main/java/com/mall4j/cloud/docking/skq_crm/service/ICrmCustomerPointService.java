package com.mall4j.cloud.docking.skq_crm.service;

import com.mall4j.cloud.api.docking.skq_crm.dto.CrmPageResult;
import com.mall4j.cloud.api.docking.skq_crm.dto.PointChangeDto;
import com.mall4j.cloud.api.docking.skq_crm.dto.PointDetailGetDto;
import com.mall4j.cloud.api.docking.skq_crm.dto.SyncPointConvertDataDto;
import com.mall4j.cloud.api.docking.skq_crm.vo.PointDetailVo;
import com.mall4j.cloud.common.response.ServerResponseEntity;

import java.util.List;

public interface ICrmCustomerPointService {

	ServerResponseEntity<CrmPageResult<List<PointDetailVo>>> pointDetailGet(PointDetailGetDto pointDetailGetDto);

	ServerResponseEntity pointChange(PointChangeDto pointChangeDto);

	/**
	 * 积分兑换数据同步
	 * @param dto
	 * @return
	 */
	ServerResponseEntity syncPointConvertData(List<SyncPointConvertDataDto> dto);
}
