package com.mall4j.cloud.delivery.service.impl;

import com.google.common.collect.Lists;
import com.mall4j.cloud.common.cache.constant.CacheNames;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.delivery.mapper.AreaMapper;
import com.mall4j.cloud.delivery.model.Area;
import com.mall4j.cloud.delivery.service.AreaService;
import com.mall4j.cloud.api.delivery.vo.AreaVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 省市区地区信息
 *
 * @author YXF
 * @date 2020-11-25 14:48:52
 */
@Service
public class AreaServiceImpl implements AreaService {

    @Autowired
    private AreaMapper areaMapper;

    @Override
    public List<AreaVO> list() {
        return areaMapper.list();
    }

    @Override
    @Cacheable(cacheNames = CacheNames.AREA_INFO_KEY, key = "'areaList'", sync = true)
    public List<AreaVO> getAreaListInfo() {
        List<AreaVO> areaList = areaMapper.getAreaListInfo();
        for (AreaVO province:areaList){
            List<Long> cityAll = Lists.newArrayList();
            for (AreaVO city:province.getAreas()){
                cityAll.add(city.getAreaId());
                List<Long> areaAll = Lists.newArrayList();
                for (AreaVO area:city.getAreas()){
                    areaAll.add(area.getAreaId());
                }
                city.setAreaIds(areaAll);
            }
            province.setAreaIds(cityAll);
        }
        return areaList;
    }

    @Override
    public List<AreaVO> listAreaOfEnable() {
        List<AreaVO> list = areaMapper.listAreaOfEnable();
        return list;
    }

    @Override
    public Integer countByNameAndParentId(String areaName, Integer level, Long areaId) {
        return areaMapper.countByNameAndParentId(areaName,level,areaId);
    }

    @Override
    public AreaVO getByAreaId(Long areaId) {
        return areaMapper.getByAreaId(areaId);
    }

    @Override
    public void save(Area area) {
        areaMapper.save(area);
    }

    @Override
    public void update(Area area) {
        areaMapper.update(area);
    }

    @Override
    public void deleteById(Long areaId) {
        int areaNum = areaMapper.countByAreaId(areaId);
        if (areaNum > 0) {
            throw new LuckException("请先删除子地区");
        }
        areaMapper.deleteById(areaId);
    }

    @Override
    @Cacheable(cacheNames = CacheNames.AREA_KEY, key = "'list:' + #pid")
    public List<AreaVO> listByPid(Long pid) {
        return areaMapper.listByPid(pid);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = CacheNames.AREA_INFO_KEY, key = "'areaList'"),
            @CacheEvict(cacheNames = CacheNames.AREA_KEY, key = "'list:' + #pid")
    })
    public void removeAllCache(Long pid) {

    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = CacheNames.AREA_KEY, key = "'list:' + #pid"),
            @CacheEvict(cacheNames = CacheNames.AREA_INFO_KEY, key = "'areaList'")
    })
    public void removeAreaCacheByParentId(Long pid) {

    }
}
