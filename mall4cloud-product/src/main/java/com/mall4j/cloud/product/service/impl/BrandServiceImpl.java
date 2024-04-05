package com.mall4j.cloud.product.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.mall4j.cloud.api.product.util.ProductLangUtil;
import com.mall4j.cloud.common.cache.constant.CacheNames;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.constant.StatusEnum;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageAdapter;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.i18n.I18nMessage;
import com.mall4j.cloud.common.i18n.LanguageEnum;
import com.mall4j.cloud.common.product.bo.EsBrandBO;
import com.mall4j.cloud.common.product.bo.EsProductBO;
import com.mall4j.cloud.common.product.constant.BrandType;
import com.mall4j.cloud.common.product.vo.BrandLangVO;
import com.mall4j.cloud.common.product.vo.BrandVO;
import com.mall4j.cloud.common.product.vo.CategoryVO;
import com.mall4j.cloud.common.product.vo.app.BrandAppVO;
import com.mall4j.cloud.product.dto.BrandDTO;
import com.mall4j.cloud.product.mapper.BrandMapper;
import com.mall4j.cloud.product.model.Brand;
import com.mall4j.cloud.product.model.BrandLang;
import com.mall4j.cloud.product.service.*;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 品牌信息
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private BrandLangService brandLangService;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private CategoryBrandService categoryBrandService;
    @Autowired
    private BrandShopService brandShopService;
    @Autowired
    private SpuService spuService;
    @Autowired
    private SpuOfflineService spuOfflineService;

    //默认主库
    @Override
    public void dbMasterTest() {
        BrandVO brandVO = brandMapper.getByBrandId(9l);
        System.out.println(brandVO.getImgUrl());
    }

    @Override
    @DS("slave")
    public void dbSlaveTest() {
        BrandVO brandVO = brandMapper.getByBrandId(9l);
        System.out.println(brandVO.getImgUrl());
    }

    @Override
    public PageVO<BrandVO> page(PageDTO pageDTO, BrandDTO brandDTO) {
        PageAdapter pageAdapter = new PageAdapter(pageDTO);
        List<BrandVO> list = brandMapper.list(pageAdapter, brandDTO);
        ProductLangUtil.brandList(list);
        PageVO pageVO = new PageVO();
        pageVO.setList(list);
        pageVO.setTotal(brandMapper.listTotal(brandDTO));
        pageVO.setPages(PageUtil.getPages(pageVO.getTotal(), pageDTO.getPageSize()));
        return pageVO;
    }

    @Override
    public BrandVO getByBrandId(Long brandId) {
        if (Objects.isNull(brandId) || Objects.equals(brandId, Constant.ZERO_LONG)) {
            return null;
        }
        BrandVO brand = brandMapper.getByBrandId(brandId);
        ProductLangUtil.brand(brand);
        return brand;
    }
    @Override
    public BrandVO getInfo(Long brandId) {
        BrandVO brand = brandMapper.getByBrandId(brandId);
        if (Objects.isNull(brand)) {
            return new BrandVO();
        }
        List<CategoryVO> categoryVO = categoryBrandService.getCategoryByBrandId(brandId);
        if (CollUtil.isNotEmpty(categoryVO)){
            brand.setCategories(categoryVO);
        }
        return brand;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(BrandDTO brandDTO) {
        Brand brand = mapperFacade.map(brandDTO, Brand.class);
        brand.setFirstLetter(brand.getFirstLetter().toUpperCase());
        brand.setStatus(StatusEnum.ENABLE.value());
        brandMapper.save(brand);
        brandLangService.save(mapperFacade.mapAsList(brandDTO.getBrandLangList(), BrandLang.class), brand.getBrandId());
        if (CollUtil.isNotEmpty(brandDTO.getCategoryIds())){
            categoryBrandService.saveByCategoryIds(brand.getBrandId(), brandDTO.getCategoryIds());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(BrandDTO brandDTO) {
        Brand brand = mapperFacade.map(brandDTO, Brand.class);
        brand.setSpuCount(brandMapper.getUseNum(brandDTO.getBrandId(), StatusEnum.ENABLE.value()));
        brandMapper.update(brand);
        brandLangService.update(mapperFacade.mapAsList(brandDTO.getBrandLangList(), BrandLang.class), brand.getBrandId());
        categoryBrandService.updateByCategoryIds(brand.getBrandId(), brandDTO.getCategoryIds());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long brandId) {
        if (getUseNum(brandId) > 0){
            throw new LuckException("有部分商品在使用该品牌，不能进行删除操作");
        }
        brandMapper.deleteById(brandId);
        brandLangService.deleteById(brandId);
        categoryBrandService.deleteByBrandId(brandId);
        brandShopService.deleteByBrandId(brandId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBrandStatus(BrandDTO brandDTO) {
        BrandVO dbBrand = getByBrandId(brandDTO.getBrandId());
        if (Objects.isNull(dbBrand) || dbBrand.getStatus().equals(brandDTO.getStatus())) {
            return;
        }
        brandMapper.updateBrandStatus(brandDTO);
        // 如果品牌下架，对应的商品下架
        if (!Objects.equals(StatusEnum.DISABLE.value(),brandDTO.getStatus())) {
            return;
        }
        // 获取品牌相关的商品
        List<Long> spuIds = spuService.getSpuIdsBySpuUpdateDTO(null, null, brandDTO.getBrandId(), null);
        if (CollUtil.isEmpty(spuIds)) {
            return;
        }
        // 查询出秒杀or团购的商品进行下线
        spuOfflineService.offlineSpuStatusAndActivityByBrandId(spuIds,brandDTO.getBrandId());
        // 清除商品缓存
        spuService.batchRemoveSpuActivityCache(spuIds);
    }

    @Override
    @Cacheable(cacheNames = CacheNames.BRAND_LIST_BY_CATEGORY, key = "#categoryId + ':' + #lang")
    public List<BrandAppVO> listByCategory(Long categoryId, Integer lang) {
        List<BrandAppVO> brandList = brandMapper.listByCategory(categoryId);
        ProductLangUtil.brandAppList(brandList);
        return brandList;
    }

    @Override
    @Cacheable(cacheNames = CacheNames.BRAND_TOP, key = "#lang")
    public List<BrandAppVO> topBrandList(Integer lang) {
        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setIsTop(1);
        List<BrandAppVO> brandList = brandMapper.appList(brandDTO, Constant.BRAND_TOP_NUM);
        ProductLangUtil.brandAppList(brandList);
        return brandList;
    }

    @Override
    public void removeCache(List<Long> categoryIds) {
        List<String> keys = new ArrayList<>();
        if (CollUtil.isNotEmpty(categoryIds)) {
            Set<Long> categoryIdSet = new HashSet<>(categoryIds);
            for (Long categoryId : categoryIdSet) {
                keys.add(CacheNames.BRAND_LIST_BY_CATEGORY + CacheNames.UNION + categoryId + Constant.COLON + LanguageEnum.LANGUAGE_ZH_CN.getLang());
                keys.add(CacheNames.BRAND_LIST_BY_CATEGORY + CacheNames.UNION + categoryId + Constant.COLON + LanguageEnum.LANGUAGE_EN.getLang());
            }
        }
        keys.add(CacheNames.BRAND_TOP + CacheNames.UNION + LanguageEnum.LANGUAGE_ZH_CN.getLang());
        keys.add(CacheNames.BRAND_TOP + CacheNames.UNION + LanguageEnum.LANGUAGE_EN.getLang());
        RedisUtil.deleteBatch(keys);
    }

    @Override
    public void updateSpuCount(Long brandId) {
        if (Objects.isNull(brandId) || Objects.equals(brandId, Constant.ZERO_LONG)) {
           return;
        }
        brandMapper.batchUpdateSpuCount(Collections.singleton(brandId));
    }


    @Override
    public void updateSpuCountByBrandIds(Collection<Long> brandIds) {
        if (CollUtil.isEmpty(brandIds)) {
           return;
        }
        brandMapper.batchUpdateSpuCount(brandIds);
    }

    @Override
    public List<BrandLang> listBrandLangByBrandNames(Set<String> brandNames, Long shopId) {
        if (CollUtil.isEmpty(brandNames)) {
            return new ArrayList<>();
        }
        return brandLangService.listByBrandNames(brandNames, shopId);
    }

    @Override
    public PageVO<BrandAppVO> appPage(PageDTO pageDTO, BrandDTO brandDTO) {
        PageAdapter pageAdapter = new PageAdapter(pageDTO);
        List<BrandAppVO> brandAppVOList = brandMapper.brandAppPage(pageAdapter, brandDTO);
        ProductLangUtil.brandAppList(brandAppVOList);
        PageVO pageVO = new PageVO();
        pageVO.setList(brandAppVOList);
        pageVO.setTotal(brandMapper.brandAppTotal(brandDTO));
        pageVO.setPages(PageUtil.getPages(pageVO.getTotal(), pageDTO.getPageSize()));
        return pageVO;
    }

    @Override
    public List<BrandVO> listByName(String name) {
        List<BrandVO> brandList = brandMapper.listByName(name, I18nMessage.getLang());
        return brandList;
    }

    private int getUseNum(Long brandId) {
        return brandMapper.getUseNum(brandId, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCustomBrandToPlatformBrandByShopId(Long shopId) {
        brandMapper.updateShopIdAndStatusByShopId(shopId, StatusEnum.ENABLE.value(), Constant.PLATFORM_SHOP_ID);
        brandShopService.updateTypeByShopId(shopId, BrandType.PLATFORM.value());
    }

    @Override
    public List<BrandVO> listAvailableBrandByCategoryIdAndBrandNameAndShopId(Long categoryId, String brandName, Long shopId) {
        List<BrandVO> brandByShopIdAndBrandName = brandMapper.listSigningByShopIdAndBrandNameAndCategoryId(shopId, brandName, categoryId);
        ProductLangUtil.brandList(brandByShopIdAndBrandName);
        return brandByShopIdAndBrandName;
    }

    @Override
    public EsBrandBO getEsBrandBO(EsProductBO esProductBO) {
        Long brandId = esProductBO.getBrandId();
        if (Objects.isNull(brandId)) {
            return null;
        }
        BrandVO brand = brandMapper.getByBrandId(brandId);
        if (Objects.isNull(brand)) {
            return null;
        }
        if (esProductBO.getAppDisplay() && !Objects.equals(brand.getStatus(), StatusEnum.ENABLE.value())) {
            esProductBO.setAppDisplay(Boolean.FALSE);
        }
        EsBrandBO esBrandBO = new EsBrandBO();
        esBrandBO.setBrandId(brand.getBrandId());
        esBrandBO.setBrandImg(brand.getImgUrl());
        Map<Integer, String> brandMap = brand.getBrandLangList().stream()
                .filter(brandLangVO -> StrUtil.isNotBlank(brandLangVO.getName()))
                .collect(Collectors.toMap(BrandLangVO::getLang, BrandLangVO::getName));
        esBrandBO.setBrandNameZh(brandMap.get(LanguageEnum.LANGUAGE_ZH_CN.getLang()));
        esBrandBO.setBrandNameEn(brandMap.get(LanguageEnum.LANGUAGE_EN.getLang()));
        return esBrandBO;
    }

    @Override
    public List<Long> listBrandIdBySpuIds(List<Long> spuIds) {
        if (CollUtil.isEmpty(spuIds)) {
            return new ArrayList<>();
        }
        return brandMapper.listBrandIdBySpuIds(spuIds);
    }

    @Override
    public List<BrandVO> listByParams(BrandDTO brandDTO) {
        List<BrandVO> brandList = brandMapper.listByParams(brandDTO);
        ProductLangUtil.brandList(brandList);
        return brandList;
    }

    @Override
    public void updateSpuCountBySpuIds(List<Long> spuIds) {
        if (CollUtil.isEmpty(spuIds)) {
            return;
        }
        List<Long> brandIds = brandMapper.listBrandIdBySpuIds(spuIds);
        updateSpuCountByBrandIds(brandIds);
    }

}
