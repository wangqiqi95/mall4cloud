package com.mall4j.cloud.order.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.i18n.I18nMessage;
import com.mall4j.cloud.common.order.vo.OrderItemLangVO;
import com.mall4j.cloud.order.model.OrderItem;
import com.mall4j.cloud.order.model.OrderItemLang;
import com.mall4j.cloud.order.mapper.OrderItemLangMapper;
import com.mall4j.cloud.order.service.OrderItemLangService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单项-国际化
 *
 * @author YXF
 * @date 2021-05-17 15:26:54
 */
@Service
public class OrderItemLangServiceImpl implements OrderItemLangService {

    @Autowired
    private OrderItemLangMapper orderItemLangMapper;

    @Override
    public PageVO<OrderItemLang> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> orderItemLangMapper.list());
    }

    @Override
    public OrderItemLang getByOrderItemId(Long orderItemId) {
        return orderItemLangMapper.getByOrderItemId(orderItemId);
    }

    @Override
    public void save(OrderItemLang orderItemLang) {
        orderItemLangMapper.save(orderItemLang);
    }

    @Override
    public void update(OrderItemLang orderItemLang) {
        orderItemLangMapper.update(orderItemLang);
    }

    @Override
    public void deleteById(Long orderItemId) {
        orderItemLangMapper.deleteById(orderItemId);
    }

    @Override
    public void saveOrderItemLang(List<OrderItem> orderItems) {
        List<OrderItemLang> orderItemLangList = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            for (OrderItemLang orderItemLang : orderItem.getOrderItemLangList()) {
                orderItemLang.setOrderItemId(orderItem.getOrderItemId());
                orderItemLangList.add(orderItemLang);
            }
        }
        orderItemLangMapper.saveBatch(orderItemLangList);
    }

    @Override
    public List<OrderItemLangVO> listOrderItemLangByIds(List<Long> orderItemIds) {
        return orderItemLangMapper.listOrderItemLangByIds(orderItemIds, I18nMessage.getLang());
    }
}
