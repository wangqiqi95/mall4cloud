package com.mall4j.cloud.docking.skq_erp.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.docking.skq_erp.dto.PushProductsDto;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.docking.skq_erp.service.IStdProductService;
import com.mall4j.cloud.docking.utils.StdClients;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("stdProductService")
public class StdProductServiceImpl implements IStdProductService {

	private static final String SHOP_URI= "/api/ip/std/data/service";
	private static final String SHOP_SYN_PRO= "std.universal.syn.pro";

	@Override
	public ServerResponseEntity<String> pushProduct(List<PushProductsDto> pushProductDtos) {
		if (null == pushProductDtos) {
			return ServerResponseEntity.fail(ResponseEnum.DATA_INCOMPLETE);
		}
		JSONObject body = new JSONObject();
		body.put("data", pushProductDtos);
		String s = StdClients.clients().postStd(SHOP_URI, SHOP_SYN_PRO, body.toJSONString());
		if (StringUtils.isBlank(s)) {
			ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
			fail.setMsg("售卖商品推送接口无响应");
			return fail;
		}

		JSONObject resp = JSON.parseObject(s);
		Boolean isSuccess = resp.getBoolean("isSuccess");
		String errorMsg = resp.getString("errorMsg");
		if (isSuccess != null && isSuccess) {
			String data = resp.getString("data");
			return ServerResponseEntity.success(resp.toJSONString());
		}
		ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
		fail.setMsg(StringUtils.isBlank(errorMsg) ? "售卖商品推送失败" : errorMsg);
		return fail;
	}
}
