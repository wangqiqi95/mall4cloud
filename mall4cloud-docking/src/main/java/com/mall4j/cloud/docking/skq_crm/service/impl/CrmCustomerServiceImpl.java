package com.mall4j.cloud.docking.skq_crm.service.impl;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.docking.skq_crm.dto.AttentionCancelDto;
import com.mall4j.cloud.api.docking.skq_crm.dto.CrmResult;
import com.mall4j.cloud.api.docking.skq_crm.dto.CustomerAddDto;
import com.mall4j.cloud.api.docking.skq_crm.dto.CustomerCheckDto;
import com.mall4j.cloud.api.docking.skq_crm.dto.CustomerGetDto;
import com.mall4j.cloud.api.docking.skq_crm.dto.CustomerUpdateDto;
import com.mall4j.cloud.api.docking.skq_crm.dto.TmallSmsCodeCheckDto;
import com.mall4j.cloud.api.docking.skq_crm.dto.TmallSmsCodeSendDto;
import com.mall4j.cloud.api.docking.skq_crm.enums.CrmMethod;
import com.mall4j.cloud.api.docking.skq_crm.resp.CustomerAddResp;
import com.mall4j.cloud.api.docking.skq_crm.resp.CustomerCheckResp;
import com.mall4j.cloud.api.docking.skq_crm.vo.CustomerGetVo;
import com.mall4j.cloud.api.docking.skq_crm.vo.ScoreExpiredGetVo;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.docking.skq_crm.service.ICrmCustomerService;
import com.mall4j.cloud.docking.utils.CrmClients;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service("crmCustomerService")
public class CrmCustomerServiceImpl implements ICrmCustomerService {

    @Override
    public ServerResponseEntity<CustomerGetVo> customerGet(CustomerGetDto customerGetDto) {
        if (null == customerGetDto) {
            return ServerResponseEntity.fail(ResponseEnum.DATA_INCOMPLETE);
        }
        Map<String, Object> stringObjectMap = customerGetDto.toMap();
        String result = CrmClients.clients().get(CrmMethod.CUSTOMER_GET.uri(), stringObjectMap);

        if (StringUtils.isBlank(result)) {
            ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
            fail.setMsg("调用CRM-会员基本信息查询接口无响应");
            return fail;
        }
        CrmResult<CustomerGetVo> crmResult = JSONUtil.toBean(result, new TypeReference<CrmResult<CustomerGetVo>>() {
        }, true);

        String errorMsg = "会员基本信息查询失败";
        if (crmResult != null) {
            if (crmResult.success()) {
                return ServerResponseEntity.success(crmResult.getJsondata());
            }
            errorMsg = crmResult.getMessage();
        }
        ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
        fail.setMsg(errorMsg);
        return fail;
    }

    @Override
    public ServerResponseEntity<ScoreExpiredGetVo> scoreExpiredGet(CustomerGetDto customerGetDto) {
        if (null == customerGetDto) {
            return ServerResponseEntity.fail(ResponseEnum.DATA_INCOMPLETE);
        }
        Map<String, Object> stringObjectMap = customerGetDto.toMap();
        String result = CrmClients.clients().get(CrmMethod.SCORE_EXPIRED_GET.uri(), stringObjectMap);

        if (StringUtils.isBlank(result)) {
            ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
            fail.setMsg("调用CRM-会员年度过期积分查询接口无响应");
            return fail;
        }
        CrmResult<ScoreExpiredGetVo> crmResult = JSONUtil.toBean(result, new TypeReference<CrmResult<ScoreExpiredGetVo>>() {
        }, true);

        String errorMsg = "会员年度过期积分查询失败";
        if (crmResult != null) {
            if (crmResult.success()) {
                return ServerResponseEntity.success(crmResult.getJsondata());
            }
            errorMsg = crmResult.getMessage();
        }
        ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
        fail.setMsg(errorMsg);

        return fail;
    }

