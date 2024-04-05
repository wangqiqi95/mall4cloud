package com.mall4j.cloud.seckill.service;

import com.mall4j.cloud.seckill.model.SeckillStore;

import java.util.List;

public interface SeckillStoreService {

    void saveBatch(List<SeckillStore> seckillStoreList);

    void deleteBySeckillId(Long seckillId);

    List<SeckillStore> listBySeckillId(Long seckillId);

    SeckillStore findBySeckillIdAndStoreId(Long seckillId, Long storeId);

    List<SeckillStore> listBySeckillIdList(List<Long> seckillIdList);
}
