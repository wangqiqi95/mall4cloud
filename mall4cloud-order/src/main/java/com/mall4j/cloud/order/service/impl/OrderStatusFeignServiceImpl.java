package com.mall4j.cloud.order.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alibaba.nacos.common.utils.StringUtils;
import com.aliyun.openservices.ons.api.SendResult;
import com.mall4j.cloud.api.biz.feign.WxMaApiFeignClient;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptTmessageSendVO;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptTmessageVO;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptUserRecordVO;
import com.mall4j.cloud.api.delivery.dto.DeliveryCompanyDTO;
import com.mall4j.cloud.api.delivery.dto.DeliveryOrderDTO;
import com.mall4j.cloud.api.delivery.feign.DeliveryCompanyFeignClient;
import com.mall4j.cloud.api.delivery.vo.DeliveryCompanyVO;
import com.mall4j.cloud.api.order.dto.DeliveryOrderItemDTO;
import com.mall4j.cloud.api.order.dto.OrderDeliveryDto;
import com.mall4j.cloud.api.order.dto.OrderRefundDto;
import com.mall4j.cloud.common.constant.MaSendTypeEnum;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.order.constant.DeliveryType;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.rocketmq.OnsMQTransactionTemplate;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.order.constant.RefundStsType;
import com.mall4j.cloud.order.constant.ReturnProcessStatusEnum;
import com.mall4j.cloud.order.dto.multishop.OrderRefundDTO;
import com.mall4j.cloud.order.model.Order;
import com.mall4j.cloud.order.model.OrderSettlement;
import com.mall4j.cloud.order.service.OrderRefundService;
import com.mall4j.cloud.order.service.OrderService;
import com.mall4j.cloud.order.service.OrderSettlementService;
import com.mall4j.cloud.order.service.OrderStatusFeignService;
import com.mall4j.cloud.order.vo.OrderRefundVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service("orderStatusFeignService")
public class OrderStatusFeignServiceImpl implements OrderStatusFeignService {
//	private final Logger logger = LoggerFactory.getLogger(OrderStatusFeignServiceImpl.class);

	@Autowired
	OnsMQTransactionTemplate orderRefundTemplate;

	@Autowired
	OrderRefundService orderRefundService;

	@Autowired
	OrderSettlementService orderSettlementService;

	@Autowired
	OrderService orderService;

	@Autowired
	DeliveryCompanyFeignClient deliveryCompanyFeignClient;
	@Autowired
    WxMaApiFeignClient wxMaApiFeignClient;
    @Autowired
    private OnsMQTemplate sendMaSubcriptMessageTemplate;

