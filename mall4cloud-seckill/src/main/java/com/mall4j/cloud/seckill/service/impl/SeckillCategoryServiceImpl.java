package com.mall4j.cloud.seckill.service.impl;

import com.mall4j.cloud.common.cache.constant.SeckillCacheNames;
import com.mall4j.cloud.seckill.model.SeckillCategory;
import com.mall4j.cloud.seckill.mapper.SeckillCategoryMapper;
import com.mall4j.cloud.seckill.service.SeckillCategoryService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 秒杀分类信息
 *
 * @author lhd
 * @date 2021-04-19 11:26:35
 */
@Service
public class SeckillCategoryServiceImpl implements SeckillCategoryService {

    @Autowired
    private SeckillCategoryMapper seckillCategoryMapper;

    @Override
    @Cacheable(cacheNames = SeckillCacheNames.CATEGORY_LIST, key = "'categoryList'" , sync = true)
    public List<SeckillCategory> list() {
        return seckillCategoryMapper.list();
    }

    @Override
    public SeckillCategory getByCategoryId(Long categoryId) {
        return seckillCategoryMapper.getByCategoryId(categoryId);
    }

    @Override
    @CacheEvict(cacheNames = SeckillCacheNames.CATEGORY_LIST, key =  "'categoryList'")
    public void save(SeckillCategory seckillCategory) {
        seckillCategoryMapper.save(seckillCategory);
    }

    @Override
    @CacheEvict(cacheNames = SeckillCacheNames.CATEGORY_LIST, key =  "'categoryList'")
    public void update(SeckillCategory seckillCategory) {
        seckillCategoryMapper.update(seckillCategory);
    }

    @Override
    @CacheEvict(cacheNames = SeckillCacheNames.CATEGORY_LIST, key =  "'categoryList'")
    public void deleteById(Long categoryId) {
        seckillCategoryMapper.deleteById(categoryId);
    }

    @Override
    public Integer countByName(String name, Long categoryId) {
        return seckillCategoryMapper.countByName(name,categoryId);
    }

    @Override
    @CacheEvict(cacheNames = SeckillCacheNames.CATEGORY_LIST, key =  "'categoryList'")
    public void updateBatch(List<SeckillCategory> seckillCategoryList) {
        seckillCategoryMapper.updateBatch(seckillCategoryList);
    }
}
