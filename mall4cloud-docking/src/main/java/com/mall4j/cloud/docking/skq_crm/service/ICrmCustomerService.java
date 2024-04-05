package com.mall4j.cloud.docking.skq_crm.service;

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
import com.mall4j.cloud.common.response.ServerResponseEntity;

public interface ICrmCustomerService {

	ServerResponseEntity<CustomerGetVo> customerGet(CustomerGetDto customerGetDto);

	ServerResponseEntity<ScoreExpiredGetVo> scoreExpiredGet(CustomerGetDto customerGetDto);

	ServerResponseEntity<CustomerCheckResp> customerCheck(CustomerCheckDto customerCheckDto);

	ServerResponseEntity<CustomerAddResp> customerAdd(CustomerAddDto customerAddDto);

	ServerResponseEntity customerUpdate(CustomerUpdateDto customerUpdateDto);

	ServerResponseEntity attentionCancel(AttentionCancelDto attentionCancelDto);

	ServerResponseEntity tmallSmsCodeSend(TmallSmsCodeSendDto tmallSmsCodeSendDto);

	ServerResponseEntity tmallSmsCodeCheck(TmallSmsCodeCheckDto tmallSmsCodeCheckDto);
}
