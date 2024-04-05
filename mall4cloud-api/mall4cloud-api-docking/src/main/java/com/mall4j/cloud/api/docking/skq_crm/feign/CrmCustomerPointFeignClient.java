package com.mall4j.cloud.api.docking.skq_crm.feign;

import com.mall4j.cloud.api.docking.skq_crm.dto.CrmPageResult;
import com.mall4j.cloud.api.docking.skq_crm.dto.PointChangeDto;
import com.mall4j.cloud.api.docking.skq_crm.dto.PointDetailGetDto;
import com.mall4j.cloud.api.docking.skq_crm.dto.SyncPointConvertDataDto;
import com.mall4j.cloud.api.docking.skq_crm.vo.PointDetailVo;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

/**
 * 类描述：crm会员积分接口
 *
 * @date 2022/1/24 9:42：41
 */
@FeignClient(value = "mall4cloud-docking",contextId = "crm-customer-point")
public interface CrmCustomerPointFeignClient {

	/**
	 * 方法描述：会员积分明细查询
	 * @param pointDetailGetDto
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity<java.util.List<com.mall4j.cloud.api.docking.skq_crm.vo.PointDetailVo>>
	 * @date 2022-01-24 09:45:01
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/crm/customer/point/detailGet")
	ServerResponseEntity<CrmPageResult<List<PointDetailVo>>> pointDetailGet(@RequestBody PointDetailGetDto pointDetailGetDto);

	/**
	 * 方法描述：会员增减积分
	 * @param pointChangeDto
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity
	 * @date 2022-01-24 10:16:54
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/crm/customer/point/change")
	ServerResponseEntity pointChange(@RequestBody PointChangeDto pointChangeDto);


	/**
	 * 积分兑换数据同步
	 * @param dto
	 * @return
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/crm/sync/point/trade/data")
	ServerResponseEntity syncPointConvertData(@Valid @RequestBody List<SyncPointConvertDataDto> dto);
}
