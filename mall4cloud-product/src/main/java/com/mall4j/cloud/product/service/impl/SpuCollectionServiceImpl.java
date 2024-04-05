package com.mall4j.cloud.product.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import com.mall4j.cloud.api.product.dto.SpuSkuPriceDTO;
import com.mall4j.cloud.api.product.vo.SkuTimeDiscountActivityVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.i18n.I18nMessage;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.product.dto.SkuPriceDTO;
import com.mall4j.cloud.product.dto.SpuAppPageVO;
import com.mall4j.cloud.product.model.SpuCollection;
import com.mall4j.cloud.product.mapper.SpuCollectionMapper;
import com.mall4j.cloud.product.service.SkuService;
import com.mall4j.cloud.product.service.SpuCollectionService;
import com.mall4j.cloud.product.service.SpuService;
import com.mall4j.cloud.product.service.SpuSkuPricingPriceService;
import com.mall4j.cloud.product.vo.SpuCollectionVO;
import com.mall4j.cloud.common.product.vo.app.SpuAppVO;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品收藏信息
 *
 * @author FrozenWatermelon
 * @date 2020-11-21 14:43:16
 */
@Slf4j
@Service
public class SpuCollectionServiceImpl implements SpuCollectionService {

    @Autowired
    private SpuCollectionMapper spuCollectionMapper;

    @Autowired
    private SpuService spuService;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private SkuService skuService;
    @Autowired
    private SpuSkuPricingPriceService spuSkuPricingPriceService;

    @Override
    public PageVO<SpuCollectionVO> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> spuCollectionMapper.list());
    }

    @Override
    public SpuCollectionVO getById(Long id) {
        return spuCollectionMapper.getById(id);
    }

    @Override
    public void save(SpuCollection spuCollection) {
        spuCollectionMapper.save(spuCollection);
    }

    @Override
    public void update(SpuCollection spuCollection) {
        spuCollectionMapper.update(spuCollection);
    }

    @Override
    public void deleteById(Long id) {
        spuCollectionMapper.deleteById(id);
    }

    @Override
    public SpuCollectionVO getBySpuId(Long spuId) {
        return null;
    }

    @Override
    public int userCollectionCount(Long spuId, Long userId) {
        return spuCollectionMapper.userCollectionCount(spuId, userId);
    }

    @Override
    public PageVO<SpuAppVO> getUserCollectionDtoPageByUserId(PageDTO pageDTO, Long userId, String spuName, Integer prodType,Long storeId) {
        PageVO<SpuAppVO> page = PageUtil.doPage(pageDTO, () -> spuCollectionMapper.getUserCollectionDtoPageByUserId(userId, spuName, I18nMessage.getLang(),prodType));

        //查询当前列表所有sku 信息
        List<SpuAppVO> result = page.getList();
        List<Long> spuIdList = result.stream().map(SpuAppVO::getSpuId).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(spuIdList)) {
            //查询所有sku满足限时调价的集合
            List<SkuPriceDTO> appSkuPriceBySkuIdList = skuService.getAppSkuPriceBySkuIdList(spuIdList, storeId);
            List<SpuSkuPriceDTO> skuPriceDTOS = mapperFacade.mapAsList(appSkuPriceBySkuIdList,SpuSkuPriceDTO.class);
            //增加价取价逻辑(小程序配置的活动价)
            List<SkuTimeDiscountActivityVO> activityVOList=spuSkuPricingPriceService.getStoreSpuAndSkuPrice(storeId,skuPriceDTOS);
            HashMap<Long, Long> spuMinPriceMap = new HashMap<>();
            if(activityVOList.size()>0){
                Map<Long, List<SkuTimeDiscountActivityVO>> skuMap = activityVOList.stream().collect(Collectors.groupingBy(SkuTimeDiscountActivityVO::getSkuId));
                appSkuPriceBySkuIdList.forEach(appSkuPriceDTO -> {
                    if (Objects.nonNull(skuMap.get(appSkuPriceDTO.getSkuId()))) {
                        appSkuPriceDTO.setPriceFee(skuMap.get(appSkuPriceDTO.getSkuId()).get(0).getPrice());
                    }
                });
            }
            //渠道sku最低价
            Map<Long, List<SkuPriceDTO>> spuMap = appSkuPriceBySkuIdList.stream().collect(Collectors.groupingBy(SkuPriceDTO::getSpuId));
            spuMap.forEach((k, v) -> {
                SkuPriceDTO skuPriceDTO = v.stream().min(Comparator.comparing(SkuPriceDTO::getPriceFee)).get();
                spuMinPriceMap.put(skuPriceDTO.getSpuId(), NumberUtil.min(skuPriceDTO.getPriceFee()));
            });

            //与商品售价进行比较，进行赋值
            result.forEach(spuAppPageVO -> {
                Long spuMinPrice = spuMinPriceMap.get(spuAppPageVO.getSpuId());
                if (Objects.nonNull(spuMinPrice)) {
                    spuAppPageVO.setPriceFee(spuMinPrice);
                }
            });
        }

        PageVO<SpuAppVO> pageVO = new PageVO<>();
        pageVO.setPages(page.getPages());
        pageVO.setTotal(page.getTotal());
        pageVO.setList(result);

        return pageVO;
    }

    @Override
    public void deleteBySpuIdAndUserId(Long spuId, Long userId) {
        spuCollectionMapper.deleteUserCollection(spuId, null, userId);
    }

    @Override
    public void spuBatchCollection(List<Long> spuIdList) {
        Long userId = AuthUserContext.get().getUserId();
        List<Long> spuIds = spuCollectionMapper.hasCollection(spuIdList, userId);
        spuIdList.removeAll(spuIds);
        if (CollectionUtil.isEmpty(spuIdList)) {
            return;
        }
        spuCollectionMapper.saveBatch(spuIdList, userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteBatchBySpuIdsAndUserId(List<Long> spuIds, Long userId) {
        boolean remove = false;
        for (Long spuId : spuIds) {
            if (Objects.isNull(spuService.getBySpuId(spuId))) {
                continue;
            }
            if (userCollectionCount(spuId, userId) > 0) {
                deleteBySpuIdAndUserId(spuId,userId);
                remove = true;
            }
        }
        return remove;
    }

    @Override
    public void deleteBySpuId(Long spuId) {
        spuCollectionMapper.deleteBySpuId(spuId);
    }

}
