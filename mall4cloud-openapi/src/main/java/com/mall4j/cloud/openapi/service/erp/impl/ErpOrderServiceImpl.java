package com.mall4j.cloud.openapi.service.erp.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.mall4j.cloud.api.openapi.skq_crm.response.CrmResponseEnum;
import com.mall4j.cloud.api.openapi.skq_erp.dto.DeliveryOrderItemDTO;
import com.mall4j.cloud.api.openapi.skq_erp.dto.OrderDeliveryDto;
import com.mall4j.cloud.api.openapi.skq_erp.response.ErpResponse;
import com.mall4j.cloud.api.openapi.skq_erp.response.ErpResponseEnum;
import com.mall4j.cloud.api.order.dto.OrderRefundDto;
import com.mall4j.cloud.api.order.feign.OrderStatusFeignClient;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.openapi.service.erp.ErpOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("erpOrderService")
public class ErpOrderServiceImpl implements ErpOrderService {

    private final Logger logger = LoggerFactory.getLogger(ErpOrderServiceImpl.class);

    @Autowired
    OrderStatusFeignClient orderStatusFeignClient;

    @Override
    public ErpResponse orderReturnstatus(OrderRefundDto orderRefundParam) {
        long start = System.currentTimeMillis();
        ErpResponse erpResponse = ErpResponse.fail();
        ServerResponseEntity responseEntity = null;
        try {
            if (orderRefundParam == null) {
                erpResponse = ErpResponse.fail(CrmResponseEnum.HTTP_MESSAGE_NOT_READABLE.value(), "请求参数为空");
                return erpResponse;
            }
            erpResponse = check(orderRefundParam);
            if (erpResponse.isSuccess()) {
                responseEntity = orderStatusFeignClient.returnMoney(orderRefundParam);
                if (responseEntity.isSuccess()) {
                    erpResponse = ErpResponse.success();
                } else {
                    erpResponse = ErpResponse.fail(CrmResponseEnum.EXCEPTION.value(), responseEntity.getMsg());
                }
            }
            return erpResponse;
        } finally {
            logger.info("线上退单退货状态变更,接收到请求参数：{}，feign调用响应为：{},返回响应为：{}，共耗时：{}", orderRefundParam, responseEntity, erpResponse, System.currentTimeMillis() - start);
        }
    }

    private ErpResponse check(OrderRefundDto orderRefundParam) {
        if (orderRefundParam.getRefundId() == null) {
            return ErpResponse.fail(ErpResponseEnum.HTTP_MESSAGE_NOT_READABLE.value(), "小程序商城退款订单号不能为空");
        }
        if (orderRefundParam.getRefundSts() == null) {
            return ErpResponse.fail(ErpResponseEnum.HTTP_MESSAGE_NOT_READABLE.value(), "处理状态不能为空");
        }
        if (orderRefundParam.getIsReceived() == null) {
            return ErpResponse.fail(ErpResponseEnum.HTTP_MESSAGE_NOT_READABLE.value(), "是否收到货不能为空");
        }
        return ErpResponse.success();
    }

    @Override
    public ErpResponse orderDelivery(OrderDeliveryDto orderDeliveryDto) {
        long start = System.currentTimeMillis();
        ErpResponse erpResponse = ErpResponse.fail();
        ServerResponseEntity responseEntity = null;
        try {
            if (orderDeliveryDto == null) {
                erpResponse = ErpResponse.fail(CrmResponseEnum.HTTP_MESSAGE_NOT_READABLE.value(), "请求参数为空");
                return erpResponse;
            }
            erpResponse = orderDeliveryDto.check();
            if (erpResponse.isSuccess()) {
                responseEntity = orderStatusFeignClient.orderDelivery(convert(orderDeliveryDto));
                if (responseEntity.isSuccess()) {
                    erpResponse = ErpResponse.success();
                } else {
                    erpResponse = ErpResponse.fail(CrmResponseEnum.EXCEPTION.value(), responseEntity.getMsg());
                }
            }
            return erpResponse;
        } finally {
            logger.info("线上订单状态变更-发货,接收到请求参数：{}，feign调用响应为：{},返回响应为：{}，共耗时：{}", orderDeliveryDto, responseEntity, erpResponse, System.currentTimeMillis() - start);
        }
    }

    private com.mall4j.cloud.api.order.dto.OrderDeliveryDto convert(OrderDeliveryDto orderDeliveryDto) {
        com.mall4j.cloud.api.order.dto.OrderDeliveryDto dto = new com.mall4j.cloud.api.order.dto.OrderDeliveryDto();
        BeanUtils.copyProperties(orderDeliveryDto, dto);
        List<DeliveryOrderItemDTO> selectOrderItems = orderDeliveryDto.getSelectOrderItems();
        if (CollectionUtil.isNotEmpty(selectOrderItems)) {
            List<com.mall4j.cloud.api.order.dto.DeliveryOrderItemDTO> list = new ArrayList<>(selectOrderItems.size());
            for (DeliveryOrderItemDTO selectOrderItem : selectOrderItems) {
                com.mall4j.cloud.api.order.dto.DeliveryOrderItemDTO itemDTO = new com.mall4j.cloud.api.order.dto.DeliveryOrderItemDTO();
                BeanUtils.copyProperties(selectOrderItem, itemDTO);
                list.add(itemDTO);
            }
            dto.setSelectOrderItems(list);
        }
        return dto;
    }

}
