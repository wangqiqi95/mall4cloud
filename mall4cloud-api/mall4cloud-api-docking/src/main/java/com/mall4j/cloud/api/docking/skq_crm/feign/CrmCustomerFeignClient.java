package com.mall4j.cloud.api.docking.skq_crm.feign;

import com.mall4j.cloud.api.docking.skq_crm.dto.AttentionCancelDto;
import com.mall4j.cloud.api.docking.skq_crm.dto.CustomerAddDto;
import com.mall4j.cloud.api.docking.skq_crm.dto.CustomerCheckDto;
import com.mall4j.cloud.api.docking.skq_crm.dto.CustomerGetDto;
import com.mall4j.cloud.api.docking.skq_crm.dto.CustomerUpdateDto;
import com.mall4j.cloud.api.docking.skq_crm.dto.TmallSmsCodeCheckDto;
import com.mall4j.cloud.api.docking.skq_crm.dto.TmallSmsCodeSendDto;
import com.mall4j.cloud.api.docking.skq_crm.resp.CustomerAddResp;
import com.mall4j.cloud.api.docking.skq_crm.resp.CustomerCheckResp;
import com.mall4j.cloud.api.docking.skq_crm.vo.CustomerGetVo;
import com.mall4j.cloud.api.docking.skq_crm.vo.ScoreExpiredGetVo;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 类描述：crm会员信息接口
 *
 * @date 2022/1/23 14:26：43
 */
@FeignClient(value = "mall4cloud-docking",contextId = "crm-customer")
public interface CrmCustomerFeignClient {
	
	/** 
	 * 方法描述：会员基本信息查询
	 * @param customerGetDto
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity<com.mall4j.cloud.api.docking.skq_crm.dto.CrmResult<com.mall4j.cloud.api.docking.skq_crm.vo.CustomerGetVo>>
	 * @date 2022-01-23 14:28:46
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/crm/customer/get")
	ServerResponseEntity<CustomerGetVo> customerGet(@RequestBody CustomerGetDto customerGetDto);

	/**
	 * 方法描述：会员年度即将过期积分值查询
	 * @param customerGetDto
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity<com.mall4j.cloud.api.docking.skq_crm.dto.CrmResult<com.mall4j.cloud.api.docking.skq_crm.vo.ScoreExpiredGetVo>>
	 * @date 2022-01-23 14:28:46
	 */
	@GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/crm/score/expired/get")
	ServerResponseEntity<ScoreExpiredGetVo> scoreExpiredGet(@RequestBody CustomerGetDto customerGetDto);

	/**
	 * 方法描述：会员注册验证接口
	 * @param customerCheckDto
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity<com.mall4j.cloud.api.docking.skq_crm.resp.CustomerCheckResp>
	 * @date 2022-01-23 14:59:19
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/crm/customer/check")
	ServerResponseEntity<CustomerCheckResp> customerCheck(@RequestBody CustomerCheckDto customerCheckDto);
	
	/** 
	 * 方法描述：会员新增接口
	 * @param customerAddDto
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity<com.mall4j.cloud.api.docking.skq_crm.resp.CustomerAddResp>
	 * @date 2022-01-23 15:28:58
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/crm/customer/add")
	ServerResponseEntity<CustomerAddResp> customerAdd(@RequestBody CustomerAddDto customerAddDto);

	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/crm/customer/update")
	ServerResponseEntity customerUpdate(@RequestBody CustomerUpdateDto customerUpdateDto);

	/**
	 * 方法描述：会员关注、取关接口
	 * @param attentionCancelDto
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity
	 * @date 2022-01-26 11:00:46
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/crm/customer/attentionCancel")
	ServerResponseEntity attentionCancel(@RequestBody AttentionCancelDto attentionCancelDto);

	/**
	 * 方法描述：获取淘宝外域入会验证码
	 * @param tmallSmsCodeSendDto
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity
	 * @date 2022-01-26 11:14:16
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/crm/customer/tmallSmsCodeSend")
	ServerResponseEntity tmallSmsCodeSend(@RequestBody TmallSmsCodeSendDto tmallSmsCodeSendDto);

	/**
	 * 方法描述：验证淘宝外域入会验证码
	 * @param tmallSmsCodeCheckDto
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity
	 * @date 2022-01-26 11:14:16
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/crm/customer/tmallSmsCodeCheck")
	ServerResponseEntity tmallSmsCodeCheck(@RequestBody TmallSmsCodeCheckDto tmallSmsCodeCheckDto);
}
