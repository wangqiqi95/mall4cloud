package com.mall4j.cloud.seckill.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.order.vo.ShopCartItemVO;
import com.mall4j.cloud.common.order.vo.ShopCartOrderMergerVO;
import com.mall4j.cloud.common.order.vo.ShopCartOrderVO;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.seckill.mapper.SeckillMapper;
import com.mall4j.cloud.seckill.mapper.SeckillSkuMapper;
import com.mall4j.cloud.seckill.model.SeckillOrder;
import com.mall4j.cloud.seckill.mapper.SeckillOrderMapper;
import com.mall4j.cloud.seckill.service.SeckillOrderService;
import com.mall4j.cloud.seckill.vo.SeckillOrderVO;
import com.mall4j.cloud.seckill.vo.SeckillVO;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;

/**
 * 秒杀订单
 *
 * @author lhd
 * @date 2021-03-30 14:59:28
 */
@Service
public class SeckillOrderServiceImpl implements SeckillOrderService {

    @Autowired
    private SeckillOrderMapper seckillOrderMapper;
    @Autowired
    private SeckillSkuMapper seckillSkuMapper;
    @Autowired
    private SeckillMapper seckillMapper;

    @Override
    public PageVO<SeckillOrderVO> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> seckillOrderMapper.list());
    }

    @Override
    public SeckillOrderVO getBySeckillOrderId(Long seckillOrderId) {
        return seckillOrderMapper.getBySeckillOrderId(seckillOrderId);
    }

    @Override
    public void save(SeckillOrder seckillOrder) {
        seckillOrderMapper.save(seckillOrder);
    }

    @Override
    public void update(SeckillOrder seckillOrder) {
        seckillOrderMapper.update(seckillOrder);
    }

    @Override
    public void deleteById(Long seckillOrderId) {
        seckillOrderMapper.deleteById(seckillOrderId);
    }

    @Override
    public boolean checkOrderSpuNum(SeckillVO seckill, Long userId, Integer count) {
        // 判断之前秒杀的商品有没有超过限制，-1表示商品不限制秒杀数量
        if (!Objects.equals(seckill.getMaxNum(), -1)) {
            int dbCount = seckillOrderMapper.selectNumByUser(seckill.getSeckillId(),seckill.getSpuId(),userId);
            // 本次秒杀商品限购
            return seckill.getMaxNum() >= count + dbCount;
        }
        return true;
    }

    @Override
    public void submit(ShopCartOrderMergerVO mergerOrder) {
        ShopCartOrderVO shopCartOrderVO = mergerOrder.getShopCartOrders().get(0);
        ShopCartItemVO shopCartItemVO = shopCartOrderVO.getShopCartItemDiscounts().get(0).getShopCartItems().get(0);

        int skuUpdateStatus = seckillSkuMapper.updateStocks(mergerOrder.getSeckillSkuId(), shopCartItemVO.getCount());
        if (skuUpdateStatus != 1) {
            throw new LuckException("库存不足");
        }
        // 更新秒杀sku库存，乐观锁
        // 保存秒杀订单信息
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setCreateTime(new Date());
        seckillOrder.setOrderId(shopCartOrderVO.getOrderId());
        seckillOrder.setProdCount(shopCartItemVO.getCount());
        seckillOrder.setSeckillId(mergerOrder.getSeckillId());
        seckillOrder.setUserId(mergerOrder.getUserId());
        seckillOrder.setSpuId(shopCartItemVO.getSpuId());
        seckillOrder.setSeckillSkuId(mergerOrder.getSeckillSkuId());
        // 秒杀成功等待支付
        seckillOrder.setState(0);
        seckillOrderMapper.save(seckillOrder);
        int seckillUpdateStatus = seckillMapper.updateStocksById(seckillOrder.getSeckillId(), seckillOrder.getProdCount());
        if (seckillUpdateStatus != 1) {
            throw new LuckException("库存不足");
        }
    }

    @Override
    public int countByOrderId(Long orderId) {
        return seckillOrderMapper.countByOrderId(orderId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelUnpayOrderByOrderId(Long orderId) {
        int count = seckillOrderMapper.countUnpayOrderByOrderId(orderId);
        if (count < 1) {
            return;
        }
        // 还原秒杀库存
        seckillSkuMapper.returnStocksByOrderId(orderId);

        // 还原秒杀库存
        seckillMapper.returnStocksByOrderId(orderId);

        int updateStatus = seckillOrderMapper.cancelUnpayOrderByOrderId(orderId);
        if (updateStatus == 0) {
            throw new LuckException(ResponseEnum.EXCEPTION);
        }
    }

    @Override
    public void paySuccessOrderByOrderId(Long orderId) {
        seckillOrderMapper.updateToPaySuccess(orderId);
    }

}
