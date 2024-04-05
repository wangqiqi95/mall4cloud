package com.mall4j.cloud.docking.skq_crm.controller;

import com.mall4j.cloud.api.docking.skq_crm.dto.CrmPageResult;
import com.mall4j.cloud.api.docking.skq_crm.dto.PointChangeDto;
import com.mall4j.cloud.api.docking.skq_crm.dto.PointDetailGetDto;
import com.mall4j.cloud.api.docking.skq_crm.dto.SyncPointConvertDataDto;
import com.mall4j.cloud.api.docking.skq_crm.feign.CrmCustomerPointFeignClient;
import com.mall4j.cloud.api.docking.skq_crm.vo.PointDetailVo;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.docking.skq_crm.service.ICrmCustomerPointService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@Api(tags = "crm-会员积分接口")
public class CrmCustomerPointController implements CrmCustomerPointFeignClient {

	@Autowired
	ICrmCustomerPointService crmCustomerPointService;
	/**
	 * 方法描述：会员积分明细查询
	 *
	 * @param pointDetailGetDto
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity<java.util.List < com.mall4j.cloud.api.docking.skq_crm.vo.PointDetailVo>>
	 * @date 2022-01-24 09:45:01
	 */
	@Override
	@ApiOperation(value = "会员积分明细查询接口", notes = "调用CRM-会员积分明细查询接口")
	public ServerResponseEntity<CrmPageResult<List<PointDetailVo>>> pointDetailGet(PointDetailGetDto pointDetailGetDto) {
		return crmCustomerPointService.pointDetailGet(pointDetailGetDto);
	}

	/**
	 * 方法描述：会员增减积分
	 *
	 * @param pointChangeDto
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity
	 * @date 2022-01-24 10:16:54
	 */
	@Override
	@ApiOperation(value = "会员积分增减接口", notes = "调用CRM-会员积分增减接口")
	public ServerResponseEntity pointChange(PointChangeDto pointChangeDto) {
		return crmCustomerPointService.pointChange(pointChangeDto);
	}

	/**
	 * 积分兑换数据同步
	 * @param dto
	 * @return
	 */
	@Override
	public ServerResponseEntity syncPointConvertData(@Valid List<SyncPointConvertDataDto> dto) {
		return crmCustomerPointService.syncPointConvertData(dto);
	}
}
