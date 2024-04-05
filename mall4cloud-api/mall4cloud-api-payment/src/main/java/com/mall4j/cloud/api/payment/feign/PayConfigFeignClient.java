package com.mall4j.cloud.api.payment.feign;

import com.mall4j.cloud.api.payment.bo.MemberOrderInfoBO;
import com.mall4j.cloud.api.payment.vo.OrderPayTypeVO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 支付配置feign处理类
 */
@FeignClient(value = "mall4cloud-payment",contextId = "payConfig")
public interface PayConfigFeignClient {
	
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/payConfig/query/orderPayType")
	ServerResponseEntity<OrderPayTypeVO> queryOrderPayType(@RequestBody MemberOrderInfoBO memberOrderInfoBO);
	
}
