package com.mall4j.cloud.order.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.mall4j.cloud.common.order.dto.OrderInvoiceDTO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.i18n.I18nMessage;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.order.mapper.OrderInvoiceMapper;
import com.mall4j.cloud.order.mapper.OrderMapper;
import com.mall4j.cloud.order.model.OrderInvoice;
import com.mall4j.cloud.order.service.OrderInvoiceService;
import com.mall4j.cloud.order.vo.OrderInvoiceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Pineapple
 * @date 2021/8/2 8:58
 */
@Service
public class OrderInvoiceServiceImpl implements OrderInvoiceService {

    @Autowired
    private OrderInvoiceMapper orderInvoiceMapper;
    @Autowired
    private OrderMapper orderMapper;

    @Override
    public PageVO<OrderInvoice> page(PageDTO pageDTO, OrderInvoiceDTO orderInvoiceDTO) {
        return PageUtil.doPage(pageDTO, () -> orderInvoiceMapper.listByShopId(orderInvoiceDTO));
    }

    @Override
    public PageVO<OrderInvoiceVO> pageUserInvoice(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> orderInvoiceMapper.listUserInvoice(AuthUserContext.get().getUserId(), I18nMessage.getLang()));
    }

    @Override
    public OrderInvoice getByOrderInvoiceId(Long orderInvoiceId) {
        return orderInvoiceMapper.getByOrderInvoiceId(orderInvoiceId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(OrderInvoice orderInvoice) {
        orderInvoiceMapper.save(orderInvoice);
        orderMapper.updateOrderTime(orderInvoice.getOrderId());
    }

    @Override
    public void update(OrderInvoice orderInvoice) {
        orderInvoiceMapper.update(orderInvoice);
    }

    @Override
    public void deleteById(Long orderInvoiceId) {
        orderInvoiceMapper.deleteById(orderInvoiceId);
    }

    @Override
    public OrderInvoiceVO getById(Long orderInvoiceId) {
        return orderInvoiceMapper.getById(orderInvoiceId, I18nMessage.getLang());
    }

    @Override
    public Long getByOrderId(Long orderId) {
        return orderInvoiceMapper.getByOrderId(orderId);
    }

    @Override
    public boolean isUpload(Long orderId) {
        return orderInvoiceMapper.isUpload(orderId) >= 1;
    }

    @Override
    public void deleteBatch(List<Long> orderIds) {
        ArrayList<Long> orderDeleteIds = new ArrayList<>();
        for (Long orderId : orderIds) {
            if (Objects.nonNull(orderInvoiceMapper.getByOrderId(orderId))){
                //如果该订单有发票信息
                orderDeleteIds.add(orderId);
            }
        }
        if (CollectionUtil.isNotEmpty(orderDeleteIds)){
            orderInvoiceMapper.deleteBatch(orderDeleteIds);
        }
    }
}
