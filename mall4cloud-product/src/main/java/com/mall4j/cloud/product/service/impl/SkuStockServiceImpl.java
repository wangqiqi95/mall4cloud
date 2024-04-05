package com.mall4j.cloud.product.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.common.cache.constant.CacheNames;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.order.bo.RefundReductionStockBO;
import com.mall4j.cloud.common.product.dto.SkuDTO;
import com.mall4j.cloud.common.product.vo.SkuVO;
import com.mall4j.cloud.product.mapper.SkuStockMapper;
import com.mall4j.cloud.product.mapper.SpuExtensionMapper;
import com.mall4j.cloud.product.model.Sku;
import com.mall4j.cloud.product.model.SkuStock;
import com.mall4j.cloud.product.service.SkuStockService;
import com.mall4j.cloud.product.vo.SkuStockVO;
import com.mall4j.cloud.product.vo.SpuStockVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 库存信息
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
@Service
public class SkuStockServiceImpl extends ServiceImpl<SkuStockMapper, SkuStock> implements SkuStockService {

    @Autowired
    private SkuStockMapper skuStockMapper;

    @Autowired
    private SpuExtensionMapper spuExtensionMapper;

    @Override
    public void saveSkuStock(SkuStock skuStock) {
        skuStockMapper.saveSkuStock(skuStock);
    }

    @Override
    public void updateSkuStock(SkuStock skuStock) {
        skuStockMapper.updateSkuStock(skuStock);
    }

    @Override
    public void deleteById(Long stockId) {
        skuStockMapper.deleteById(stockId);
    }

    @Override
    public void batchSave(List<SkuStock> skuStocks) {
        skuStockMapper.batchSave(skuStocks);
    }

    @Override
    public void deleteBySkuIds(List<Long> skuIds) {
        skuStockMapper.deleteBySkuIds(skuIds);
    }

    @Override
    public List<SkuStockVO> listBySkuList(List<SkuVO> skuVOList) {
        return skuStockMapper.listBySkuList(skuVOList);
    }

    @Override
    public void updateBatch(List<SkuDTO> skuList) {
        if (CollUtil.isEmpty(skuList)) {
            return;
        }
        // 如果是修改库存，此时不需要改变锁定库存
        List<SkuStock> skuStocks = new ArrayList<>();
        for (SkuDTO sku : skuList) {
            SkuStock skuStock = new SkuStock();
            if (Objects.nonNull(sku.getChangeStock()) && sku.getChangeStock() != 0) {
                skuStock.setStock(sku.getChangeStock());
                skuStock.setSkuId(sku.getSkuId());
                skuStocks.add(skuStock);
            }
        }
        if (CollUtil.isNotEmpty(skuStocks)) {
            int updateNum = skuStockMapper.updateStock(skuStocks);
            if (updateNum == 0) {
                throw new LuckException("sku库存发生改变，请刷新后重试");
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reductionStock(List<RefundReductionStockBO> refundReductionStocks) {
        // 还原商品实际库存，减少销量
        spuExtensionMapper.reductionActualStockByRefund(refundReductionStocks);
        // 还原sku实际库存
        skuStockMapper.reductionActualStockByCancelOrder(refundReductionStocks);
    }


    @Override
    @Cacheable(cacheNames = CacheNames.SKU_STOCK_LIST_KEY, key = "#spuId")
    public List<SkuStockVO> listStockBySpuId(Long spuId) {
        return skuStockMapper.listStockBySpuId(spuId);
    }

    @Override
    public void resetStock(List<Sku> skuSaveList) {
        List<Long> skuIdList = skuSaveList.stream().map(Sku::getSkuId).collect(Collectors.toList());
        List<SkuStock> listDb = this.list(new LambdaQueryWrapper<SkuStock>().in(SkuStock::getSkuId, skuIdList));
        List<Long> skuIdDbList = listDb.stream().map(SkuStock::getSkuId).collect(Collectors.toList());
        ArrayList<SkuStock> skuStocks = new ArrayList<>();
        Date now = new Date();
        skuSaveList.forEach(sku -> {
            if (!skuIdDbList.contains(sku.getSkuId())) {
                SkuStock skuStock = new SkuStock();
                skuStock.setActualStock(0);
                skuStock.setLockStock(0);
                skuStock.setStock(0);
                skuStock.setSpuId(sku.getSpuId());
                skuStock.setSkuId(sku.getSkuId());
                skuStock.setCreateTime(now);
                skuStock.setUpdateTime(now);
                skuStocks.add(skuStock);
            }
        });
        this.saveBatch(skuStocks);
    }

    @Override
    public List<SpuStockVO> sumStockBySpuIds(List<Long> spuIds) {
        return skuStockMapper.sumStockBySpuIds(spuIds);
    }

    @Override
    public void updateSkuChannelsStock(Long skuId, Integer stock) {
        skuStockMapper.updateSkuChannelsStock(skuId, stock);
    }
}
