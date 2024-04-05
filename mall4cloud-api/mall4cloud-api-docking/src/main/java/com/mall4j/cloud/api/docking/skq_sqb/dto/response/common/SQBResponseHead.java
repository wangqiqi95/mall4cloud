package com.mall4j.cloud.api.docking.skq_sqb.dto.response.common;

import lombok.Data;

@Data
public class SQBResponseHead {
	
	/**
	 * 收钱吧应用版本号，当前版本1.0.0
	 */
	private String version;
	
	/**
	 * 支持的签名算法：
	 * SHA256':SHA256withRSA
	 */
	private String sign_type;
	
	/**
	 * 轻POS应用编号，商户入网后由收钱吧技术支持提供
	 */
	private String appid;
	
	/**
	 * 发起请求时间
	 */
	private String response_time;
	
	/**
	 * 备注
	 */
	private String reserve;

}
