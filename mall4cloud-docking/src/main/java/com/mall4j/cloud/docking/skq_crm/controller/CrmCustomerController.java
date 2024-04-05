package com.mall4j.cloud.docking.skq_crm.controller;

import com.mall4j.cloud.api.docking.skq_crm.dto.AttentionCancelDto;
import com.mall4j.cloud.api.docking.skq_crm.dto.CustomerAddDto;
import com.mall4j.cloud.api.docking.skq_crm.dto.CustomerCheckDto;
import com.mall4j.cloud.api.docking.skq_crm.dto.CustomerGetDto;
import com.mall4j.cloud.api.docking.skq_crm.dto.CustomerUpdateDto;
import com.mall4j.cloud.api.docking.skq_crm.dto.TmallSmsCodeCheckDto;
import com.mall4j.cloud.api.docking.skq_crm.dto.TmallSmsCodeSendDto;
import com.mall4j.cloud.api.docking.skq_crm.feign.CrmCustomerFeignClient;
import com.mall4j.cloud.api.docking.skq_crm.resp.CustomerAddResp;
import com.mall4j.cloud.api.docking.skq_crm.resp.CustomerCheckResp;
import com.mall4j.cloud.api.docking.skq_crm.vo.CustomerGetVo;
import com.mall4j.cloud.api.docking.skq_crm.vo.ScoreExpiredGetVo;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.docking.skq_crm.service.ICrmCustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "crm会员相关接口")
public class CrmCustomerController implements CrmCustomerFeignClient {

	@Autowired
	ICrmCustomerService crmCustomerService;


	/**
	 * 方法描述：会员基本信息查询
	 *
	 * @param customerGetDto
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity<com.mall4j.cloud.api.docking.skq_crm.dto.CrmResult < com.mall4j.cloud.api.docking.skq_crm.vo.CustomerGetVo>>
	 * @date 2022-01-23 14:28:46
	 */
	@Override
	@ApiOperation(value = "会员基本信息查询", notes = "调用CRM接口查询会员基本信息")
	public ServerResponseEntity<CustomerGetVo> customerGet(CustomerGetDto customerGetDto) {
		return crmCustomerService.customerGet(customerGetDto);
	}

	@Override
	@ApiOperation(value = "会员年度过期积分查询", notes = "调用CRM接口会员年度过期积分")
	public ServerResponseEntity<ScoreExpiredGetVo> scoreExpiredGet(CustomerGetDto customerGetDto) {
		return crmCustomerService.scoreExpiredGet(customerGetDto);
	}

	/**
	 * 方法描述：会员注册验证接口
	 *
	 * @param customerCheckDto
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity<com.mall4j.cloud.api.docking.skq_crm.resp.CustomerCheckResp>
	 * @date 2022-01-23 14:59:19
	 */
	@Override
	@ApiOperation(value = "会员注册验证接口", notes = "调用CRM接口，校验会员是否存在")
	public ServerResponseEntity<CustomerCheckResp> customerCheck(CustomerCheckDto customerCheckDto) {
		return crmCustomerService.customerCheck(customerCheckDto);
	}

	/**
	 * 方法描述：会员新增接口
	 *
	 * @param customerAddDto
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity<com.mall4j.cloud.api.docking.skq_crm.resp.CustomerAddResp>
	 * @date 2022-01-23 15:28:58
	 */
	@Override
	@ApiOperation(value = "会员新增接口", notes = "调用CRM接口推送会员信息")
	public ServerResponseEntity<CustomerAddResp> customerAdd(CustomerAddDto customerAddDto) {
		return crmCustomerService.customerAdd(customerAddDto);
	}

	@Override
	@ApiOperation(value = "会员更新接口", notes = "调用CRM接口推送会员更新信息")
	public ServerResponseEntity customerUpdate(CustomerUpdateDto customerUpdateDto) {
		return crmCustomerService.customerUpdate(customerUpdateDto);
	}

	/**
	 * 方法描述：会员关注、取关接口
	 *
	 * @param attentionCancelDto
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity
	 * @date 2022-01-26 11:00:46
	 */
	@Override
	@ApiOperation(value = "会员关注、取关接口", notes = "会员关注、取关接口")
	public ServerResponseEntity attentionCancel(AttentionCancelDto attentionCancelDto) {
		return crmCustomerService.attentionCancel(attentionCancelDto);
	}

	/**
	 * 方法描述：获取淘宝外域入会验证码
	 *
	 * @param tmallSmsCodeSendDto
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity
	 * @date 2022-01-26 11:14:16
	 */
	@Override
	@ApiOperation(value = "获取淘宝外域入会验证码", notes = "获取淘宝外域入会验证码")
	public ServerResponseEntity tmallSmsCodeSend(TmallSmsCodeSendDto tmallSmsCodeSendDto) {
		return crmCustomerService.tmallSmsCodeSend(tmallSmsCodeSendDto);
	}

	/**
	 * 方法描述：验证淘宝外域入会验证码
	 *
	 * @param tmallSmsCodeCheckDto
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity
	 * @date 2022-01-26 11:14:16
	 */
	@Override
	@ApiOperation(value = "验证淘宝外域入会验证码", notes = "验证淘宝外域入会验证码")
	public ServerResponseEntity tmallSmsCodeCheck(TmallSmsCodeCheckDto tmallSmsCodeCheckDto) {
		return crmCustomerService.tmallSmsCodeCheck(tmallSmsCodeCheckDto);
	}
}
