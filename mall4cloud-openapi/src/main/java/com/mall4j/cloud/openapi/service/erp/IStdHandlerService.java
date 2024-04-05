package com.mall4j.cloud.openapi.service.erp;

import com.mall4j.cloud.api.openapi.skq_erp.dto.StdCommonReq;
import com.mall4j.cloud.api.openapi.skq_erp.response.StdResult;
import com.mall4j.cloud.openapi.service.erp.impl.StdHandlerService;

/**
 * 类描述：对接中台业务逻辑处理类
 *
 * @date 2022/1/6 23:34：04
 */
public interface IStdHandlerService {

	/**
	 * 方法描述：中台调用接口处理逻辑
	 * @param commonReq
	 * @param bodyStr
	 * @return com.mall4j.cloud.api.openapi.skq_erp.response.StdResult
	 * @date 2022-01-06 23:35:00
	 */
	StdResult stdHandler(StdCommonReq commonReq, String bodyStr);

	default void register(String method, IStdHandlerService stdHandlerService) {
		StdHandlerService.getInstance().register(method, stdHandlerService);
	}
}
