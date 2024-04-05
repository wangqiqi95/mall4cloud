package com.mall4j.cloud.seckill.service.impl;

import com.mall4j.cloud.seckill.mapper.SeckillStoreMapper;
import com.mall4j.cloud.seckill.model.SeckillStore;
import com.mall4j.cloud.seckill.service.SeckillStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeckillStoreServiceImpl implements SeckillStoreService {

    @Autowired
    private SeckillStoreMapper seckillStoreMapper;

    @Override
    public void saveBatch(List<SeckillStore> seckillStoreList) {
        seckillStoreMapper.saveBatch(seckillStoreList);
    }

    @Override
    public void deleteBySeckillId(Long seckillId) {
        seckillStoreMapper.deleteBySeckillId(seckillId);
    }

    @Override
    public List<SeckillStore> listBySeckillId(Long seckillId) {
        return seckillStoreMapper.listBySeckillId(seckillId);
    }

    @Override
    public SeckillStore findBySeckillIdAndStoreId(Long seckillId, Long storeId) {
        return seckillStoreMapper.findBySeckillIdAndStoreId(seckillId, storeId);
    }

    @Override
    public List<SeckillStore> listBySeckillIdList(List<Long> seckillIdList) {
        return seckillStoreMapper.listBySeckillIdList(seckillIdList);
    }
}
