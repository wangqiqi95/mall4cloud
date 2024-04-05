package com.mall4j.cloud.api.docking.skq_sqb.dto.response.common.providerResponse;

import lombok.Data;

import java.util.List;

/**
 * @author ty
 * @ClassName ProviderResponse
 * @description
 * @date 2023/5/8 15:35
 */
@Data
public class ProviderResponse {

	/**
	 * 内容为核销单品信息
	 */
	private String goods_details;
	
	/**
	 * 内容为核销券信息
	 */
	private String voucher_details;
}
