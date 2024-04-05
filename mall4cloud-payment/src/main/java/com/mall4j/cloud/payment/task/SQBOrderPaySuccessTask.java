package com.mall4j.cloud.payment.task;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.mall4j.cloud.api.docking.skq_sqb.config.SQBParams;
import com.mall4j.cloud.api.docking.skq_sqb.dto.request.SQBBodyByQueryResult;
import com.mall4j.cloud.api.docking.skq_sqb.dto.response.QueryResultRespTender;
import com.mall4j.cloud.api.docking.skq_sqb.dto.response.SQBQueryResultResp;
import com.mall4j.cloud.api.docking.skq_sqb.feign.LitePosApiFeignClient;
import com.mall4j.cloud.api.order.bo.OrderStatusBO;
import com.mall4j.cloud.api.order.constant.OrderStatus;
import com.mall4j.cloud.api.order.constant.SQBOrderStatus;
import com.mall4j.cloud.api.order.feign.OrderFeignClient;
import com.mall4j.cloud.common.constant.PayType;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.payment.bo.PayInfoResultBO;
import com.mall4j.cloud.payment.constant.PayStatus;
import com.mall4j.cloud.payment.manager.PayNoticeManager;
import com.mall4j.cloud.payment.mapper.PayInfoMapper;
import com.mall4j.cloud.payment.model.PayInfo;
import com.mall4j.cloud.payment.service.PayInfoService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 收钱吧订单支付成功处理定时任务
 */
@Component
@Slf4j
public class SQBOrderPaySuccessTask {
	
	private static final Logger logger = LoggerFactory.getLogger(SQBOrderPaySuccessTask.class);
	
	@Autowired
	private PayInfoService payInfoService;
	@Autowired
	private LitePosApiFeignClient litePosApiFeignClient;
	@Autowired
	private OrderFeignClient orderFeignClient;
	@Autowired
	private PayNoticeManager payNoticeManager;
	@Resource
	private PayInfoMapper payInfoMapper;
	@Autowired
	private SQBParams sqbParams;
	
	
	
