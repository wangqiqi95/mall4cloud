package com.mall4j.cloud.order.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.api.delivery.dto.DeliveryOrderItemDTO;
import com.mall4j.cloud.api.order.bo.DistributionAmountWithOrderIdBO;
import com.mall4j.cloud.api.order.bo.EsOrderBO;
import com.mall4j.cloud.api.order.bo.EsOrderItemBO;
import com.mall4j.cloud.api.product.feign.CategoryFeignClient;
import com.mall4j.cloud.api.product.feign.SkuFeignClient;
import com.mall4j.cloud.api.product.feign.SpuFeignClient;
import com.mall4j.cloud.api.product.util.ProductLangUtil;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.i18n.I18nMessage;
import com.mall4j.cloud.common.order.constant.DeliveryType;
import com.mall4j.cloud.common.order.vo.OrderItemLangVO;
import com.mall4j.cloud.common.order.vo.OrderItemVO;
import com.mall4j.cloud.common.product.vo.CategoryVO;
import com.mall4j.cloud.common.product.vo.SkuVO;
import com.mall4j.cloud.common.product.vo.SpuVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.LangUtil;
import com.mall4j.cloud.order.mapper.OrderItemLangMapper;
import com.mall4j.cloud.order.mapper.OrderItemMapper;
import com.mall4j.cloud.order.mapper.OrderMapper;
import com.mall4j.cloud.order.mapper.OrderRefundMapper;
import com.mall4j.cloud.order.model.Order;
import com.mall4j.cloud.order.model.OrderItem;
import com.mall4j.cloud.order.service.OrderItemService;
import com.mall4j.cloud.order.vo.OrderDetailVO;
import com.mall4j.cloud.order.vo.OrderItemDetailVO;
import com.mall4j.cloud.order.vo.OrderRefundVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 订单项
 *
 * @author FrozenWatermelon
 * @date 2020-12-04 11:27:35
 */
