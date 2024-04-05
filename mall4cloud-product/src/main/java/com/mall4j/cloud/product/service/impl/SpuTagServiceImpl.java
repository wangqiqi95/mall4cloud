package com.mall4j.cloud.product.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.mall4j.cloud.api.feign.SearchSpuFeignClient;
import com.mall4j.cloud.common.cache.constant.CacheNames;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.product.dto.ProductSearchLimitDTO;
import com.mall4j.cloud.common.product.vo.search.SpuSearchVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.product.dto.SpuTagDTO;
import com.mall4j.cloud.product.mapper.SpuTagMapper;
import com.mall4j.cloud.product.model.SpuTag;
import com.mall4j.cloud.product.service.SpuTagService;
import com.mall4j.cloud.product.vo.SpuTagVO;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品分组表
 *
 * @author lhd
 * @date 2021-02-20 14:28:10
 */
@Service
public class SpuTagServiceImpl implements SpuTagService {

    @Autowired
    private SpuTagMapper spuTagMapper;

    @Autowired
    private SearchSpuFeignClient searchSpuFeignClient;

    @Override
    public SpuTagVO getById(Long id) {
        return spuTagMapper.getById(id);
    }

    @Override
    public void save(SpuTag spuTag) {
        spuTagMapper.save(spuTag);
    }

    @Override
    public void update(SpuTag spuTag) {
        spuTagMapper.update(spuTag);
    }

    @Override
    public void deleteById(Long id) {
        spuTagMapper.deleteById(id);
    }

    @Override
    public PageVO<SpuTagVO> pageByTitle(PageDTO pageDTO, SpuTagDTO spuTagDTO) {
        return PageUtil.doPage(pageDTO, () -> spuTagMapper.list(spuTagDTO));
    }

    @Override
    public List<SpuTagVO> listByTitle(SpuTagDTO spuTagDTO) {
        return spuTagMapper.list(spuTagDTO);
    }

    @Override
    public void updateProdCountById(Long id) {
        spuTagMapper.updateProdCountById(id);
    }

    @Override
    public void batchUpdateProdCountById(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        spuTagMapper.batchUpdateProdCountById(ids);
    }

    @Override
    @Cacheable(cacheNames = CacheNames.SPU_TAG_BY_SHOP, key = "#shopId")
    public List<SpuTagVO> listByShopId(Long shopId) {
        List<SpuTagVO> spuTagList = spuTagMapper.listByShopId(shopId);
        if (CollUtil.isEmpty(spuTagList)) {
            return new ArrayList<>();
        }
        Set<Long> spuIds = new HashSet<>();
        Iterator<SpuTagVO> iterator = spuTagList.iterator();
        while (iterator.hasNext()) {
            SpuTagVO spuTagVO = iterator.next();
            if (CollUtil.isEmpty(spuTagVO.getSpuList())) {
                iterator.remove();
                continue;
            }
            for (SpuSearchVO spuSearchVO : spuTagVO.getSpuList()) {
                spuIds.add(spuSearchVO.getSpuId());
            }
        }
        ServerResponseEntity<List<SpuSearchVO>> spuResponse = searchSpuFeignClient.listSpuBySpuIds(new ArrayList<>(spuIds));
        if (spuResponse.isFail()) {
            throw new LuckException(spuResponse.getMsg());
        }
        Map<Long, SpuSearchVO> spuMap = spuResponse.getData().stream().collect(Collectors.toMap(SpuSearchVO::getSpuId, s -> s));
        for (SpuTagVO spuTagVO : spuTagList) {
            for (SpuSearchVO spuSearchVO : spuTagVO.getSpuList()) {
                SpuSearchVO spu = spuMap.get(spuSearchVO.getSpuId());
                if (Objects.isNull(spu)) {
                    continue;
                }
                spuSearchVO.setMainImgUrl(spu.getMainImgUrl());
                spuSearchVO.setMarketPriceFee(spu.getMarketPriceFee());
                spuSearchVO.setPriceFee(spu.getPriceFee());
                spuSearchVO.setSellingPoint(spu.getSellingPoint());
                spuSearchVO.setShopId(spu.getShopId());
                spuSearchVO.setSpuName(spu.getSpuName());
            }
        }
        return spuTagList;
    }

    @Override
    @CacheEvict(cacheNames = CacheNames.SPU_TAG_BY_SHOP, key = "#shopId")
    public void removeCacheByShopId(Long shopId) {

    }
}
