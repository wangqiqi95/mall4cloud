package com.mall4j.cloud.docking.skq_sqb.service;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.docking.skq_sqb.config.SQBParams;
import com.mall4j.cloud.api.docking.skq_sqb.dto.request.SQBBodyByCancelOrder;
import com.mall4j.cloud.api.docking.skq_sqb.dto.request.SQBBodyByPurchase;
import com.mall4j.cloud.api.docking.skq_sqb.dto.request.SQBBodyByQueryResult;
import com.mall4j.cloud.api.docking.skq_sqb.dto.request.SQBBodyByRefund;
import com.mall4j.cloud.api.docking.skq_sqb.dto.request.common.CommonRequest;
import com.mall4j.cloud.api.docking.skq_sqb.dto.request.common.SQBBody;
import com.mall4j.cloud.api.docking.skq_sqb.dto.request.common.SQBHead;
import com.mall4j.cloud.api.docking.skq_sqb.dto.request.common.SQBReq;
import com.mall4j.cloud.api.docking.skq_sqb.dto.response.SQBCancelOrderResp;
import com.mall4j.cloud.api.docking.skq_sqb.dto.response.SQBPurchaseResp;
import com.mall4j.cloud.api.docking.skq_sqb.dto.response.SQBQueryResultResp;
import com.mall4j.cloud.api.docking.skq_sqb.dto.response.SQBRefundResp;
import com.mall4j.cloud.api.docking.skq_sqb.dto.response.common.SQBBaseResponse;
import com.mall4j.cloud.api.docking.skq_sqb.dto.response.common.SQBBizResponse;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.docking.skq_sqb.api.LitePosApi;
import com.mall4j.cloud.api.docking.skq_sqb.utils.SQBSignUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;


@Slf4j
@Service
public class LitePosService {
	
	@Autowired
	private LitePosApi litePosApi;
	
	@Autowired
	private SQBParams sqbParams;
	
