package com.mall4j.cloud.product.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.ons.api.SendResult;
import com.mall4j.cloud.api.biz.feign.ChannelsSpuFeignClient;
import com.mall4j.cloud.api.group.feign.GroupFeignClient;
import com.mall4j.cloud.api.group.feign.dto.OrderGiftReduceAppDTO;
import com.mall4j.cloud.api.order.bo.EsOrderBO;
import com.mall4j.cloud.api.order.bo.EsOrderItemBO;
import com.mall4j.cloud.api.order.bo.OrderStatusBO;
import com.mall4j.cloud.api.order.constant.OrderStatus;
import com.mall4j.cloud.api.order.feign.OrderFeignClient;
import com.mall4j.cloud.api.product.dto.SkuStockLockDTO;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.order.vo.OrderItemVO;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.product.bo.SkuWithStockBO;
import com.mall4j.cloud.product.mapper.SkuStockLockMapper;
import com.mall4j.cloud.product.mapper.SkuStockMapper;
import com.mall4j.cloud.product.mapper.SkuStoreMapper;
import com.mall4j.cloud.product.mapper.SpuExtensionMapper;
import com.mall4j.cloud.product.model.SkuStockLock;
import com.mall4j.cloud.product.service.SkuStockLockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 库存锁定信息
 *
 * @author FrozenWatermelon
 * @date 2020-12-22 16:12:10
 */
@Service
@Slf4j
public class SkuStockLockServiceImpl implements SkuStockLockService {

    @Autowired
    private SkuStockLockMapper skuStockLockMapper;

    @Autowired
    private SpuExtensionMapper spuExtensionMapper;

    @Autowired
    private SkuStockMapper skuStockMapper;

    @Autowired
    private SkuStoreMapper skuStoreMapper;

    @Autowired
    private OrderFeignClient orderFeignClient;

    @Autowired
    private OnsMQTemplate stockMqTemplate;
    @Autowired
    GroupFeignClient groupFeignClient;

    @Autowired
    private ChannelsSpuFeignClient channelsSpuFeignClient;

