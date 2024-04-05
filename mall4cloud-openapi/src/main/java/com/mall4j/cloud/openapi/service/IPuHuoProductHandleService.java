package com.mall4j.cloud.openapi.service;

import cn.hutool.core.bean.BeanUtil;
import com.mall4j.cloud.api.openapi.ipuhuo.dto.CommonReqDto;
import com.mall4j.cloud.api.openapi.ipuhuo.dto.IPuHuoRespDto;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public interface IPuHuoProductHandleService {

	IPuHuoRespDto productAll(CommonReqDto commonReqDto, HttpServletRequest request);

	default <T> T fillBeanWithRequest(HttpServletRequest request, T bean, boolean isIgnoreError) {
		Map<String, String[]> parameterMap = request.getParameterMap();
		Map<String, String> paramMap = new HashMap<>();
		if (parameterMap != null && !parameterMap.isEmpty()) {
			for (Map.Entry<String, String[]> stringEntry : parameterMap.entrySet()) {
				String key = stringEntry.getKey();
				String[] value = stringEntry.getValue();
				if (StringUtils.isNotBlank(key) && value != null && value.length > 0) {
					paramMap.put(key, value[0]);
				}
			}
		}
		return BeanUtil.fillBeanWithMapIgnoreCase(paramMap, bean, isIgnoreError);
	}
}
