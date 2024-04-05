package com.mall4j.cloud.api.openapi.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @description: reques工具类
 * @date 2022/1/6 22:20
 */
@Component
public class RequestUtils {

	private static final Logger logger = LoggerFactory.getLogger(RequestUtils.class);

	/**
	 * 从request中取json格式数据
	 * @param request
	 * @return
	 */
	public <T> T getRequestData(HttpServletRequest request, Class<T> destinationClass) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		T t = null;
		try {
			InputStream inputStream = request.getInputStream();
			byte[] by = new byte[1024];
			int len = 0;
			if (inputStream != null) {
				while ((len = inputStream.read(by)) != -1) {
					outputStream.write(by, 0, len);
				}
				outputStream.flush();
			}
			String json = new String(outputStream.toByteArray(), "utf-8");
			if (StringUtils.isNotBlank(json)) {
				t = JSONObject.parseObject(json, destinationClass);
			}
		} catch (IOException e) {
			logger.error("", e);
			try {
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (Exception e1) {
				logger.error("", e1);
			}
		}
		return t;
	}

	/**
	 * 从request中取json格式数据
	 * @param request
	 * @return
	 */
	public String getRequestData(HttpServletRequest request) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		String json = null;
		try {
			InputStream inputStream = request.getInputStream();
			byte[] by = new byte[1024];
			int len = 0;
			if (inputStream != null) {
				while ((len = inputStream.read(by)) != -1) {
					outputStream.write(by, 0, len);
				}
				outputStream.flush();
			}
			json = new String(outputStream.toByteArray(), "utf-8");
		} catch (IOException e) {
			logger.error("", e);
			try {
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (Exception e1) {
				logger.error("", e1);
			}
		}
		return json;
	}
}