    @Override
    public PageVO<SkuStockLock> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> skuStockLockMapper.list());
    }

    @Override
    public SkuStockLock getById(Long id) {
        return skuStockLockMapper.getById(id);
    }

    @Override
    public void save(SkuStockLock skuStockLock) {
        skuStockLockMapper.save(skuStockLock);
    }

    @Override
    public void update(SkuStockLock skuStockLock) {
        skuStockLockMapper.update(skuStockLock);
    }

    @Override
    public void deleteById(Long id) {
        skuStockLockMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Void> lock(List<SkuStockLockDTO> skuStockLocksParam) {

        List<SkuStockLock> skuStockLocks = new ArrayList<>();
        for (SkuStockLockDTO skuStockLockDTO : skuStockLocksParam) {
            SkuStockLock skuStockLock = new SkuStockLock();
            skuStockLock.setCount(skuStockLockDTO.getCount());
            skuStockLock.setOrderId(skuStockLockDTO.getOrderId());
            skuStockLock.setSkuId(skuStockLockDTO.getSkuId());
            skuStockLock.setSpuId(skuStockLockDTO.getSpuId());
            skuStockLock.setStatus(0);
            skuStockLocks.add(skuStockLock);

            // 减sku库存
            int skuStockUpdateIsSuccess = skuStockMapper.reduceStockByOrder(skuStockLockDTO.getSkuId(), skuStockLockDTO.getCount());

            if (skuStockUpdateIsSuccess < 1) {
                throw new LuckException(ResponseEnum.NOT_STOCK, skuStockLockDTO.getSkuId());
            }

            // 减商品库存
            int spuStockUpdateIsSuccess = spuExtensionMapper.reduceStockByOrder(skuStockLockDTO.getSpuId(), skuStockLockDTO.getCount());
            if (spuStockUpdateIsSuccess < 1) {
                throw new LuckException(ResponseEnum.NOT_STOCK, skuStockLockDTO.getSkuId());
            }
        }

        // 保存库存锁定信息
        skuStockLockMapper.saveBatch(skuStockLocks);

        List<Long> orderIds = skuStockLocksParam.stream().map(SkuStockLockDTO::getOrderId).collect(Collectors.toList());

        // 一个小时后解锁库存 修改为半个小时
//        SendResult sendResult = stockMqTemplate.syncSend(orderIds, RocketMqConstant.CANCEL_ORDER_DELAY_LEVEL);
//        if (sendResult == null || sendResult.getMessageId() == null) {
//            // 消息发不出去就抛异常，发的出去无所谓
//            throw new LuckException(ResponseEnum.EXCEPTION);
//        }

        return ServerResponseEntity.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unlockStock(List<Long> orderIds) {
        log.info("[解锁业务]开始");
//        ServerResponseEntity<List<OrderStatusBO>> ordersStatusResponse = orderFeignClient.getOrdersStatus(orderIds);
//        if (!ordersStatusResponse.isSuccess()) {
//            throw new LuckException(ordersStatusResponse.getMsg());
//        }
//        List<OrderStatusBO> orderStatusList = ordersStatusResponse.getData();
        log.info("[解锁业务]-----1");
        List<Long> needUnLockOrderIds = orderIds;
//        for (OrderStatusBO orderStatusBO : orderStatusList) {
//            // 该订单没有下单成功，或订单已取消，赶紧解锁库存
//            if (orderStatusBO.getStatus() == null || Objects.equals(orderStatusBO.getStatus(), OrderStatus.CLOSE.value())) {
//                needUnLockOrderIds.add(orderStatusBO.getOrderId());
//            }
//        }
        log.info("[解锁业务]-----2");
        if (CollectionUtil.isEmpty(needUnLockOrderIds)) {
            return;
        }
        log.info("[解锁业务]-----3");
        List<SkuWithStockBO> allSkuWithStocks = skuStockLockMapper.listByOrderIds(needUnLockOrderIds);
        if (CollectionUtil.isEmpty(allSkuWithStocks)) {
            return;
        }
        List<Long> lockIds = allSkuWithStocks.stream().map(SkuWithStockBO::getId).collect(Collectors.toList());
        log.info("[解锁业务]-----4");
        // 还原商品库存
        spuExtensionMapper.addStockByOrder(allSkuWithStocks);
        // 还原sku库存
        skuStockMapper.addStockByOrder(allSkuWithStocks);
        //还原总店库存
        skuStoreMapper.addStockByOrder(allSkuWithStocks, Constant.MAIN_SHOP);
        // 将锁定状态标记为已解锁
        int updateStatus = skuStockLockMapper.unLockByIds(lockIds);
        log.info("[解锁业务]-----5");
        if (updateStatus == 0) {
            throw new LuckException(ResponseEnum.EXCEPTION);
        }

        //赠品库存还原
        ServerResponseEntity<List<OrderItemVO>> serverResponse = orderFeignClient.getOrderItemsByOrderIds(orderIds);
        if(serverResponse.isFail() || serverResponse.getData()==null || CollectionUtil.isEmpty(serverResponse.getData())){
            log.info("扣减库存查询订单失败。订单id:{}", JSONObject.toJSONString(orderIds));
        }


        List<OrderItemVO> orderItemVOS = serverResponse.getData();
        // 过滤掉非赠品商品
        orderItemVOS = orderItemVOS.stream().filter(o -> o.getGiftActivityId() !=null && o.getType() == 1).collect(Collectors.toList());

        ArrayList<OrderGiftReduceAppDTO> orderGiftReduceAppDTOS = new ArrayList<>();
        for (OrderItemVO orderItemVO : orderItemVOS) {
            OrderGiftReduceAppDTO orderGiftReduceAppDTO = new OrderGiftReduceAppDTO();
            orderGiftReduceAppDTO.setActivityId(orderItemVO.getGiftActivityId().intValue());
            orderGiftReduceAppDTO.setCommodityId(orderItemVO.getSpuId());
            orderGiftReduceAppDTO.setNum(orderItemVO.getCount());
            orderGiftReduceAppDTOS.add(orderGiftReduceAppDTO);
        }
        groupFeignClient.unLockStock(orderGiftReduceAppDTOS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markerStockUse(List<Long> orderIds) {

        List<SkuWithStockBO> skuWithStocks = skuStockLockMapper.listByOrderIds(orderIds);

        //  ==============订单从正常状态变成已支付=============
        if (CollectionUtil.isNotEmpty(skuWithStocks)) {
            // 减少商品实际库存，增加销量
            spuExtensionMapper.reduceActualStockByOrder(skuWithStocks);
            // 减少sku实际库存
            skuStockMapper.reduceActualStockByOrder(skuWithStocks);

            int updateStatus = skuStockLockMapper.markerStockUse(orderIds, 0);
            if (updateStatus == 0) {
                throw new LuckException(ResponseEnum.EXCEPTION);
            }
        }

        // ================ 由于订单支付回调成功过慢，导致订单由取消变成已支付 ====================

        List<SkuWithStockBO> unLockSkuWithStocks = skuStockLockMapper.listUnLockByOrderIds(orderIds);

        if (CollectionUtil.isNotEmpty(unLockSkuWithStocks)) {
            // 减少商品实际库存，增加销量
            //todo 商品销量增加失败
            spuExtensionMapper.reduceActualStockByCancelOrder(unLockSkuWithStocks);
            // 减少sku实际库存
            skuStockMapper.reduceActualStockByCancelOrder(unLockSkuWithStocks);

            int updateStatus = skuStockLockMapper.markerStockUse(orderIds, -1);
            if (updateStatus == 0) {
                throw new LuckException(ResponseEnum.EXCEPTION);
            }
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void ecOrderDeduction(Long orderId) {
        ServerResponseEntity<EsOrderBO> orderBOServerResponseEntity = orderFeignClient.getEsOrder(orderId);
        if(orderBOServerResponseEntity==null || orderBOServerResponseEntity.isFail() || orderBOServerResponseEntity.getData()==null){
            Assert.faild(StrUtil.format("订单查询失败，orderId:{}",orderId));
        }

        EsOrderBO orderBO = orderBOServerResponseEntity.getData();
        List<EsOrderItemBO> orderItemBOS = orderBO.getOrderItems();
        for (EsOrderItemBO orderItemBO : orderItemBOS) {
            spuExtensionMapper.reduceEcChannelsStockBySpuId(orderItemBO.getSpuId(),orderItemBO.getCount());
            skuStockMapper.reduceEcChannelsStockByskuId(orderItemBO.getSkuId(),orderItemBO.getCount());
            channelsSpuFeignClient.reduceChannelsStockBySkuId(orderItemBO.getSkuId(),orderItemBO.getCount());
        }
    }
}