	/**
	 * 方法描述：中台发货处理逻辑
	 * @param orderDeliveryDto
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity
	 * @date 2021-12-29 09:15:01
	 */
	@Override
	public ServerResponseEntity orderDelivery(OrderDeliveryDto orderDeliveryDto) {
//		Long orderNo = orderDeliveryDto.getOrderNo();
//		Order order = orderService.getByOrderId(orderNo);
		String orderNo = orderDeliveryDto.getOrderNo();
		Order order = orderService.getByOrderNumber(orderNo);
		if (order == null) {
			return ServerResponseEntity.fail(500, "小程序商城订单不存在");
		}
		List<DeliveryOrderItemDTO> selectOrderItems = orderDeliveryDto.getSelectOrderItems();
		if(CollectionUtil.isEmpty(selectOrderItems)){
			return ServerResponseEntity.fail(500, "请至少选择一个订单项进行发货操作");
		}
		DeliveryOrderDTO deliveryOrderParam = new DeliveryOrderDTO();
		deliveryOrderParam.setOrderId(order.getOrderId());
		if(StringUtils.isNotEmpty(orderDeliveryDto.getDeliveryCode()) && "SR".equals(orderDeliveryDto.getDeliveryCode())){
			deliveryOrderParam.setDeliveryType(DeliveryType.LOGISTICS.value());
		}else {
			deliveryOrderParam.setDeliveryType(DeliveryType.DELIVERY.value());
		}
		deliveryOrderParam.setDeliveryCompanyCode(orderDeliveryDto.getDeliveryCode());
		deliveryOrderParam.setDeliveryNo(orderDeliveryDto.getLogisticNo());
		// 设置快递公司id
		DeliveryCompanyDTO deliveryCompanyDTO = new DeliveryCompanyDTO();
		deliveryCompanyDTO.setName(orderDeliveryDto.getLogisticsName());
		try {
			ServerResponseEntity<List<DeliveryCompanyVO>> listServerResponseEntity = deliveryCompanyFeignClient.listBySearch(deliveryCompanyDTO);
			List<DeliveryCompanyVO> data = listServerResponseEntity.getData();
			if (CollectionUtil.isNotEmpty(data)) {
				DeliveryCompanyVO deliveryCompanyVO = data.get(0);
				deliveryOrderParam.setDeliveryCompanyId(deliveryCompanyVO.getDeliveryCompanyId());
			}
		} catch (Exception e) {
			log.error("", e);
		}
		deliveryOrderParam.setSelectOrderItems(convert(orderDeliveryDto.getSelectOrderItems()));
		orderService.delivery(deliveryOrderParam);

        try {
            // 平台发货完发送订阅消息通知
            List<String> businessIds = new ArrayList<>();
            businessIds.add(StrUtil.toString(order.getOrderId()));
            ServerResponseEntity<WeixinMaSubscriptTmessageVO> subscriptResp = wxMaApiFeignClient.getinsIderSubscriptTmessage(MaSendTypeEnum.ORDER_DELIVERY.getValue(),
                    businessIds);
            log.info("订单发货subscriptResp {}", JSONObject.toJSONString(subscriptResp.getData()));
            if (subscriptResp.isSuccess()) {
                WeixinMaSubscriptTmessageVO weixinMaSubscriptTmessageVO = subscriptResp.getData();
                List<WeixinMaSubscriptUserRecordVO> userRecords = weixinMaSubscriptTmessageVO.getUserRecords();

                if (!org.springframework.util.CollectionUtils.isEmpty(userRecords)) {

                    List<WeixinMaSubscriptTmessageSendVO> notifyList = userRecords.stream().map(u -> {
                        /**
                         *  1: 要跳转的 地址
                         *  pages/order-detail/order-detail?orderId=
                         *  需要拼接订单编号参数
                         */
//                        String page =StrUtil.isEmpty(subscriptResp.getData().getPage())?"":subscriptResp.getData().getPage()+orderNo;
						String page ="pages/order-detail/order-detail?orderId="+order.getOrderId();

                        String spuName = selectOrderItems.get(0).getSpuName();

                        /**
                         * 值替换
                         * 1会员业务 2优惠券业务 3订单业务 4退单业务 5线下活动  6积分商城 7每日签到 8直播通知 9微客审核
                         * 可以替换的模版为固定的， 根据对应类型在参考 mall4cloud.weixin_ma_subscript_tmessage_optional_value
                         * 当前微客 场景下 提交人{submitter}、提交时间{submitTime}、  审核结果（通过/不通过）{auditResult}
                         * 构建参数map.
                         */
                        Map<String,String> paramMap = new HashMap();
                        paramMap.put("{orderId}",StrUtil.toString(orderNo));
                        paramMap.put("{amount}", StrUtil.toString(order.getActualTotal()/100));
                        paramMap.put("{spuName}",spuName);
                        paramMap.put("{sendTime}", DateUtil.now());  //取当前时间作为发货时间

                        //构建msgdata参数
                        List<WeixinMaSubscriptTmessageSendVO.MsgData> msgDataList = weixinMaSubscriptTmessageVO.getTmessageValues().stream().map(t -> {
                            WeixinMaSubscriptTmessageSendVO.MsgData msgData = new WeixinMaSubscriptTmessageSendVO.MsgData();
                            msgData.setName(t.getTemplateValueName());
                            msgData.setValue(StrUtil.isEmpty(paramMap.get(t.getTemplateValue()))?t.getTemplateValue():paramMap.get(t.getTemplateValue()));
                            return msgData;
                        }).collect(Collectors.toList());

                        WeixinMaSubscriptTmessageSendVO sendVO = new WeixinMaSubscriptTmessageSendVO();
                        sendVO.setUserSubscriptRecordId(u.getId());
                        sendVO.setData(msgDataList);
                        sendVO.setPage(page);
                        return sendVO;
                    }).collect(Collectors.toList());
                    log.info("发货通知发送订阅消息，构建参数对象 {}", JSONObject.toJSONString(notifyList));
                    sendMaSubcriptMessageTemplate.syncSend(notifyList);
                }
            }
        } catch (Exception e) {
            log.error("订单发货通知订阅通知发送异常",e);
        }
		return ServerResponseEntity.success();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ServerResponseEntity orderDelivery(List<OrderDeliveryDto> orderDeliveryDtos) {
		ServerResponseEntity serverResponseEntity = ServerResponseEntity.fail(ResponseEnum.EXCEPTION);
		if (!CollectionUtils.isEmpty(orderDeliveryDtos)) {
			for (OrderDeliveryDto orderDeliveryDto : orderDeliveryDtos) {
				serverResponseEntity = this.orderDelivery(orderDeliveryDto);
				if (serverResponseEntity.isFail()) {
					break;
				}
			}
		}
		return serverResponseEntity;
	}

	@Override
	public ServerResponseEntity<Void> returnMoney(OrderRefundDto orderRefundParam) {
		// 获取退款单信息
		OrderRefundVO orderRefundVo = orderRefundService.getDetailByRefundNumber(orderRefundParam.getRefundId());
		if (!Objects.equals(ReturnProcessStatusEnum.CONSIGNMENT.value(), orderRefundVo.getReturnMoneySts())) {
			throw new LuckException("订单退款状态已发生改变，请勿重复操作");
		}
		if (!Objects.equals(orderRefundVo.getShopId(), AuthUserContext.get().getTenantId())) {
			throw new LuckException(ResponseEnum.UNAUTHORIZED);
		}
		// 拒绝退款，可以不需要分布式事务啥的，因为就单纯拒绝，发个通知给用户，做幂等处理就好了
		if (Objects.equals(orderRefundParam.getRefundSts(), RefundStsType.DISAGREE.value())) {
			OrderRefundDTO orderRefundDTO = new OrderRefundDTO();
			orderRefundDTO.setRefundId(orderRefundVo.getRefundId());
			orderRefundDTO.setRefundSts(orderRefundParam.getRefundSts());
			orderRefundDTO.setIsReceived(orderRefundParam.getIsReceived());
			orderRefundDTO.setRejectMessage(orderRefundParam.getRejectMessage());
			orderRefundService.disagreeRefund(orderRefundDTO, orderRefundVo);
			return ServerResponseEntity.success();
		}

		return agreeRefund(orderRefundParam,orderRefundVo);
	}


	private ServerResponseEntity<Void> agreeRefund(OrderRefundDto orderRefundParam, OrderRefundVO orderRefundVo) {

		OrderSettlement orderSettlement = orderSettlementService.getByOrderId(orderRefundVo.getOrderId());
		if (orderSettlement == null) {
			return ServerResponseEntity.showFailMsg("没有查询到支付记录，无法申请退款");
		}

		orderRefundVo.setPayId(orderSettlement.getPayId());
		orderRefundVo.setPayType(orderSettlement.getPayType());


		// 执行退款，真正意义上的退款，需要确保
		// 发送事务消息
		SendResult sendResult = orderRefundTemplate.sendMessageInTransaction(orderRefundVo, orderRefundParam);

		if (sendResult == null || sendResult.getMessageId() == null) {
			return ServerResponseEntity.fail(ResponseEnum.EXCEPTION);
		}
		return ServerResponseEntity.success();
	}

	private List<com.mall4j.cloud.api.delivery.dto.DeliveryOrderItemDTO> convert(List<DeliveryOrderItemDTO> selectOrderItems) {
		List<com.mall4j.cloud.api.delivery.dto.DeliveryOrderItemDTO> dtos = new ArrayList<>();
		if (CollectionUtil.isNotEmpty(selectOrderItems)) {
			for (DeliveryOrderItemDTO selectOrderItem : selectOrderItems) {
				com.mall4j.cloud.api.delivery.dto.DeliveryOrderItemDTO dto = new com.mall4j.cloud.api.delivery.dto.DeliveryOrderItemDTO();
				BeanUtils.copyProperties(selectOrderItem, dto);
				dtos.add(dto);
			}
		}
		return dtos;
	}

}
