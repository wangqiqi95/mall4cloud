package com.mall4j.cloud.api.openapi.utils;

import com.mall4j.cloud.api.openapi.skq_erp.config.OpenApiStdParams;
import com.mall4j.cloud.api.openapi.skq_erp.dto.StdCommonReq;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 类描述：中台验签工具类
 *
 * @date 2022/1/7 1:22：26
 */
@Slf4j
@Component
public class StdSignUtils {

	@Autowired
	OpenApiStdParams openApiStdParams;

	public boolean verifySign(StdCommonReq commonReq, String data) {
		StringBuilder signStr = new StringBuilder();
		signStr.append("data=").append(data).append("&");
		signStr.append("key=").append(openApiStdParams.getUserKey()).append("&");
		signStr.append("method=").append(commonReq.getMethod()).append("&");
		signStr.append("timestamp=").append(commonReq.getTimestamp()).append("&");
		signStr.append("version=").append(commonReq.getVersion());

		String encode = MD5Util.encode(signStr.toString()).toUpperCase();
		log.info("加签字符串：{}，传入sign:{},加签生成字符串：{}",signStr,commonReq.getSign(),encode);
		return encode.equals(commonReq.getSign());
	}

//	public static void main(String[] args) {
////		StringBuilder signStr = new StringBuilder();
////		String data = "{\"phone\":\"15993910913\"}";
////		long times = System.currentTimeMillis();
////		signStr.append("data=").append(data).append("&");
////		signStr.append("key=").append("xxykey").append("&");
////		signStr.append("method=").append("std.universal.getSmsCode").append("&");
////		signStr.append("timestamp=").append(1657781471).append("&");
////		signStr.append("version=").append("v1");
////		System.out.println(times);
////		String encode = MD5Util.encode(signStr.toString()).toUpperCase();
////		System.out.println(encode);
//
//		String dataStr = "data={\"phone\":\"15993910913\"}&key=xxykey&method=std.universal.getSmsCode&timestamp=1657782959&version=v1";
//		System.out.println(dataStr);
//		System.out.println(MD5Util.encode(dataStr.toString()).toUpperCase());
//	}
	

}