@Service
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private OrderItemLangMapper orderItemLangMapper;

    @Autowired
    private CategoryFeignClient categoryFeignClient;

    @Autowired
    private OrderRefundMapper orderRefundMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private SpuFeignClient spuFeignClient;
    @Autowired
    private SkuFeignClient skuFeignClient;

    @Override
    public PageVO<OrderItemVO> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> orderItemMapper.list());
    }

    @Override
    public OrderItemVO getByOrderItemId(Long orderItemId) {
        return orderItemMapper.getByOrderItemId(orderItemId, I18nMessage.getLang());
    }

    @Override
    public void save(OrderItem orderItem) {
        orderItemMapper.save(orderItem);
    }

    @Override
    public void update(OrderItem orderItem) {
        orderItemMapper.update(orderItem);
    }

    @Override
    public void deleteById(Long orderItemId) {
        orderItemMapper.deleteById(orderItemId);
    }

    @Override
    public List<OrderItem> listOrderItemsByOrderId(Long orderId) {
        return orderItemMapper.listOrderItemsByOrderId(orderId);
    }

    @Override
    public List<OrderItemVO> listOrderItemAndLangByOrderId(Long orderId) {
        List<OrderItemVO> orderItems = orderItemMapper.listOrderItemAndLangByOrderId(orderId);
        orderItems.forEach(item -> {
            if (item.getSkuId() != null && StrUtil.isBlank(item.getSkuName())) {
                ServerResponseEntity<SkuVO> sku = skuFeignClient.getById(item.getSkuId());
                if (sku != null && sku.isSuccess() && sku.getData() != null) {
                    if (StrUtil.isNotBlank(sku.getData().getSkuName())) {
                        item.setSkuName(sku.getData().getSkuName());
                    }
                }
            }
            if (item.getSpuId() != null && StrUtil.isBlank(item.getSpuName())) {
                ServerResponseEntity<SpuVO> spu = spuFeignClient.getById(item.getSpuId());
                if (spu != null && spu.isSuccess() && spu.getData() != null) {
                    if (StrUtil.isNotBlank(spu.getData().getName())) {
                        item.setSpuName(spu.getData().getName());
                    }
                }
            }
        });
//        OrderLangUtil.orderItemVOList(orderItems);
        return orderItems;
    }

    @Override
    public List<DistributionAmountWithOrderIdBO> sumTotalDistributionAmountByOrderIds(List<Long> orderIds) {
        return orderItemMapper.sumTotalDistributionAmountByOrderIds(orderIds);
    }

    @Override
    public void saveBatch(List<OrderItem> orderItems) {
        if (CollUtil.isEmpty(orderItems)) {
            return;
        }
        orderItemMapper.saveBatch(orderItems);
    }

    @Override
    public void updateBatch(List<OrderItem> orderItems) {
        orderItemMapper.updateBatch(orderItems);
    }

    @Override
    public List<String> getSpuNameListByOrderIds(long[] orderIdList) {
        List<String> spuNameList = new ArrayList<>();
        List<OrderItemLangVO> orderItemLangList = orderItemLangMapper.getLangListByOrderIds(orderIdList);
        Map<Long, List<OrderItemLangVO>> orderItemLangMap = orderItemLangList.stream().collect(Collectors.groupingBy(OrderItemLangVO::getOrderItemId));
        for (Long orderItemId : orderItemLangMap.keySet()) {
            Map<Integer, String> spuNameMap = orderItemLangMap.get(orderItemId).stream().collect(Collectors.toMap(OrderItemLangVO::getLang, OrderItemLangVO::getSpuName));
            spuNameList.add(LangUtil.getLangValue(spuNameMap));
        }
        return spuNameList;
    }

    @Override
    public Integer countByOrderId(Long orderId) {
        return orderItemMapper.countByOrderId(orderId);
    }

    @Override
    public Integer allCountByOrderId(Long orderId) {
        return orderItemMapper.allCountByOrderId(orderId);
    }

    @Override
    public void updateByDeliveries(List<DeliveryOrderItemDTO> deliveryOrderItems, Integer deliveryType) {
        int devType = 0;
        for (DeliveryOrderItemDTO deliveryOrderItem : deliveryOrderItems) {
            devType = orderItemMapper.getDevTypeByOrderItemId(deliveryOrderItem.getOrderItemId());
            if (Objects.equals(devType, DeliveryType.DELIVERY.value())){
                //如果快递项已有快递类型，则set进去应当是快递类型
                deliveryType = devType;
            }
            orderItemMapper.updateByDelivery(deliveryOrderItem,deliveryType);
        }
    }

    @Override
    public int countUnDeliveryNumByOrderId(Long orderId) {
        return orderItemMapper.countUnDeliveryNumByOrderId(orderId);
    }

    @Override
    public void reduceUnDeliveryNumByOrderItemId(Long orderItemId, Integer count) {
        orderItemMapper.reduceUnDeliveryNumByOrderItemId(orderItemId,count);
    }

    @Override
    public void updateRefundStatusByOrderId(Long orderId, Integer refundStatus) {
        orderItemMapper.updateRefundStatusByOrderId(orderId, refundStatus);
    }

    @Override
    public OrderDetailVO listDetailByParam(Long orderId, Long refundId) {
        OrderDetailVO orderDetailVO = new OrderDetailVO();
        Integer lang = I18nMessage.getLang();
        List<OrderItemDetailVO> list = orderItemMapper.listDetailByOrderId(orderId, lang);
        Order order = orderMapper.getByOrderId(orderId);
        OrderRefundVO orderRefundVO = null;
        if (Objects.nonNull(refundId)){
            orderRefundVO = orderRefundMapper.getByRefundId(refundId);
        }
        for (OrderItemDetailVO detailVO : list) {
            CategoryVO categoryVO = categoryFeignClient.getByCategoryId(detailVO.getCategoryId()).getData();
            ProductLangUtil.category(categoryVO);
            detailVO.setCategoryName(categoryVO.getName());
            if (Objects.nonNull(orderRefundVO) && Objects.equals(orderRefundVO.getOrderItemId(),0L)){
                detailVO.setRefundCount(detailVO.getCount());
                detailVO.setRefundAmount(detailVO.getActualTotal());
            } else if (Objects.nonNull(orderRefundVO) && !Objects.equals(orderRefundVO.getOrderItemId(),0L)){
                if (Objects.equals(orderRefundVO.getOrderItemId(),detailVO.getOrderItemId())){
                    detailVO.setRefundCount(orderRefundVO.getRefundCount());
                    detailVO.setRefundAmount(orderRefundVO.getRefundAmount());
                }
            }
        }
        orderDetailVO.setOrderItemDetailList(list);
        orderDetailVO.setFreeFreightAmount(order.getFreeFreightAmount());
        orderDetailVO.setPlatformFreeFreightAmount(order.getPlatformFreeFreightAmount());
        orderDetailVO.setFreightAmount(order.getFreightAmount());
        return orderDetailVO;
    }

    @Override
    public OrderItemVO getSpuInfoByOrderItemId(Long orderItemId) {
        return orderItemMapper.getSpuInfoByOrderItemId(orderItemId, I18nMessage.getLang());
    }

    @Override
    public boolean getDevTypeByOrderId(Long orderId) {
        List<Integer> devTypes = orderItemMapper.getDevTypeByOrderId(orderId);
        return devTypes.contains(DeliveryType.DELIVERY.value());
    }

    @Override
    public List<OrderItemVO> getOrderItems(List<Long> orderItemIds) {
        if (CollUtil.isEmpty(orderItemIds)) {
            return null;
        }
        List<OrderItemVO> orderItems = orderItemMapper.getOrderItems(orderItemIds);
//        OrderLangUtil.orderItemVOList(orderItems);
        return orderItems;
    }

    @Override
    public List<OrderItemVO> getOrderItemsByOrderIds(List<Long> orderIds) {
        if (CollUtil.isEmpty(orderIds)) {
            return null;
        }
        List<OrderItemVO> orderItems = orderItemMapper.listOrderItemByOrderIds(orderIds);
//        OrderLangUtil.orderItemVOList(orderItems);
        return orderItems;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBatchDistributionAmount(List<EsOrderItemBO> message) {
        if (CollUtil.isEmpty(message)) {
            return;
        }
        int row = orderItemMapper.updateBatchDistributionAmount(message);
//        if (row <= 0) {
//            return;
//        }
        // 将订单项里面的分销金额统计起来，加到订单的分销总金额里面
        List<Long> orderItemIds = message.stream().map(EsOrderItemBO::getOrderItemId).collect(Collectors.toList());
        List<OrderItemVO> orderItems = orderItemMapper.getOrderItems(orderItemIds);
        List<Long> orderIds = orderItems.stream().map(OrderItemVO::getOrderId).distinct().collect(Collectors.toList());
        List<EsOrderBO> orderList = orderMapper.getEsOrderList(orderIds);
        List<Order> updateOrders = new ArrayList<>();
        for (EsOrderBO esOrderBO : orderList) {
            Order update = new Order();
            update.setOrderId(esOrderBO.getOrderId());
            BigDecimal distributionAmount = new BigDecimal(BigInteger.ZERO.toString());
            BigDecimal developingAmount = new BigDecimal(BigInteger.ZERO.toString());
            for (EsOrderItemBO item : message) {
                distributionAmount = distributionAmount.add(new BigDecimal(item.getDistributionAmount().toString()));
                developingAmount = developingAmount.add(new BigDecimal(item.getDistributionParentAmount().toString()));
            }
            update.setDistributionAmount(distributionAmount.longValue());
            update.setDevelopingAmount(developingAmount.longValue());
            updateOrders.add(update);
        }
        // 修改订单分销金额
        orderMapper.updateBatchDistributionAmount(updateOrders);
    }

    @Override
    public List<OrderItemVO> listOrderItems(OrderItem orderItemDTO) {
        return orderItemMapper.listOrderItems(orderItemDTO);
    }

    @Override
    public List<OrderItemVO> listOrderItemByIds(List<Long> orderItemIds) {
        return orderItemMapper.listOrderItemByIds(orderItemIds);
    }

    @Override
    public OrderItemVO getOrderItemByOrderNumberAndSkuId(String orderNumber, Long skuId) {
        return orderItemMapper.getOrderItemByOrderNumberAndSkuId(orderNumber, skuId);
    }

    @Override
    public int jointVentureCommissionOrderItemSettled(List<Long> orderIds, Integer jointVentureCommissionStatus, Integer jointVentureRefundStatus) {
        return orderItemMapper.jointVentureCommissionOrderItemSettled(orderIds, jointVentureCommissionStatus, jointVentureRefundStatus);
    }

    @Override
    public int updateDistributionRefundStatusBatchByOrderItemId(List<Long> orderItemIds, Integer distributionRefundStatus) {
        if (CollectionUtils.isEmpty(orderItemIds)) {
            return 0;
        }
        return orderItemMapper.updateDistributionRefundStatusBatchByOrderItemId(orderItemIds, distributionRefundStatus);
    }

    @Override
    public int updateDistributionRefundStatusByOrderId(Long orderId, Integer distributionRefundStatus) {
        return orderItemMapper.updateDistributionRefundStatusByOrderId(orderId, distributionRefundStatus);
    }
}
