package com.mall4j.cloud.order.feign;

import com.mall4j.cloud.api.order.feign.OrderCommFeignClient;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.order.vo.OrderItemLangVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.order.model.OrderItem;
import com.mall4j.cloud.order.service.OrderItemLangService;
import com.mall4j.cloud.order.service.OrderItemService;
import com.mall4j.cloud.common.order.vo.OrderItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * @author FrozenWatermelon
 * @date 2020/12/25
 */
@RestController
public class OrderCommFeignController implements OrderCommFeignClient {


    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OrderItemLangService orderItemLangService;


    @Override
    public ServerResponseEntity<Long> getSpuIdByOrderItemId(Long orderItemId){
        OrderItemVO orderItem = orderItemService.getByOrderItemId(orderItemId);
        if (Objects.isNull(orderItem)) {
            return ServerResponseEntity.showFailMsg("订单项不存在");
        }
        if (Objects.equals(orderItem.getIsComm(), Constant.IS_COMM)) {
            return ServerResponseEntity.showFailMsg("[" +orderItem.getSpuName()+"]" +"该订单项已评论，请勿重复评论");
        }
        return ServerResponseEntity.success(orderItem.getSpuId());
    }

    @Override
    public void updateOrderItemComm(Long orderItemId) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderItemId(orderItemId);
        orderItem.setIsComm(Constant.IS_COMM);
        orderItemService.update(orderItem);
    }

    @Override
    public ServerResponseEntity<OrderItemVO> getOrderItemByOrderItemId(Long orderItemId) {
        return ServerResponseEntity.success(orderItemService.getSpuInfoByOrderItemId(orderItemId));
    }

    @Override
    public ServerResponseEntity<OrderItemVO> getByOrderItemId(Long orderItemId) {
        return ServerResponseEntity.success(orderItemService.getByOrderItemId(orderItemId));
    }

    @Override
    public ServerResponseEntity<List<OrderItemLangVO>> listOrderItemLangByIds(List<Long> orderItemIds) {
        List<OrderItemLangVO> res = orderItemLangService.listOrderItemLangByIds(orderItemIds);
        return ServerResponseEntity.success(res);
    }

    @Override
    public ServerResponseEntity<List<OrderItemVO>> listOrderItemByIds(List<Long> orderItemIds) {
        List<OrderItemVO> res = orderItemService.listOrderItemByIds(orderItemIds);
        return ServerResponseEntity.success(res);
    }
}
