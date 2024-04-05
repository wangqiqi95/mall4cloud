package com.mall4j.cloud.seckill.service.impl;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.mall4j.cloud.common.cache.constant.SeckillCacheNames;
import com.mall4j.cloud.common.cache.util.CacheManagerUtil;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.seckill.dto.SeckillSkuDTO;
import com.mall4j.cloud.seckill.mapper.SeckillSkuMapper;
import com.mall4j.cloud.seckill.service.SeckillSkuService;
import com.mall4j.cloud.seckill.vo.SeckillSkuVO;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 秒杀活动sku
 *
 * @author lhd
 * @date 2021-03-30 14:53:09
 */
@Service
public class SeckillSkuServiceImpl implements SeckillSkuService {

    @Autowired
    private SeckillSkuMapper seckillSkuMapper;

    @Autowired
    private CacheManagerUtil cacheManagerUtil;

    @Override
    public PageVO<SeckillSkuVO> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> seckillSkuMapper.list());
    }

    @Override
    @Cacheable(cacheNames = SeckillCacheNames.SECKILL_SKU_BY_ID, key = "#seckillSkuId", sync = true)
    public SeckillSkuVO getBySeckillSkuId(Long seckillSkuId) {
        return seckillSkuMapper.getBySeckillSkuId(seckillSkuId);
    }

    @Override
    @Cacheable(cacheNames = SeckillCacheNames.SECKILL_SKU_BY_SECKILL_ID, key = "#seckillId", sync = true)
    public List<SeckillSkuVO> listSeckillSkuBySeckillId(Long seckillId) {
        return seckillSkuMapper.selectListBySeckillId(seckillId);
    }

    @Override
    public void saveBatch(List<SeckillSkuDTO> seckillSkuList) {
        seckillSkuMapper.saveBatch(seckillSkuList);
    }

    @Override
    @CacheEvict(cacheNames = SeckillCacheNames.SECKILL_SKU_BY_SECKILL_ID, key = "#seckillId")
    public void removeSeckillSkuCacheBySeckillId(Long seckillId) {
        List<SeckillSkuVO> seckillSkus = listSeckillSkuBySeckillId(seckillId);
        //清除缓存
        if (CollectionUtils.isNotEmpty(seckillSkus)) {
            for (SeckillSkuVO seckillSku : seckillSkus) {
                // 不能采用this清除缓存，因为这样没有aop
                cacheManagerUtil.evictCache(SeckillCacheNames.SECKILL_SKU_BY_ID, String.valueOf(seckillSku.getSeckillSkuId()));
            }
        }
    }

}
