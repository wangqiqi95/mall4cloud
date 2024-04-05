package com.mall4j.cloud.docking.skq_crm.service.impl;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.docking.skq_crm.dto.*;
import com.mall4j.cloud.api.docking.skq_crm.enums.CrmMethod;
import com.mall4j.cloud.api.docking.skq_crm.vo.PointDetailVo;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.docking.skq_crm.service.ICrmCustomerPointService;
import com.mall4j.cloud.docking.utils.CrmClients;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("crmCustomerPointService")
public class CrmCustomerPointServiceImpl implements ICrmCustomerPointService {
	@Override
	public ServerResponseEntity<CrmPageResult<List<PointDetailVo>>> pointDetailGet(PointDetailGetDto pointDetailGetDto) {
		if (null == pointDetailGetDto) {
			return ServerResponseEntity.fail(ResponseEnum.DATA_INCOMPLETE);
		}
		Map<String, Object> stringObjectMap = pointDetailGetDto.toMap();
		String result = CrmClients.clients().get(CrmMethod.CUSTOMER_POINT_DETAIL_GET.uri(), stringObjectMap);

		if (StringUtils.isBlank(result)) {
			ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
			fail.setMsg("调用CRM-会员积分明细查询接口无响应");
			return fail;
		}
		CrmPageResult<List<PointDetailVo>> crmPageResult = JSONUtil.toBean(result, new TypeReference<CrmPageResult<List<PointDetailVo>>>() {
		}, true);

		String errorMsg = "会员积分明细查询失败";
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

	@Override
	public ServerResponseEntity pointChange(PointChangeDto pointChangeDto) {
		if (null == pointChangeDto) {
			return ServerResponseEntity.fail(ResponseEnum.DATA_INCOMPLETE);
		}
		String result = CrmClients.clients().postCrm(CrmMethod.CUSTOMER_POINT_CHANGE.uri(), JSONObject.toJSONString(pointChangeDto));

		if (StringUtils.isBlank(result)) {
			ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
			fail.setMsg("调用CRM-会员积分增减接口无响应");
			return fail;
		}
		CrmResult crmResult = JSONObject.parseObject(result, CrmResult.class);

		String errorMsg = "会员积分增减失败";
		if (crmResult != null) {
			if (crmResult.success()) {
				return ServerResponseEntity.success();
			}
			errorMsg = crmResult.getMessage();
		}
		ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
		fail.setMsg(errorMsg);
		return fail;
	}

    @Override
    public ServerResponseEntity syncPointConvertData(List<SyncPointConvertDataDto> dto) {
		String result = CrmClients.clients().postCrm(CrmMethod.SYNC_CONVERT_DATA.uri(), JSONObject.toJSONString(dto));

		if (StringUtils.isBlank(result)) {
			ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
			fail.setMsg("调用CRM-积分兑换数据同步接口无响应");
			return fail;
		}
		CrmResult crmResult = JSONObject.parseObject(result, CrmResult.class);

		String errorMsg = "积分兑换数据同步失败";
		if (crmResult != null) {
			if (crmResult.success()) {
				return ServerResponseEntity.success();
			}
			errorMsg = crmResult.getMessage();
		}
		ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
		fail.setMsg(errorMsg);
        return fail;
    }
}
