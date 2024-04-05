package com.mall4j.cloud.api.biz.dto.channels.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 视频号4.0用户优惠券列表请求实体
 */
@Data
public class EcUserCouponListRequest {
	
	//用户openid
	private String openid;
	
	//优惠券状态，不填时获取所有优惠券，填时获取特定状态优惠券
	//100生效中, 101已过期, 102已使用
	private Integer status;
	
	//页码
	private Integer page;
	
	//页大小
	@JsonProperty("page_size")
	private Integer pageSize;
	
	//翻页上下文，第一次请求填空，后续请求值为上次请求的返回
	@JsonProperty("page_ctx")
	private String pageCtx;
	
}