	public ServerResponseEntity<SQBPurchaseResp> purchase(SQBBodyByPurchase sqbBodyByPurchase) {
		log.info("提交收钱吧购买接口,方法入参：{}", JSONObject.toJSONString(sqbBodyByPurchase));
		RequestBody requestBody = createRequestBody(sqbBodyByPurchase);
		
		JSONObject responseBody = litePosApi.purchase(requestBody);
		
		log.info("调用收钱吧购买API,执行结果:{}" ,JSONObject.toJSONString(responseBody));
		
		if (Objects.nonNull(responseBody)) {
			
			try {
				//校验签名
				boolean flag = SQBSignUtils.syncSign(JSONObject.toJSONString(responseBody),sqbParams.getPublicKey());
				if(!flag){
					log.info("调用收钱吧购买API同步反参验签失败");
					return ServerResponseEntity.fail(ResponseEnum.EXCEPTION);
				}
				
				SQBBaseResponse sqbBaseResponse = JSONObject.toJavaObject(responseBody, SQBBaseResponse.class);
				
				if(Objects.nonNull(sqbBaseResponse) && "200".equals(sqbBaseResponse.getResponse().getBody().getResult_code())){
					SQBPurchaseResp sqbPurchaseResp = JSONObject.toJavaObject(sqbBaseResponse.getResponse().getBody().getBiz_response().getData(), SQBPurchaseResp.class);
					
					if( Objects.nonNull(sqbPurchaseResp) ){
						return ServerResponseEntity.success(sqbPurchaseResp);
					}else {
						SQBBizResponse biz_response = sqbBaseResponse.getResponse().getBody().getBiz_response();
						if(Objects.nonNull(biz_response)){
							log.error("调用收钱吧购买API异常, 通讯响应码为: {}, 业务执行结果返回码: {}, 业务执行错误信息: {} ", biz_response.getResult_code() ,biz_response.getError_code() ,biz_response.getError_message());
							ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
							fail.setMsg(biz_response.getError_message());
							return fail;
						}
					}
				}
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		return ServerResponseEntity.fail(ResponseEnum.EXCEPTION);
	}

	public ServerResponseEntity<SQBRefundResp> refund(SQBBodyByRefund sqbBodyByRefund) {
		log.info("提交收钱吧退款接口,方法入参：{}", JSONObject.toJSONString(sqbBodyByRefund));
		
		RequestBody requestBody = createRequestBody(sqbBodyByRefund);
		JSONObject responseBody = litePosApi.refund(requestBody);

		log.info("调用收钱吧退款API, 执行结果:{}", JSONObject.toJSONString(responseBody));

		if (Objects.nonNull(responseBody)) {

			//校验签名
			boolean flag = SQBSignUtils.syncSign(JSONObject.toJSONString(responseBody),sqbParams.getPublicKey());
			if(!flag){
				log.info("调用收钱吧退款API同步反参验签失败");
				return ServerResponseEntity.fail(ResponseEnum.EXCEPTION);
			}

			SQBBaseResponse sqbBaseResponse = JSONObject.toJavaObject(responseBody, SQBBaseResponse.class);

			if(Objects.nonNull(sqbBaseResponse) && "200".equals(sqbBaseResponse.getResponse().getBody().getResult_code())){
				SQBRefundResp sqbRefundResp = JSONObject.toJavaObject(sqbBaseResponse.getResponse().getBody().getBiz_response().getData(), SQBRefundResp.class);
				
				if( Objects.nonNull(sqbRefundResp) ){
					return ServerResponseEntity.success(sqbRefundResp);
				}else {
					SQBBizResponse biz_response = sqbBaseResponse.getResponse().getBody().getBiz_response();
					if(Objects.nonNull(biz_response)){
						log.error("调用收钱吧退款API异常, 通讯响应码为: {}, 业务执行结果返回码: {}, 业务执行错误信息: {} ", biz_response.getResult_code() ,biz_response.getError_code() ,biz_response.getError_message());
						ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
						fail.setMsg(biz_response.getError_message());
						return fail;
					}
				}
			}
		}

		return ServerResponseEntity.fail(ResponseEnum.EXCEPTION);
	}

	public ServerResponseEntity<SQBCancelOrderResp> cancelOrderOperation(SQBBodyByCancelOrder sqbBodyByCancelOrder) {
		log.info("收钱吧取消订单接口,方法入参：{}", JSONObject.toJSONString(sqbBodyByCancelOrder));

		RequestBody requestBody = createRequestBody(sqbBodyByCancelOrder);
		JSONObject responseBody = litePosApi.cancelOrderOperation(requestBody);

		log.info("调用收钱吧取消订单API，执行结果:{}", JSONObject.toJSONString(responseBody));

		if (Objects.nonNull(responseBody)) {

			//校验签名
			boolean flag = SQBSignUtils.syncSign(JSONObject.toJSONString(responseBody),sqbParams.getPublicKey());
			if(!flag){
				log.info("调用收钱吧取消订单API同步反参验签失败");
				return ServerResponseEntity.fail(ResponseEnum.EXCEPTION);
			}

			SQBBaseResponse sqbBaseResponse = JSONObject.toJavaObject(responseBody, SQBBaseResponse.class);

			if(Objects.nonNull(sqbBaseResponse) && "200".equals(sqbBaseResponse.getResponse().getBody().getResult_code())){
				SQBCancelOrderResp sqbCancelOrderResp = JSONObject.toJavaObject(sqbBaseResponse.getResponse().getBody().getBiz_response().getData(), SQBCancelOrderResp.class);

				if( Objects.nonNull(sqbCancelOrderResp) ){
					return ServerResponseEntity.success(sqbCancelOrderResp);
				}else {
					SQBBizResponse biz_response = sqbBaseResponse.getResponse().getBody().getBiz_response();
					if(Objects.nonNull(biz_response)){
						log.error("调用收钱吧取消订单API异常, 通讯响应码为: {}, 业务执行结果返回码: {}, 业务执行错误信息: {} ", biz_response.getResult_code() ,biz_response.getError_code() ,biz_response.getError_message());
						ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
						fail.setMsg(biz_response.getError_message());
						return fail;
					}
				}
			}
		}

		return ServerResponseEntity.fail(ResponseEnum.EXCEPTION);
	}

	public ServerResponseEntity<SQBQueryResultResp> queryResult(SQBBodyByQueryResult sqbBodyByQueryResult) {
		log.info("收钱吧销售类结果查询接口,方法入参：{}", JSONObject.toJSONString(sqbBodyByQueryResult));

		RequestBody requestBody = createRequestBody(sqbBodyByQueryResult);
		JSONObject responseBody = litePosApi.queryResult(requestBody);

		log.info("调用收钱吧销售类结果查询API，执行结果:{}", JSONObject.toJSONString(responseBody));

		if (Objects.nonNull(responseBody)) {

			//校验签名
			boolean flag = SQBSignUtils.syncSign(JSONObject.toJSONString(responseBody),sqbParams.getPublicKey());
			if(!flag){
				log.info("调用收钱吧销售类结果查询API同步反参验签失败");
				return ServerResponseEntity.fail(ResponseEnum.EXCEPTION);
			}

			SQBBaseResponse sqbBaseResponse = JSONObject.toJavaObject(responseBody, SQBBaseResponse.class);

			if(Objects.nonNull(sqbBaseResponse) && "200".equals(sqbBaseResponse.getResponse().getBody().getResult_code())){
				SQBQueryResultResp sqbQueryResultResp = JSONObject.toJavaObject(sqbBaseResponse.getResponse().getBody().getBiz_response().getData(), SQBQueryResultResp.class);

				if( Objects.nonNull(sqbQueryResultResp) ){
					return ServerResponseEntity.success(sqbQueryResultResp);
				}else {
					SQBBizResponse biz_response = sqbBaseResponse.getResponse().getBody().getBiz_response();
					if(Objects.nonNull(biz_response)){
						log.error("调用收钱吧销售类结果查询API异常, 通讯响应码为: {}, 业务执行结果返回码: {}, 业务执行错误信息: {} ", biz_response.getResult_code() ,biz_response.getError_code() ,biz_response.getError_message());
						ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
						fail.setMsg(biz_response.getError_message());
						return fail;
					}
				}
			}
		}

		return ServerResponseEntity.fail(ResponseEnum.EXCEPTION);
	}
	
	private RequestBody createRequestBody(SQBBody sqbBody){
		SQBReq sqbReq = new SQBReq();
		CommonRequest commonRequest = new CommonRequest();
		SQBHead head = new SQBHead();
		
		head.setAppid(sqbParams.getAppid());
		head.setVersion("1.0.0");
		head.setSign_type("SHA256");
		head.setRequest_time(DateUtil.format(new Date(), "yyyy-MM-dd'T'HH:mm:ssXXX"));
		head.setReserve("{}");
		
		commonRequest.setHead(head);
		commonRequest.setBody(sqbBody);
		
		String signature = SQBSignUtils.sign(JSONObject.toJSONString(commonRequest),sqbParams.getPrivateKey(),"UTF-8");
		
		sqbReq.setRequest(commonRequest);
		sqbReq.setSignature(signature);
		
		log.info("调用收钱吧组装参数公共方法出参:{}", JSONObject.toJSONString(sqbReq));
		return RequestBody.create(MediaType.parse("application/json"), JSONObject.toJSONString(sqbReq));
	}
}
