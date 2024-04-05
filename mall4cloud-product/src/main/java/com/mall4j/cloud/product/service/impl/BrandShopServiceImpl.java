package com.mall4j.cloud.product.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.constant.StatusEnum;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.i18n.I18nMessage;
import com.mall4j.cloud.common.i18n.LanguageEnum;
import com.mall4j.cloud.common.product.constant.BrandType;
import com.mall4j.cloud.common.product.vo.BrandVO;
import com.mall4j.cloud.api.product.dto.BrandShopDTO;
import com.mall4j.cloud.product.dto.BrandSigningDTO;
import com.mall4j.cloud.product.mapper.BrandLangMapper;
import com.mall4j.cloud.product.mapper.BrandMapper;
import com.mall4j.cloud.product.mapper.BrandShopMapper;
import com.mall4j.cloud.product.model.BrandShop;
import com.mall4j.cloud.product.service.BrandShopService;
import com.mall4j.cloud.product.vo.BrandShopVO;
import com.mall4j.cloud.product.vo.BrandSigningVO;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 品牌店铺关联信息
 *
 * @author FrozenWatermelon
 * @date 2021-04-30 13:21:10
 */
@Service
public class BrandShopServiceImpl implements BrandShopService {

    @Autowired
    private BrandShopMapper brandShopMapper;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private BrandLangMapper brandLangMapper;

    @Override
    public PageVO<BrandShop> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> brandShopMapper.list());
    }

    @Override
    public BrandShop getByBrandShopId(Long brandShopId) {
        return brandShopMapper.getByBrandShopId(brandShopId);
    }

    @Override
    public void save(BrandShop brandShop) {
        brandShopMapper.save(brandShop);
    }

    @Override
    public void update(BrandShop brandShop) {
        brandShopMapper.update(brandShop);
    }

    @Override
    public void deleteById(Long brandShopId) {
        brandShopMapper.deleteById(brandShopId);
    }

    @Override
    public BrandSigningVO listSigningByShopId(Long shopId) {
        // 查询平台品牌签约列表
        List<BrandShopVO> platformBrandList = brandShopMapper.listByShopIdAndType(shopId, BrandType.PLATFORM.value(), I18nMessage.getLang());
        // 查询店铺自定义品牌签约列表
        List<BrandShopVO> customizeBrandList = brandShopMapper.listByShopIdAndType(shopId, BrandType.CUSTOMIZE.value(), I18nMessage.getLang());
        BrandSigningVO brandSigningVO = new BrandSigningVO();
        brandSigningVO.setCustomizeBrandList(customizeBrandList);
        brandSigningVO.setPlatformBrandList(platformBrandList);
        return brandSigningVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void signingBrands(BrandSigningDTO brandSigningDTO, Long shopId) {
        if (Objects.equals(Constant.PLATFORM_SHOP_ID, shopId)) {
            // 防止误删平台品牌
            return;
        }
        List<BrandShopDTO> platformBrandList = brandSigningDTO.getPlatformBrandList();
        List<BrandShopDTO> customizeBrandList = brandSigningDTO.getCustomizeBrandList();
        if (platformBrandList.size() + customizeBrandList.size() > Constant.SIGNING_BRAND_LIMIT_NUM) {
            throw new LuckException("签约的品牌数量不能超过" + Constant.SIGNING_BRAND_LIMIT_NUM);
        }
        // 删除已签约的信息
        brandShopMapper.deleteBatchByShopId(shopId);
        // 处理签约的平台品牌
        if (Objects.nonNull(brandSigningDTO.getPlatformBrandList()) && brandSigningDTO.getPlatformBrandList().size() != 0) {
            List<BrandVO> brandVOList = brandMapper.listByIds(platformBrandList.stream().map(BrandShopDTO::getBrandId).collect(Collectors.toList()));
            if (brandVOList.size() != platformBrandList.size()) {
                throw new LuckException("平台品牌签约信息错误");
            }
            // 插入品牌签约关联信息
            brandShopMapper.insertBatch(shopId, platformBrandList, BrandType.PLATFORM.value());
        }
        // 处理新增的自定义品牌
        // 查询之前新增的自定义品牌列表
        List<BrandVO> oldBrandVOList = brandMapper.listByShopId(shopId);
        if (oldBrandVOList.size() != 0) {
            // 删除店铺自定义的品牌信息
            brandMapper.deleteBatchByShopId(shopId);
            brandLangMapper.deleteBatchByBrandIds(oldBrandVOList.stream().map(BrandVO::getBrandId).collect(Collectors.toList()));
        }
        if (Objects.nonNull(brandSigningDTO.getCustomizeBrandList()) && brandSigningDTO.getCustomizeBrandList().size() != 0) {
            // 重新新增店铺自定义的品牌信息列表
            customizeBrandList.forEach(item -> {
                item.setShopId(shopId);
                item.setBrandId(null);
                item.setIsTop(0);
                item.setSeq(0);
                item.setStatus(StatusEnum.DISABLE.value());
            });
            brandMapper.insertBatchByBrandShopList(customizeBrandList);
            brandLangMapper.insertBatch(customizeBrandList, LanguageEnum.LANGUAGE_ZH_CN.getLang());
            brandLangMapper.insertBatch(customizeBrandList, LanguageEnum.LANGUAGE_EN.getLang());
            brandShopMapper.insertBatch(shopId, customizeBrandList, BrandType.CUSTOMIZE.value());
        }
    }

    @Override
    public void updateTypeByShopId(Long shopId, Integer type) {
        brandShopMapper.updateTypeByShopId(shopId, type);
    }

    @Override
    public void deleteByBrandId(Long brandId) {
        brandShopMapper.deleteByBrandId(brandId);
    }

    @Override
    public void insertBatchByShopId(List<BrandShopDTO> brandShopDTOList, Long shopId) {
         if (CollUtil.isEmpty(brandShopDTOList)) {
             return;
         }
         int signedCount = brandShopMapper.countByShopIdAndBrandId(shopId, null);
         if (signedCount + brandShopDTOList.size() > Constant.SIGNING_BRAND_LIMIT_NUM) {
             throw new LuckException("签约的品牌数量不能超过" + Constant.SIGNING_BRAND_LIMIT_NUM);
         }
         brandShopMapper.insertBatch(shopId, brandShopDTOList, BrandType.PLATFORM.value());
    }

    @Override
    public int countByShopIdAndBrandId(Long shopId, Long brandId) {
        return brandShopMapper.countByShopIdAndBrandId(shopId, brandId);
    }

}
