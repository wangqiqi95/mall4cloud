package com.mall4j.cloud.docking.service;

import com.mall4j.cloud.api.docking.jos.dto.QueryPayAmountDto;
import com.mall4j.cloud.api.docking.jos.dto.QueryPayAmountResp;
import com.mall4j.cloud.api.docking.jos.dto.QuerySettlementStatusDto;
import com.mall4j.cloud.api.docking.jos.dto.QuerySettlementStatusResp;
import com.mall4j.cloud.api.docking.jos.dto.SettlementApplyDto;
import com.mall4j.cloud.api.docking.jos.dto.SettlementApplyResp;
import com.mall4j.cloud.api.docking.jos.dto.SettlementUpdateDto;
import com.mall4j.cloud.api.docking.jos.dto.SettlementUpdateResp;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @description: jos 益世企业综合服务发佣相关接口对接实现
 * @date 2021/12/23 23:22
 */
public interface IJosSettlementService {

	/**
	 * 四、	益世企业综合服务发佣申请接口
	 *
	 * @param settlementApplyDto
	 * @return
	 */
	SettlementApplyResp memberAndProtocoInfo(SettlementApplyDto settlementApplyDto);

	/**
	 * 五、	企业综合服务发佣信息修改接口
	 *
	 * @param settlementUpdateDto
	 * @return
	 */
	SettlementUpdateResp settlementUpdate(SettlementUpdateDto settlementUpdateDto);

	/**
	 * 六、	企业综合服务发佣状态查询接口
	 *
	 * @param querySettlementStatusDto
	 * @return
	 */
	QuerySettlementStatusResp querySettlementStatus(QuerySettlementStatusDto querySettlementStatusDto);

	/**
	 * 七、	企业综合服务查询应付金额接口
	 * @param queryPayAmountDto
	 * @return
	 */
	QueryPayAmountResp queryPayAmount(@RequestBody QueryPayAmountDto queryPayAmountDto);
}
