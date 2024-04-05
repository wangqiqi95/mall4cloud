package com.mall4j.cloud.product.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.mall4j.cloud.api.auth.constant.SysTypeEnum;
import com.mall4j.cloud.common.cache.constant.CacheNames;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.i18n.I18nMessage;
import com.mall4j.cloud.common.i18n.LanguageEnum;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.product.bo.CategoryRateBO;
import com.mall4j.cloud.product.constant.SpuStatus;
import com.mall4j.cloud.api.product.dto.CategoryShopDTO;
import com.mall4j.cloud.product.mapper.CategoryShopMapper;
import com.mall4j.cloud.product.service.CategoryShopService;
import com.mall4j.cloud.product.service.SpuService;
import com.mall4j.cloud.product.vo.CategoryShopVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author  lth
 * @date 2021/4/25 14:34
 */
@Service
public class CategoryShopServiceImpl implements CategoryShopService {

    @Autowired
    private CategoryShopMapper categoryShopMapper;

    @Autowired
    private SpuService spuService;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void signingCategory(List<CategoryShopDTO> categoryShopDTOList, Long shopId) {
        if (categoryShopDTOList.size() > Constant.SIGNING_CATEGORY_LIMIT_NUM) {
            throw new LuckException("签约的分类信息不能超过" + Constant.SIGNING_CATEGORY_LIMIT_NUM);
        }
        // 处理分类扣率问题
        this.dealWithRate(categoryShopDTOList, shopId);
        // 删除已签约的平台分类信息
        categoryShopMapper.deleteByShopId(shopId);
        // 重新插入
        if (CollUtil.isNotEmpty(categoryShopDTOList)) {
            categoryShopMapper.saveBatch(categoryShopDTOList, shopId);
        }
        // 更新缓存
        this.removeCacheByShopId(shopId);
    }

    /**
     * 根据店铺id清除缓存
     * @param shopId
     */
    private void removeCacheByShopId(Long shopId) {
        if (Objects.isNull(shopId)) {
            return;
        }
        List<String> keyList = new ArrayList<>();
        keyList.add(CacheNames.SIGNING_CATEGORY_BY_SHOP_KEY + CacheNames.UNION + shopId + Constant.COLON + LanguageEnum.LANGUAGE_ZH_CN.getLang());
        keyList.add(CacheNames.SIGNING_CATEGORY_BY_SHOP_KEY + CacheNames.UNION + shopId + Constant.COLON + LanguageEnum.LANGUAGE_EN.getLang());
        keyList.add(CacheNames.CATEGORY_RATE + CacheNames.UNION + shopId);
        keyList.add(CacheNames.LIST_SIGNING_CATEGORY + CacheNames.UNION + shopId + Constant.COLON + LanguageEnum.LANGUAGE_ZH_CN.getLang());
        keyList.add(CacheNames.LIST_SIGNING_CATEGORY + CacheNames.UNION + shopId + Constant.COLON + LanguageEnum.LANGUAGE_EN.getLang());
        RedisUtil.deleteBatch(keyList);
    }

    @Override
    @Cacheable(cacheNames = CacheNames.SIGNING_CATEGORY_BY_SHOP_KEY, key = "#shopId + ':' + #lang")
    public List<CategoryShopVO> listByShopId(Long shopId, Integer lang) {
        return categoryShopMapper.listByShopId(shopId, lang);
    }

