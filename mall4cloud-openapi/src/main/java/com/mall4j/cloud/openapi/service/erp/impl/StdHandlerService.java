package com.mall4j.cloud.openapi.service.erp.impl;

import com.mall4j.cloud.api.openapi.skq_erp.dto.StdCommonReq;
import com.mall4j.cloud.api.openapi.skq_erp.response.StdResult;
import com.mall4j.cloud.api.openapi.utils.RequestUtils;
import com.mall4j.cloud.api.openapi.utils.StdSignUtils;
import com.mall4j.cloud.openapi.service.erp.IStdHandlerService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 类描述：中台调用接口调度
 *
 * @date 2022/1/6 23:38：40
 */
public class StdHandlerService {

	private static final Logger logger = LoggerFactory.getLogger(StdHandlerService.class);

	private static final Map<String, IStdHandlerService> HANDLE_SERVICE_MAP = new ConcurrentHashMap<>();

	StdSignUtils signUtils;

	RequestUtils requestUtils;

	static ApplicationContext applicationContext;

	/**
	 * 方法描述：中台调用接口处理逻辑
	 *
	 * @param commonReq
	 * @param request
	 * @return com.mall4j.cloud.api.openapi.skq_erp.response.StdResult
	 * @date 2022-01-06 23:35:00
	 */
	public StdResult stdHandler(StdCommonReq commonReq, HttpServletRequest request) {
		long start = System.currentTimeMillis();
		String requestData = "";
		StdResult stdResult = null;
		try {
			IStdHandlerService handlerService = null;
			if (commonReq == null) {
				return stdResult = StdResult.fail("请求参数为空");
			}
			stdResult = commonReq.check();
			if (stdResult.fail()) {
				return stdResult;
			}
			requestData = getRequestUtils().getRequestData(request);
			if (!getSignUtils().verifySign(commonReq, requestData)) {
				return stdResult = StdResult.fail("验签失败");
			}
			if (StringUtils.isNotBlank(commonReq.getMethod()) && (handlerService = HANDLE_SERVICE_MAP.get(commonReq.getMethod())) != null) {
				return stdResult = handlerService.stdHandler(commonReq, requestData);
			} else {
				logger.error("中台调用接口-method不正确,参数为:" + commonReq);
				return StdResult.fail("method不正确");
			}
		} finally {
			logger.info("中台调用接口接收到请求参数：{}，body参数：{}，响应为：{}，共耗时：{}", commonReq, requestData, stdResult, System.currentTimeMillis() - start);
		}
	}

	/**
	 * 方法描述：中台调用接口处理逻辑
	 *
	 * @param commonReq
	 * @param request
	 * @return com.mall4j.cloud.api.openapi.skq_erp.response.StdResult
	 * @date 2022-01-06 23:35:00
	 */
	public StdResult testStdHandler(StdCommonReq commonReq, HttpServletRequest request) {
		long start = System.currentTimeMillis();
		String requestData = "";
		StdResult stdResult = null;
		try {
			IStdHandlerService handlerService = null;
			if (commonReq == null) {
				return stdResult = StdResult.fail("请求参数为空");
			}
			stdResult = commonReq.check();
			if (stdResult.fail()) {
				return stdResult;
			}
			requestData = commonReq.getPostData();
//			if (!getSignUtils().verifySign(commonReq, requestData)) {
//				return stdResult = StdResult.fail("验签失败");
//			}
			if (StringUtils.isNotBlank(commonReq.getMethod()) && (handlerService = HANDLE_SERVICE_MAP.get(commonReq.getMethod())) != null) {
				return stdResult = handlerService.stdHandler(commonReq, requestData);
			} else {
				logger.error("中台调用接口-method不正确,参数为:" + commonReq);
				return StdResult.fail("method不正确");
			}
		} finally {
			logger.info("中台调用接口接收到请求参数：{}，body参数：{}，响应为：{}，共耗时：{}", commonReq, requestData, stdResult, System.currentTimeMillis() - start);
		}
	}

	public void register(String method, IStdHandlerService stdHandlerService) {
		HANDLE_SERVICE_MAP.put(method, stdHandlerService);
	}

	private StdHandlerService() {
	}

	public static StdHandlerService getInstance() {
		return StdHandlerService.Instance.instance;
	}

	public static void setApplicationContext(ApplicationContext context) {
		applicationContext = context;
	}

	public StdSignUtils getSignUtils() {
		if (signUtils == null) {
			signUtils = applicationContext.getBean(StdSignUtils.class);
		}
		return signUtils;
	}

	public RequestUtils getRequestUtils() {
		if (requestUtils == null) {
			requestUtils = applicationContext.getBean(RequestUtils.class);
		}
		return requestUtils;
	}

	private static class Instance {
		private static StdHandlerService instance = new StdHandlerService();
	}
}
