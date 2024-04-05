package com.mall4j.cloud.payment.feign;


import com.mall4j.cloud.api.payment.bo.MemberOrderInfoBO;
import com.mall4j.cloud.api.payment.feign.PayConfigFeignClient;
import com.mall4j.cloud.api.payment.vo.OrderPayTypeVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.payment.service.PayConfigBizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * 支付配置openfeign实现类
 *
 */

@RestController
public class PayConfigFeignClientController implements PayConfigFeignClient {
	

	@Autowired
	private PayConfigBizService payConfigBizService;
	
	@Override
	public ServerResponseEntity<OrderPayTypeVO> queryOrderPayType(MemberOrderInfoBO memberOrderInfoBO) {
		return payConfigBizService.queryOrderPayType(memberOrderInfoBO);
	}
}