    @Override
	public ServerResponseEntity<CustomerCheckResp> customerCheck(CustomerCheckDto customerCheckDto) {
        long start = System.currentTimeMillis();
		if (null == customerCheckDto) {
			return ServerResponseEntity.fail(ResponseEnum.DATA_INCOMPLETE);
		}

		String s = CrmClients.clients().postCrm(CrmMethod.CUSTOMER_CHECK.uri(), JSONObject.toJSONString(customerCheckDto));

//        String url = CrmClients.clients().getUri(CrmMethod.CUSTOMER_CHECK.uri());
//        String s = "";
//        try{
//            s = HttpUtil.post(url,JSONObject.toJSONString(customerCheckDto),30000);
//        }catch (Exception e){
//            log.error("HttpUtil调用 crm customerCheck接口异常，url为:{},  json参数为:{}, 请求响应为:{}, 共耗时: {}",url, JSONObject.toJSONString(customerCheckDto), s,
//                    System.currentTimeMillis() - start);
//            e.printStackTrace();
//        }finally {
//            log.info("HttpUtil调用crm customerCheck 接口结束，url为:{},  json参数为:{}, 请求响应为:{}, 共耗时: {}",url, JSONObject.toJSONString(customerCheckDto), s,
//                    System.currentTimeMillis() - start);
//        }

		if (StringUtils.isBlank(s)) {
			ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
			fail.setMsg("调用CRM-会员注册验证接口无响应");
			return fail;
		}

		CrmResult<CustomerCheckResp> crmResult = JSONUtil.toBean(s, new TypeReference<CrmResult<CustomerCheckResp>>() {
		}, true);
		String errorMsg = "会员注册验证失败";
		if (crmResult != null) {
			if (crmResult.success()) {
				return ServerResponseEntity.success(crmResult.getJsondata());
			}
			errorMsg = crmResult.getMessage();
		}
		ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
		fail.setMsg(errorMsg);
		return fail;
	}

	@Override
	public ServerResponseEntity<CustomerAddResp> customerAdd(CustomerAddDto customerAddDto) {
		if (null == customerAddDto) {
			return ServerResponseEntity.fail(ResponseEnum.DATA_INCOMPLETE);
		}

		String s = CrmClients.clients().postCrm(CrmMethod.CUSTOMER_ADD.uri(), JSONObject.toJSONString(customerAddDto));
		if (StringUtils.isBlank(s)) {
			ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
			fail.setMsg("调用CRM-会员新增接口无响应");
			return fail;
		}

		CrmResult<CustomerAddResp> crmResult = JSONUtil.toBean(s, new TypeReference<CrmResult<CustomerAddResp>>() {
		}, true);
		String errorMsg = "会员新增失败";
		if (crmResult != null) {
			if (crmResult.success()) {
				return ServerResponseEntity.success(crmResult.getJsondata());
			}
			errorMsg = crmResult.getMessage();
		}
		ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
		fail.setMsg(errorMsg);
		return fail;
	}

    @Override
    public ServerResponseEntity customerUpdate(CustomerUpdateDto customerUpdateDto) {
        if (null == customerUpdateDto) {
            return ServerResponseEntity.fail(ResponseEnum.DATA_INCOMPLETE);
        }

        String s = CrmClients.clients().postCrm(CrmMethod.CUSTOMER_UPDATE.uri(), JSONObject.toJSONString(customerUpdateDto));
        if (StringUtils.isBlank(s)) {
            ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
            fail.setMsg("调用CRM-会员更新接口无响应");
            return fail;
        }

        CrmResult crmResult = JSONObject.parseObject(s, CrmResult.class);
        String errorMsg = "会员更新失败";
        if (crmResult != null) {
            if (crmResult.success()) {
                return ServerResponseEntity.success();
            }
            errorMsg = crmResult.getMessage();
        }
        ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
        fail.setMsg(errorMsg);
        return fail;
    }