    @Override
    @Cacheable(cacheNames = CacheNames.CATEGORY_RATE, key = "#shopId")
    public List<CategoryRateBO> listRateByShopId(Long shopId) {
        return categoryShopMapper.listRateByShopId(shopId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByCategoryId(Long categoryId) {
        List<Long> shopIdList = categoryShopMapper.listShopIdByCategoryId(categoryId);
        categoryShopMapper.deleteByCategoryId(categoryId);
        // 清除缓存
        removeCacheByShopIds(shopIdList);
    }

    @Override
    public void removeCacheByChangeCategoryId(Long categoryId) {
        if (Objects.isNull(categoryId)) {
            return;
        }
        List<Long> shopIdList = categoryShopMapper.listShopIdByCategoryId(categoryId);
        this.removeCacheByShopIds(shopIdList);
    }

    @Override
    public void removeCacheByChangeCategoryIds(List<Long> categoryIds) {
        if (CollUtil.isEmpty(categoryIds)) {
            return;
        }
        List<Long> shopIdList = categoryShopMapper.listShopIdByCategoryIds(categoryIds);
        this.removeCacheByShopIds(shopIdList);
    }

    @Override
    public CategoryShopVO getByShopIdAndCategoryId(Long shopId, Long categoryId) {
        return categoryShopMapper.getByShopIdAndCategoryId(shopId, categoryId);
    }

    @Override
    public int countByShopIdAndCategoryId(Long shopId, Long categoryId) {
        return categoryShopMapper.countByShopIdAndCategoryId(shopId, categoryId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSigningCategory(Long shopId, Long categoryId) {
        CategoryShopVO categoryShopVO = categoryShopMapper.getByShopIdAndCategoryId(shopId, categoryId);
        if (Objects.isNull(categoryShopVO)) {
            throw new LuckException("找不到当前签约信息,请刷新后重试");
        }
        spuService.batchChangeSpuStatusByCidListAndShopId(Collections.singletonList(categoryId), SpuStatus.OFF_SHELF.value(), shopId);
        categoryShopMapper.deleteByShopIdAndCategoryId(shopId, categoryId);
        this.removeCacheByShopId(shopId);
    }

    /**
     * 根据店铺id列表清除缓存
     * @param shopIdList
     */
    private void removeCacheByShopIds(List<Long> shopIdList) {
        if (CollUtil.isEmpty(shopIdList)) {
            return;
        }
        List<String> keyList = new ArrayList<>();
        shopIdList.forEach(shopId -> {
            keyList.add(CacheNames.SIGNING_CATEGORY_BY_SHOP_KEY + CacheNames.UNION + shopId + Constant.COLON + LanguageEnum.LANGUAGE_ZH_CN.getLang());
            keyList.add(CacheNames.SIGNING_CATEGORY_BY_SHOP_KEY + CacheNames.UNION + shopId + Constant.COLON + LanguageEnum.LANGUAGE_EN.getLang());
            keyList.add(CacheNames.LIST_SIGNING_CATEGORY + CacheNames.UNION + shopId + Constant.COLON + LanguageEnum.LANGUAGE_ZH_CN.getLang());
            keyList.add(CacheNames.LIST_SIGNING_CATEGORY + CacheNames.UNION + shopId + Constant.COLON + LanguageEnum.LANGUAGE_EN.getLang());
        });
        if (CollUtil.isNotEmpty(keyList)) {
            RedisUtil.deleteBatch(keyList);
        }
    }

    @Override
    public void insertBatchByShopId(List<CategoryShopDTO> categoryShopDTOList, Long shopId) {
        if (CollUtil.isEmpty(categoryShopDTOList)) {
            return;
        }
        int signedCount = categoryShopMapper.countByShopIdAndCategoryId(shopId, null);
        if (signedCount + categoryShopDTOList.size() > Constant.SIGNING_CATEGORY_LIMIT_NUM) {
            throw new LuckException("签约的分类信息不能超过" + Constant.SIGNING_CATEGORY_LIMIT_NUM);
        }
        categoryShopMapper.saveBatch(categoryShopDTOList, shopId);
    }

    private void dealWithRate(List<CategoryShopDTO> categoryShopDTOList, Long shopId) {
        //查找以前签约的分类信息列表
        List<CategoryShopVO> oldCategoryShopList = categoryShopMapper.listByShopId(shopId, I18nMessage.getLang());
        // 商家端操作需要把用户非法添加的自定义扣率清除
        if (Objects.equals(AuthUserContext.get().getSysType(), SysTypeEnum.MULTISHOP.value())) {
            for (CategoryShopDTO categoryShopDTO : categoryShopDTOList) {
                // 标明该签约分类是否已经存在
                boolean isExist = false;
                int j = 0;
                // 查找是否存在
                for (; j < oldCategoryShopList.size(); j++) {
                    if (Objects.equals(oldCategoryShopList.get(j).getCategoryId(), categoryShopDTO.getCategoryId())) {
                        isExist = true;
                        categoryShopDTO.setCategoryShopId(oldCategoryShopList.get(j).getCategoryShopId());
                        categoryShopDTO.setRate(oldCategoryShopList.get(j).getCustomizeRate());
                        break;
                    }
                }
                if (!isExist) {
                    // 不存在，把扣率置为null，防止用户非法添加自定义扣率
                    categoryShopDTO.setRate(null);
                } else {
                    // 已存在，去除旧元素，减少循环次数
                    oldCategoryShopList.remove(j);
                }
            }
            this.dealWithDeleteCategoryList(oldCategoryShopList, shopId);
            return;
        }
        // 平台端处理
        if (Objects.equals(AuthUserContext.get().getSysType(), SysTypeEnum.PLATFORM.value())) {
            for (CategoryShopDTO categoryShopDTO : categoryShopDTOList) {
                // 标明该签约分类是否已经存在
                boolean isExist = false;
                int j = 0;
                // 查找是否存在
                for (; j < oldCategoryShopList.size(); j++) {
                    if (Objects.equals(oldCategoryShopList.get(j).getCategoryId(), categoryShopDTO.getCategoryId())) {
                        isExist = true;
                        categoryShopDTO.setCategoryShopId(oldCategoryShopList.get(j).getCategoryShopId());
                        break;
                    }
                }
                if (isExist) {
                    // 已存在，去除旧元素，减少循环次数
                    oldCategoryShopList.remove(j);
                }
            }
            this.dealWithDeleteCategoryList(oldCategoryShopList, shopId);
            return;
        }
        // 用户端错误操作
        throw new LuckException("用户端无法操作该数据");
    }

    /**
     * 处理删除的分类
     * @param categoryShopVOList
     * @param shopId
     */
    private void dealWithDeleteCategoryList(List<CategoryShopVO> categoryShopVOList, Long shopId) {
        spuService.batchChangeSpuStatusByCidListAndShopId(categoryShopVOList.stream().map(CategoryShopVO::getCategoryId).collect(Collectors.toList()), SpuStatus.OFF_SHELF.value(), shopId);
    }
}
