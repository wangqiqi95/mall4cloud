package com.mall4j.cloud.docking.feign.jos;

import com.mall4j.cloud.api.docking.jos.dto.QueryPayAmountDto;
import com.mall4j.cloud.api.docking.jos.dto.QueryPayAmountResp;
import com.mall4j.cloud.api.docking.jos.dto.QuerySettlementStatusDto;
import com.mall4j.cloud.api.docking.jos.dto.QuerySettlementStatusResp;
import com.mall4j.cloud.api.docking.jos.dto.SettlementApplyDto;
import com.mall4j.cloud.api.docking.jos.dto.SettlementApplyResp;
import com.mall4j.cloud.api.docking.jos.dto.SettlementUpdateDto;
import com.mall4j.cloud.api.docking.jos.dto.SettlementUpdateResp;
import com.mall4j.cloud.api.docking.jos.feign.SettlementFeignClient;
import com.mall4j.cloud.docking.service.IJosSettlementService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 益世企业综合服务发佣相关接口
 * @date 2021/12/26 13:40
 */
@RestController
@Api(tags = "京东益世-企业综合服务发佣相关接口")
public class JosSettlementController implements SettlementFeignClient {

	@Autowired
	IJosSettlementService josSettlementService;

	/**
	 * 四、	益世企业综合服务发佣申请接口
	 *
	 * @param settlementApplyDto
	 * @return
	 */
	@Override
	public SettlementApplyResp memberAndProtocoInfo(SettlementApplyDto settlementApplyDto) {
		return josSettlementService.memberAndProtocoInfo(settlementApplyDto);
	}

	/**
	 * 五、	企业综合服务发佣信息修改接口
	 *
	 * @param settlementUpdateDto
	 * @return
	 */
	@Override
	public SettlementUpdateResp settlementUpdate(SettlementUpdateDto settlementUpdateDto) {
		return josSettlementService.settlementUpdate(settlementUpdateDto);
	}

	/**
	 * 六、	企业综合服务发佣状态查询接口
	 *
	 * @param querySettlementStatusDto
	 * @return
	 */
	@Override
	public QuerySettlementStatusResp querySettlementStatus(QuerySettlementStatusDto querySettlementStatusDto) {
		return josSettlementService.querySettlementStatus(querySettlementStatusDto);
	}

	/**
	 * 七、	企业综合服务查询应付金额接口
	 *
	 * @param queryPayAmountDto
	 * @return
	 */
	@Override
	public QueryPayAmountResp queryPayAmount(QueryPayAmountDto queryPayAmountDto) {
		return josSettlementService.queryPayAmount(queryPayAmountDto);
	}
}