    @Override
    public ServerResponseEntity attentionCancel(AttentionCancelDto attentionCancelDto) {
        if (null == attentionCancelDto) {
            return ServerResponseEntity.fail(ResponseEnum.DATA_INCOMPLETE);
        }

        String s = CrmClients.clients().postCrm(CrmMethod.CUSTOMER_ATTENTIONCANCEL.uri(), JSONObject.toJSONString(attentionCancelDto));
        if (StringUtils.isBlank(s)) {
            ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
            fail.setMsg("调用CRM-会员关注、取关接口无响应");
            return fail;
        }

        CrmResult crmResult = JSONObject.parseObject(s, CrmResult.class);
        String errorMsg = "会员关注、取关失败";
        if (crmResult != null) {
            if (crmResult.success()) {
                return ServerResponseEntity.success();
            }
            errorMsg = crmResult.getMessage();
        }
        ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
        fail.setMsg(errorMsg);
        return fail;
    }

    @Override
    public ServerResponseEntity tmallSmsCodeSend(TmallSmsCodeSendDto tmallSmsCodeSendDto) {
        long start = System.currentTimeMillis();
        if (null == tmallSmsCodeSendDto) {
            return ServerResponseEntity.fail(ResponseEnum.DATA_INCOMPLETE);
        }

        String s = CrmClients.clients().postCrm(CrmMethod.CUSTOMER_TMALL_SMS_CODE.uri(), JSONObject.toJSONString(tmallSmsCodeSendDto));


//        String url = CrmClients.clients().getUri(CrmMethod.CUSTOMER_TMALL_SMS_CODE.uri());
//        String s = "";
//        try{
//            s = HttpUtil.post(url,JSONObject.toJSONString(tmallSmsCodeSendDto),30000);
//        }catch (Exception e){
//            log.error("HttpUtil调用crm tmallSmsCodeSend 接口异常，url为:{},  json参数为:{}, 请求响应为:{}, 共耗时: {}",url, JSONObject.toJSONString(tmallSmsCodeSendDto), s,
//                    System.currentTimeMillis() - start);
//            e.printStackTrace();
//        }finally {
//            log.info("HttpUtil调用crm tmallSmsCodeSend 接口结束，url为:{},  json参数为:{}, 请求响应为:{}, 共耗时: {}",url, JSONObject.toJSONString(tmallSmsCodeSendDto), s,
//                    System.currentTimeMillis() - start);
//        }


        if (StringUtils.isBlank(s)) {
            ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
            fail.setMsg("调用CRM-获取淘宝外域入会验证码无响应");
            return fail;
        }

        CrmResult crmResult = JSONObject.parseObject(s, CrmResult.class);
        String errorMsg = "获取淘宝外域入会验证码失败";
        if (crmResult != null) {
            if (crmResult.success()) {
                return ServerResponseEntity.success();
            }
            errorMsg = crmResult.getMessage();
        }
        ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
        fail.setMsg(errorMsg);
        return fail;
    }

    @Override
    public ServerResponseEntity tmallSmsCodeCheck(TmallSmsCodeCheckDto tmallSmsCodeCheckDto) {
        if (null == tmallSmsCodeCheckDto) {
            return ServerResponseEntity.fail(ResponseEnum.DATA_INCOMPLETE);
        }

        String s = CrmClients.clients().postCrm(CrmMethod.CUSTOMER_TMALL_SMS_CHECK.uri(), JSONObject.toJSONString(tmallSmsCodeCheckDto));
        if (StringUtils.isBlank(s)) {
            ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
            fail.setMsg("调用CRM-验证淘宝外域入会验证码请求无响应");
            return fail;
        }

        CrmResult crmResult = JSONObject.parseObject(s, CrmResult.class);
        String errorMsg = "验证淘宝外域入会验证码失败";
        if (crmResult != null) {
            if (crmResult.success()) {
                return ServerResponseEntity.success();
            }
            errorMsg = crmResult.getMessage();
        }
        ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
        fail.setMsg(errorMsg);
        return fail;
    }
}
