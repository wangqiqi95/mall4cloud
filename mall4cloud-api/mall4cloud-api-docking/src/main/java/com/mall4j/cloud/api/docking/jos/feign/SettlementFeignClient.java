package com.mall4j.cloud.api.docking.jos.feign;

import com.mall4j.cloud.api.docking.jos.dto.QueryPayAmountDto;
import com.mall4j.cloud.api.docking.jos.dto.QueryPayAmountResp;
import com.mall4j.cloud.api.docking.jos.dto.QuerySettlementStatusDto;
import com.mall4j.cloud.api.docking.jos.dto.QuerySettlementStatusResp;
import com.mall4j.cloud.api.docking.jos.dto.SettlementApplyDto;
import com.mall4j.cloud.api.docking.jos.dto.SettlementApplyResp;
import com.mall4j.cloud.api.docking.jos.dto.SettlementUpdateDto;
import com.mall4j.cloud.api.docking.jos.dto.SettlementUpdateResp;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @description: jos发佣相关接口
 * @date 2021/12/26 13:38
 */
@FeignClient(value = "mall4cloud-docking",contextId = "jos-settlement")
public interface SettlementFeignClient {

	/**
	 * 四、	益世企业综合服务发佣申请接口
	 * @param settlementApplyDto
	 * @return
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/josSettlement/settlementApply")
	SettlementApplyResp memberAndProtocoInfo(@RequestBody SettlementApplyDto settlementApplyDto);

	/**
	 * 五、	企业综合服务发佣信息修改接口
	 * @param settlementUpdateDto
	 * @return
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/josSettlement/settlementUpdate")
	SettlementUpdateResp settlementUpdate(@RequestBody SettlementUpdateDto settlementUpdateDto);

	/**
	 * 六、	企业综合服务发佣状态查询接口
	 * @param querySettlementStatusDto
	 * @return
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/josSettlement/querySettlementStatus")
	QuerySettlementStatusResp querySettlementStatus(@RequestBody QuerySettlementStatusDto querySettlementStatusDto);

	/**
	 * 七、	企业综合服务查询应付金额接口
	 * @param queryPayAmountDto
	 * @return
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/josSettlement/queryPayAmount")
	QueryPayAmountResp queryPayAmount(@RequestBody QueryPayAmountDto queryPayAmountDto);

}