	@XxlJob("sqbOrderPaySuccessTask")
	public void sqbOrderPaySuccessTask(){
		
		logger.info("--------------------开始执行收钱吧订单支付成功处理定时任务--------------------");
		long currentTime = System.currentTimeMillis();
		
		//获取未支付并且支付类型为10的支付记录
		List<PayInfo> payInfoList = payInfoService.listByPayStatusAndPayType(PayStatus.UNPAY.value(), PayType.SQB_LITE_POS.value());
		logger.info("获取支付类型为10并且未支付的支付记录, 查询结果：{},结果条数：【{}】", JSON.toJSONString(payInfoList), payInfoList.size() );
		
		if(CollectionUtil.isNotEmpty(payInfoList)){
			
			for (PayInfo payInfo : payInfoList) {
				// 销售类结果查询
				SQBBodyByQueryResult sqbBodyByQueryResult = new SQBBodyByQueryResult();
				sqbBodyByQueryResult.setBrand_code(sqbParams.getBrand_code());
				sqbBodyByQueryResult.setStore_sn(sqbParams.getStore_sn());
				sqbBodyByQueryResult.setWorkstation_sn("0");
				sqbBodyByQueryResult.setCheck_sn(payInfo.getOrderNumber());
				//调用收钱吧查询销售结果
				ServerResponseEntity<SQBQueryResultResp> queryResultResp = litePosApiFeignClient.queryResult(sqbBodyByQueryResult);
				
				if (Objects.nonNull(queryResultResp) && queryResultResp.isSuccess() && Objects.nonNull(queryResultResp.getData())) {
					
					SQBQueryResultResp sqbQueryResultResp = queryResultResp.getData();
					log.info("收钱吧支付成功订单,查询销售结果订单编号：【{}】,响应结果：{}", payInfo.getOrderNumber(), JSON.toJSONString(sqbQueryResultResp));
					
					//获取小程序订单信息
					List<Long> orderIdList = Arrays.stream(payInfo.getOrderIds().split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
					ServerResponseEntity<List<OrderStatusBO>> ordersStatusResponse = orderFeignClient.getOrdersStatus(orderIdList);
					
					if(Objects.nonNull(ordersStatusResponse) && ordersStatusResponse.isSuccess() && CollectionUtil.isNotEmpty(ordersStatusResponse.getData())){
						List<OrderStatusBO> orderStatusList = ordersStatusResponse.getData();
						OrderStatusBO orderStatusBO = orderStatusList.get(0);
						
						//根据收钱吧订单状态处理
						if( Objects.nonNull(orderStatusBO) ){
							
							//收钱吧订单已支付,小程序订单未支付
							if ( SQBOrderStatus.SUCCESS.value().equals(sqbQueryResultResp.getOrderStatus())
									&& Objects.equals(orderStatusBO.getStatus(), OrderStatus.UNPAY.value()) ) {
								
								//收钱吧订单的流水信息
								List<QueryResultRespTender> tenderList = sqbQueryResultResp.getTenders();
								if( CollectionUtil.isNotEmpty(tenderList) ){
									QueryResultRespTender tender = tenderList.get(0);
									if( Objects.nonNull(tender) && "3".equals(tender.getPayStatus()) ){
										
										PayInfoResultBO payInfoResultBO = new PayInfoResultBO();
										payInfoResultBO.setPaySuccess(true);
										payInfoResultBO.setPayId(payInfo.getPayId());
										payInfoResultBO.setCallbackContent("定时任务查询结果：" + JSON.toJSONString(sqbQueryResultResp));
										payInfoResultBO.setPayAmount(payInfo.getPayAmount());
										payInfoResultBO.setBizPayNo(tender.getTenderSn());
										
										//调用支付成功接口
										payNoticeManager.noticeOrder(payInfoResultBO, payInfo);
										
										logger.info("订单号："+payInfo.getOrderNumber()+",调用支付成功接口结束");
									}
								}
							}
							
							//处理收钱吧已取消订单，只是变更支付记录状态为-2(订单已取消)，预防下次定时任务执行的时候多捞数据
							if( SQBOrderStatus.CANCELED.value().equals(sqbQueryResultResp.getOrderStatus() )){
								
								PayInfo updatePayInfo = new PayInfo();
								updatePayInfo.setPayId(payInfo.getPayId());
								updatePayInfo.setCallbackContent("定时任务查询结果："+JSON.toJSONString(sqbQueryResultResp));
								updatePayInfo.setCallbackTime(new Date());
								updatePayInfo.setPayStatus(PayStatus.CANCELED_OR_CANCELED.value());
								payInfoMapper.update(updatePayInfo);
								
								logger.info("订单号："+payInfo.getOrderNumber()+",支付记录变更为取消状态结束");
							}
						}
						
					}
				}else {
					logger.error("订单号: "+payInfo.getOrderNumber()+","+queryResultResp.getMsg());
				}
			}
		}
		logger.info("-----结束执行收钱吧订单支付成功处理定时任务,总耗时：【{}】ms------", (System.currentTimeMillis() - currentTime) );
	}
	
	
	
	
	@XxlJob("oneSQBOrderPaySuccessTask")
	public void oneSQBOrderPaySuccessTask() {
		
		logger.info("--------------------开始执行单个订单编号查询收钱吧订单支付成功处理定时任务--------------------");
		long currentTime = System.currentTimeMillis();
		
		String param = XxlJobHelper.getJobParam();
		log.info("接收調度中心参数...【{}】", param);
		if (StrUtil.isEmptyIfStr(param)) {
			log.info("参数");
			return;
		}
		
		//根据传入orderNumber查询支付记录
		PayInfo dbPayInfo = payInfoMapper.getPayInfoByOrderNumber(param);
		logger.info("执行单个订单编号定时任务,获取支付记录, 查询结果：{}", JSON.toJSONString(dbPayInfo));
		
		if (Objects.nonNull(dbPayInfo)) {
			
			// 销售类结果查询
			SQBBodyByQueryResult sqbBodyByQueryResult = new SQBBodyByQueryResult();
			sqbBodyByQueryResult.setBrand_code(sqbParams.getBrand_code());
			sqbBodyByQueryResult.setStore_sn(sqbParams.getStore_sn());
			sqbBodyByQueryResult.setWorkstation_sn("0");
			sqbBodyByQueryResult.setCheck_sn(param);
			//调用收钱吧查询销售结果
			ServerResponseEntity<SQBQueryResultResp> queryResultResp = litePosApiFeignClient.queryResult(sqbBodyByQueryResult);
			
			if (Objects.nonNull(queryResultResp) && queryResultResp.isSuccess() && Objects.nonNull(queryResultResp.getData())) {
				
				SQBQueryResultResp sqbQueryResultResp = queryResultResp.getData();
				log.info("执行单个订单编号定时任务,查询销售结果订单编号：【{}】,查询结果：{}", dbPayInfo.getOrderNumber(), JSON.toJSONString(sqbQueryResultResp));
				
				//获取小程序订单信息
				List<Long> orderIdList = Arrays.stream(dbPayInfo.getOrderIds().split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
				ServerResponseEntity<List<OrderStatusBO>> ordersStatusResponse = orderFeignClient.getOrdersStatus(orderIdList);
				
				log.info("获取小程序系统订单详情,查询结果：{}", JSON.toJSONString(ordersStatusResponse));
				
				if (Objects.nonNull(ordersStatusResponse) && ordersStatusResponse.isSuccess() && CollectionUtil.isNotEmpty(ordersStatusResponse.getData())) {
					List<OrderStatusBO> orderStatusList = ordersStatusResponse.getData();
					
					
					OrderStatusBO orderStatusBO = orderStatusList.get(0);
					
					//根据订单状态进行处理,收钱吧订单已支付,小程序订单未支付
					if (Objects.nonNull(orderStatusBO) && SQBOrderStatus.SUCCESS.value().equals(sqbQueryResultResp.getOrderStatus())
							&& Objects.equals(orderStatusBO.getStatus(), OrderStatus.UNPAY.value()) ) {
						
						//收钱吧订单的流水信息
						List<QueryResultRespTender> tenderList = sqbQueryResultResp.getTenders();
						if( CollectionUtil.isNotEmpty(tenderList) ){
							QueryResultRespTender tender = tenderList.get(0);
							if( Objects.nonNull(tender) && "3".equals(tender.getPayStatus()) ){
								
								PayInfoResultBO payInfoResultBO = new PayInfoResultBO();
								payInfoResultBO.setPaySuccess(true);
								payInfoResultBO.setPayId(dbPayInfo.getPayId());
								payInfoResultBO.setCallbackContent("定时任务查询结果：" + JSON.toJSONString(sqbQueryResultResp));
								payInfoResultBO.setPayAmount(dbPayInfo.getPayAmount());
								payInfoResultBO.setBizPayNo(tender.getTenderSn());
								
								//调用支付成功接口
								payNoticeManager.noticeOrder(payInfoResultBO, dbPayInfo);
								
								logger.info("订单号："+param+",调用支付成功接口结束");
							}
						}
					}
				}
			}else {
				logger.error("订单号: "+param+","+queryResultResp.getMsg());
			}
		}
		
		logger.info("-----结束执行单个订单编号查询收钱吧订单支付成功处理定时任务,总耗时：【{}】ms------", (System.currentTimeMillis() - currentTime) );
	}
}
