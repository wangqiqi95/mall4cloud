package com.mall4j.cloud.openapi.service.erp.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.common.utils.StringUtils;
import com.alibaba.nacos.common.utils.UuidUtils;
import com.mall4j.cloud.api.openapi.skq_erp.dto.StdCommonReq;
import com.mall4j.cloud.api.openapi.skq_erp.dto.StdDynamicCodeDto;
import com.mall4j.cloud.api.openapi.skq_erp.response.StdResult;
import com.mall4j.cloud.api.user.dto.UserDynamicCodeDTO;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.openapi.service.erp.IStdHandlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service("stdDynamicCodeService")
public class StdDynamicCodeServiceImpl implements IStdHandlerService, InitializingBean {
	
	private static final Logger logger = LoggerFactory.getLogger(StdDynamicCodeServiceImpl.class);
	
	private static final String method = "std.universal.dynamicCode";
	
	@Autowired
	private UserFeignClient userFeignClient;
	
	@Override
	public StdResult stdHandler(StdCommonReq commonReq, String bodyStr) {
		long start = System.currentTimeMillis();
		String requestId = UuidUtils.generateUuid();
		StdResult stdResult = null;
		ServerResponseEntity<String> responseEntity = null;
		try {
			StdDynamicCodeDto stdDynamicCodeDto = null;
			if (StringUtils.isBlank(bodyStr) || Objects.isNull(stdDynamicCodeDto = JSON.parseObject(bodyStr,StdDynamicCodeDto.class))) {
				stdResult = StdResult.fail("请求参数为空");
				return stdResult;
			}
			if ((stdResult = stdDynamicCodeDto.check()).fail()) {
				return stdResult;
			}
			
			UserDynamicCodeDTO userDynamicCodeDTO = new UserDynamicCodeDTO();
			userDynamicCodeDTO.setDynamicCode(stdDynamicCodeDto.getDynamicCode());
			
			//远程调用
//			responseEntity = userFeignClient.decryptDynamicCode(userDynamicCodeDTO);
			
			if (Objects.isNull(responseEntity) ||responseEntity.isFail() || StrUtil.isEmpty(responseEntity.getData())) {
				stdResult = StdResult.fail(responseEntity.getMsg());
				logger.info("动态会员码解析-{}-feign调用处理失败，stdDynamicCodeDto:{},feign请求参数为:{},feign响应为:{}", requestId, stdDynamicCodeDto, userDynamicCodeDTO,
						responseEntity);
			}else {
				stdResult = StdResult.success(responseEntity.getData());
			}
		} catch (Exception e) {
			logger.error(requestId + "-动态会员码解析处理异常", e);
			stdResult = StdResult.fail("动态会员码解析失败");
		} finally {
			logger.info("动态会员码解析-{}-query请求参数:{},json请求参数:{},feign调用响应为：{},请求响应:{},共耗时:{}", requestId, commonReq, bodyStr, responseEntity, stdResult, System.currentTimeMillis() - start);
		}
		return stdResult;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		register(method, this);
	}
}
