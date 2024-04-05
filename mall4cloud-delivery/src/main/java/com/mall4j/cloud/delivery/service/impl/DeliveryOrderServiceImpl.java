package com.mall4j.cloud.delivery.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.mall4j.cloud.api.delivery.dto.DeliveryOrderDTO;
import com.mall4j.cloud.api.delivery.dto.DeliveryOrderItemDTO;
import com.mall4j.cloud.api.delivery.vo.DeliveryCompanyVO;
import com.mall4j.cloud.api.delivery.vo.DeliveryInfoVO;
import com.mall4j.cloud.api.delivery.vo.DeliveryOrderFeignVO;
import com.mall4j.cloud.api.order.feign.OrderAddrFeignClient;
import com.mall4j.cloud.api.order.vo.OrderAddrVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.order.constant.DeliveryType;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.delivery.mapper.DeliveryOrderItemMapper;
import com.mall4j.cloud.delivery.mapper.DeliveryOrderMapper;
import com.mall4j.cloud.delivery.model.DeliveryOrder;
import com.mall4j.cloud.delivery.model.DeliveryOrderItem;
import com.mall4j.cloud.delivery.service.DeliveryCompanyService;
import com.mall4j.cloud.delivery.service.DeliveryOrderService;
import com.mall4j.cloud.delivery.vo.DeliveryOrderVO;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 订单快递信息
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:00
 */
@Service
public class DeliveryOrderServiceImpl implements DeliveryOrderService {

    @Autowired
    private DeliveryOrderMapper deliveryOrderMapper;

    @Autowired
    private DeliveryOrderItemMapper deliveryOrderItemMapper;

    @Autowired
    private DeliveryCompanyService deliveryCompanyService;

    @Autowired
    private OrderAddrFeignClient orderAddrFeignClient;

    @Autowired
    private MapperFacade mapperFacade;


    @Override
    public PageVO<DeliveryOrderVO> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> deliveryOrderMapper.list());
    }

    @Override
    public DeliveryOrderVO getByDeliveryOrderId(Long deliveryOrderId) {
        return deliveryOrderMapper.getByDeliveryOrderId(deliveryOrderId);
    }

    @Override
    public List<DeliveryOrderFeignVO> getByDeliveryByOrderId(Long orderId) throws UnsupportedEncodingException {
        List<DeliveryOrderFeignVO> deliveryOrderList = deliveryOrderMapper.getByDeliveryByOrderId(orderId);
        for (DeliveryOrderFeignVO deliveryOrderFeignVO : deliveryOrderList) {
            if (Objects.nonNull(deliveryOrderFeignVO.getDeliveryCompanyId())){
                DeliveryCompanyVO companyVO = deliveryCompanyService.getByDeliveryCompanyId(deliveryOrderFeignVO.getDeliveryCompanyId());
                deliveryOrderFeignVO.setDeliveryName(companyVO.getName());
            }
            if(!Objects.equals(deliveryOrderFeignVO.getDeliveryType(), DeliveryType.LOGISTICS.value())){
                DeliveryInfoVO deliveryInfoVO = deliveryCompanyService.query(deliveryOrderFeignVO.getDeliveryCompanyId(), deliveryOrderFeignVO.getDeliveryNo(), deliveryOrderFeignVO.getConsigneeMobile());
                deliveryOrderFeignVO.setDelivery(deliveryInfoVO);
            }
        }
        return deliveryOrderList;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveDeliveryInfo(DeliveryOrderDTO deliveryOrderParam) {
        boolean isDelivery = true;
        List<DeliveryOrderItemDTO> selectOrderItems = deliveryOrderParam.getSelectOrderItems();
        Long orderId = deliveryOrderParam.getOrderId();
        // 快递发货
        if (Objects.equals(DeliveryType.DELIVERY.value(), deliveryOrderParam.getDeliveryType())
                || Objects.equals(DeliveryType.NOT_DELIVERY.value(), deliveryOrderParam.getDeliveryType())
                || Objects.equals(DeliveryType.LOGISTICS.value(), deliveryOrderParam.getDeliveryType())){
            ServerResponseEntity<OrderAddrVO> orderAddrRes = orderAddrFeignClient.getOrderAddrByOrderId(orderId);
            if (orderAddrRes.isFail()) {
                throw new LuckException(orderAddrRes.getMsg());
            }
            OrderAddrVO orderAddrVO = orderAddrRes.getData();
            //校验数量及获取发货的总数
            int prodNumSum = getAllProdNum(selectOrderItems);
            DeliveryOrder deliveryOrder = new DeliveryOrder();
            deliveryOrder.setOrderId(deliveryOrderParam.getOrderId());
            if (Objects.equals(DeliveryType.DELIVERY.value(), deliveryOrderParam.getDeliveryType())){
                //只有快递配送才有物流公司和单号
                deliveryOrder.setDeliveryCompanyId(deliveryOrderParam.getDeliveryCompanyId());
                deliveryOrder.setDeliveryNo(deliveryOrderParam.getDeliveryNo());
            }
            deliveryOrder.setCreateTime(new Date());
            deliveryOrder.setStatus(1);
            deliveryOrder.setAllCount(prodNumSum);
            if (Objects.nonNull(orderAddrVO)) {
                // 保存收件人信息
                deliveryOrder.setConsigneeMobile(orderAddrVO.getMobile());
                deliveryOrder.setConsigneeName(orderAddrVO.getConsignee());
            }
            deliveryOrder.setDeliveryType(deliveryOrderParam.getDeliveryType());
            //保存订单物流信息
            deliveryOrderMapper.save(deliveryOrder);
            //保存需要添加的关联信息
            if(CollectionUtil.isNotEmpty(deliveryOrderParam.getSelectOrderItems())){
                List<DeliveryOrderItem> deliveryOrderItems = new ArrayList<>();
                for (DeliveryOrderItemDTO selectOrderItem : deliveryOrderParam.getSelectOrderItems()) {
                    DeliveryOrderItem deliveryOrderItem = new DeliveryOrderItem();
                    deliveryOrderItem.setDeliveryOrderId(deliveryOrder.getDeliveryOrderId());
                    deliveryOrderItem.setCount(selectOrderItem.getChangeNum());
                    deliveryOrderItem.setSpuName(selectOrderItem.getSpuName());
                    deliveryOrderItem.setImgUrl(selectOrderItem.getPic());
                    deliveryOrderItems.add(deliveryOrderItem);
                }
                deliveryOrderItemMapper.saveBatch(deliveryOrderItems);
            }
        }
    }

    @Override
    public List<DeliveryOrderVO> listDetailDelivery(Long orderId) {
        return deliveryOrderItemMapper.listDetailDelivery(orderId);
    }

    @Override
    public void updateOrderDeliveries(List<DeliveryOrderDTO> list) {
        deliveryOrderMapper.updateBatch(list);
    }

    private int getAllProdNum(List<DeliveryOrderItemDTO> selectOrderItems) {
        //获取发货总数
        int prodNumSum = 0;
        for (DeliveryOrderItemDTO selectOrderItem : selectOrderItems) {
            prodNumSum += selectOrderItem.getChangeNum();
        }
        return prodNumSum;
    }

    @Override
    public void save(DeliveryOrder deliveryOrder) {
        deliveryOrderMapper.save(deliveryOrder);
    }

    @Override
    public void update(DeliveryOrder deliveryOrder) {
        deliveryOrderMapper.update(deliveryOrder);
    }

    @Override
    public void deleteById(Long deliveryOrderId) {
        deliveryOrderMapper.deleteById(deliveryOrderId);
    }
}
