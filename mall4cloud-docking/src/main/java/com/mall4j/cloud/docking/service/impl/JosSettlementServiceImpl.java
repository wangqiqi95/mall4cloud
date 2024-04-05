package com.mall4j.cloud.docking.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.docking.jos.dto.QueryPayAmountDto;
import com.mall4j.cloud.api.docking.jos.dto.QueryPayAmountResp;
import com.mall4j.cloud.api.docking.jos.dto.QuerySettlementStatusDto;
import com.mall4j.cloud.api.docking.jos.dto.QuerySettlementStatusResp;
import com.mall4j.cloud.api.docking.jos.dto.SettlementApplyDto;
import com.mall4j.cloud.api.docking.jos.dto.SettlementApplyResp;
import com.mall4j.cloud.api.docking.jos.dto.SettlementUpdateDto;
import com.mall4j.cloud.api.docking.jos.dto.SettlementUpdateResp;
import com.mall4j.cloud.api.docking.jos.enums.JosInterfaceMethod;
import com.mall4j.cloud.docking.service.ICallJosInterfaceService;
import com.mall4j.cloud.docking.service.IJosSettlementService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service("josSettlementService")
public class JosSettlementServiceImpl implements IJosSettlementService {
	/**
	 * 四、	益世企业综合服务发佣申请接口
	 *
	 * @param settlementApplyDto
	 * @return
	 */
	@Override
	public SettlementApplyResp memberAndProtocoInfo(SettlementApplyDto settlementApplyDto) {
		return new ICallJosInterfaceService<SettlementApplyResp>() {
			@Override
			public SettlementApplyResp convert(String s) {
				if (StringUtils.isNotBlank(s)) {
					return JSONObject.parseObject(s, SettlementApplyResp.class);
				}
				return new SettlementApplyResp(0, "调用失败");
			}
		}.callJosInterface(JosInterfaceMethod.SETTLEMENT_APPLY, settlementApplyDto);
	}

	/**
	 * 五、	企业综合服务发佣信息修改接口
	 *
	 * @param settlementUpdateDto
	 * @return
	 */
	@Override
	public SettlementUpdateResp settlementUpdate(SettlementUpdateDto settlementUpdateDto) {
		return new ICallJosInterfaceService<SettlementUpdateResp>() {
			@Override
			public SettlementUpdateResp convert(String s) {
				if (StringUtils.isNotBlank(s)) {
					return JSONObject.parseObject(s, SettlementUpdateResp.class);
				}
				return new SettlementUpdateResp(0, "调用失败");
			}
		}.callJosInterface(JosInterfaceMethod.SETTLEMENT_UPDATE, settlementUpdateDto);
	}

	/**
	 * 六、	企业综合服务发佣状态查询接口
	 *
	 * @param querySettlementStatusDto
	 * @return
	 */
	@Override
	public QuerySettlementStatusResp querySettlementStatus(QuerySettlementStatusDto querySettlementStatusDto) {
		return new ICallJosInterfaceService<QuerySettlementStatusResp>() {
			@Override
			public QuerySettlementStatusResp convert(String s) {
				if (StringUtils.isNotBlank(s)) {
					return JSONObject.parseObject(s, QuerySettlementStatusResp.class);
				}
				return new QuerySettlementStatusResp(0, "调用失败");
			}
		}.callJosInterface(JosInterfaceMethod.QUERY_SETTLEMENT_STATUS, querySettlementStatusDto);
	}

	/**
	 * 七、	企业综合服务查询应付金额接口
	 *
	 * @param queryPayAmountDto
	 * @return
	 */
	@Override
	public QueryPayAmountResp queryPayAmount(QueryPayAmountDto queryPayAmountDto) {
		return new ICallJosInterfaceService<QueryPayAmountResp>() {
			@Override
			public QueryPayAmountResp convert(String s) {
				if (StringUtils.isNotBlank(s)) {
					return JSONObject.parseObject(s, QueryPayAmountResp.class);
				}
				return new QueryPayAmountResp(0, "调用失败");
			}
		}.callJosInterface(JosInterfaceMethod.QUERY_PAYAMOUNT, queryPayAmountDto);
	}
}
