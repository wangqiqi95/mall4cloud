package com.mall4j.cloud.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.api.docking.dto.zhls.product.CategoriesReqDto;
import com.mall4j.cloud.api.docking.dto.zhls.product.CategoryDto;
import com.mall4j.cloud.api.docking.dto.zhls.product.SaveSkuDto;
import com.mall4j.cloud.api.docking.dto.zhls.product.Sku;
import com.mall4j.cloud.api.docking.dto.zhls.product.Sku.*;
import com.mall4j.cloud.api.docking.dto.zhls.product.Sku.MediaInfo.Img;
import com.mall4j.cloud.api.docking.dto.zhls.recommend.BaseRecommendDto;
import com.mall4j.cloud.api.docking.dto.zhls.recommend.RecommendGetReqDto;
import com.mall4j.cloud.api.docking.dto.zhls.recommend.RecommendGetRespDto;
import com.mall4j.cloud.api.docking.feign.zhls.RecommendFeignClient;
import com.mall4j.cloud.api.platform.feign.ConfigFeignClient;
import com.mall4j.cloud.api.product.dto.ErpSkuStockDTO;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.common.cache.constant.ProductCacheNames;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.product.dto.ProductSearchDTO;
import com.mall4j.cloud.common.product.vo.search.SpuCommonVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.product.config.ProductConfigProperties;
import com.mall4j.cloud.product.constant.SpuStatus;
import com.mall4j.cloud.product.dto.ZhlsCommodityReqDto;
import com.mall4j.cloud.product.mapper.*;
import com.mall4j.cloud.product.model.Category;
import com.mall4j.cloud.product.model.SkuStore;
import com.mall4j.cloud.product.model.Spu;
import com.mall4j.cloud.product.service.CategoryService;
import com.mall4j.cloud.product.service.SpuService;
import com.mall4j.cloud.product.service.ZhlsCommodityService;
import com.mall4j.cloud.product.vo.ZhlsCommodityVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Description 有数商品信息上报
 * @Author axin
 * @Date 2023-05-05 14:31
 **/
@Slf4j
@Service
@RefreshScope
public class ZhlsCommodityServiceImpl implements ZhlsCommodityService {
    @Autowired
    private RecommendFeignClient recommendFeignClient;
    @Autowired
    private SpuService spuService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ConfigFeignClient configFeignClient;
    @Autowired
    private UserFeignClient userFeignClient;
    @Autowired
    private SkuStoreMapper skuStoreMapper;
    @Autowired
    private SkuStockMapper skuStockMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private SpuMapper spuMapper;


    @Value("${biz.oss.resources-url}")
    private String imgDomain;

    @Autowired
    private ProductConfigProperties productConfigProperties;


    @Override
    public void productCategoriesAdd(List<Category> categories,Integer isDeleted) {
        if(CollectionUtils.isNotEmpty(categories)){
            CategoriesReqDto categoriesReqDto = new CategoriesReqDto();
            List<CategoryDto> list = Lists.newArrayList();

            for (Category category : categories) {
                CategoryDto categoryDto = new CategoryDto();
                categoryDto.setExternal_category_id(String.valueOf(category.getCategoryId()));
                categoryDto.setCategory_name(category.getName());
                //shopid 1是前台分类 0是后台分类
                categoryDto.setCategory_type(category.getShopId()==1?category.getShopId().intValue():2);
                categoryDto.setCategory_level(category.getLevel());
                categoryDto.setExternal_parent_category_id(String.valueOf(category.getParentId()));
                categoryDto.setIs_root(category.getLevel() == 0);
                categoryDto.setIs_deleted(isDeleted);
                list.add(categoryDto);
            }

            if(list.size()>50){
                List<List<CategoryDto>> partition = Lists.partition(list, 50);
                for (List<CategoryDto> categoryDtos : partition) {
                    categoriesReqDto.setCategories(categoryDtos);
                    recommendFeignClient.productCategoriesAdd(categoriesReqDto);
                }
            }else{
                categoriesReqDto.setCategories(list);
                recommendFeignClient.productCategoriesAdd(categoriesReqDto);
            }
        }
    }

    @Override
    public void skuInfoAdd(List<Long> skuIds) {
        List<Sku> list = Lists.newArrayList();
        setSku(skuIds, list);

        invokeAddSkuInfo(list);
    }

    @Override
    public void skuInfoUpdateStatus(List<Long> skuIds,Integer status) {
        List<Sku> list = Lists.newArrayList();
        if(SpuStatus.PUT_SHELF.value().equals(status)){
            setSku(skuIds, list);

            invokeAddSkuInfo(list);
        }else{
            List<com.mall4j.cloud.product.model.Sku> skus = skuMapper.selectBatchIds(skuIds);
            for (com.mall4j.cloud.product.model.Sku skuEo : skus) {
                Sku sku = new Sku();
                sku.setExternal_sku_id(skuEo.getSkuCode());
                SalesInfo salesInfo = new SalesInfo();
                salesInfo.setIs_available(false);
                sku.setSales_info(salesInfo);
                list.add(sku);
            }
            invokeUpdateSkuInfo(list);
        }

    }

    @Override
    public void skuInfoUpdateStock(List<ErpSkuStockDTO> reqDtos) {
        if (CollectionUtils.isNotEmpty(reqDtos)) {
            Map<String,String> spuCodeMap = Maps.newHashMap();
            for (ErpSkuStockDTO reqDto : reqDtos) {
                if(StringUtils.isNotBlank(reqDto.getSkuCode())){
                    String spuCode = reqDto.getSkuCode().split("-")[0];
                    spuCodeMap.put(reqDto.getSkuCode(),spuCode);
                }
            }
            List<Spu> spus = spuMapper.selectList(Wrappers.lambdaQuery(Spu.class).in(Spu::getSpuCode, spuCodeMap.values()));
            Map<String, Integer> spuStatusMap = spus.stream().collect(Collectors.toMap(Spu::getSpuCode, Spu::getStatus, (v1, v2) -> v2));

            List<Sku> list = Lists.newArrayList();
            for (ErpSkuStockDTO dto : reqDtos) {
                String spuCode = spuCodeMap.get(dto.getSkuCode());
                Integer status = spuStatusMap.get(spuCode);
                Sku sku = new Sku();
                sku.setExternal_sku_id(dto.getSkuCode());
                sku.setExternal_spu_id(spuCode);
                SalesInfo salesInfo = new SalesInfo();
                salesInfo.setSku_stock_status(dto.getAvailableStockNum() > 0 ? 1 : 2);
                salesInfo.setIs_available(1==status);
                sku.setSales_info(salesInfo);
                list.add(sku);
            }
            invokeUpdateSkuInfo(list);
        }
    }

    @Override
    public void skuInfoUpdatePrice(List<Long> skuIds) {
        List<Sku> list = Lists.newArrayList();
        List<com.mall4j.cloud.product.model.Sku> skus = skuMapper.selectBatchIds(skuIds);

        for (com.mall4j.cloud.product.model.Sku skuEo : skus) {
            Sku sku = new Sku();
            sku.setExternal_sku_id(skuEo.getSkuCode());
            Price price = new Price();
            price.setCurrent_price(skuEo.getPriceFee()/100f);
            price.setSku_price(skuEo.getMarketPriceFee()/100f);

            sku.setPrice(price);
            list.add(sku);
        }
        invokeUpdateSkuInfo(list);
    }


    @Override
    public ZhlsCommodityVO zhlsRecommendList(Long storeId, ZhlsCommodityReqDto reqDto) {
        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();

        List<String> spuCodes = Lists.newArrayList();
        long total = 0;
        BaseRecommendDto<RecommendGetRespDto> baseRecommendDto = new BaseRecommendDto<>();
        Map<String, RecommendGetRespDto.item> itemMap = Maps.newHashMap();
        ProductSearchDTO productSearch = new ProductSearchDTO();

        if (Objects.nonNull(userInfoInTokenBO) && userFeignClient.isRecEnabled(userInfoInTokenBO.getUserId())) {
            RecommendGetReqDto recommendGetReqDto = new RecommendGetReqDto();
            recommendGetReqDto.setUid_type(2);
            recommendGetReqDto.setUser_id(userInfoInTokenBO.getBizUserId());

            ServerResponseEntity<String> serverResponse = configFeignClient.getConfig("REC_STRATEGY");
            if (serverResponse.isFail()) {
                throw new LuckException("未获取到推荐策略");
            }
            JSONObject jsonObject = JSON.parseObject(serverResponse.getData());
            if (StringUtils.isNotBlank(reqDto.getItemId())) {
                String channelid = (String) jsonObject.get("商品相关性");
                recommendGetReqDto.setChannel_id(channelid);
            } else {
                String channelid = (String) jsonObject.get("猜你喜欢");
                recommendGetReqDto.setChannel_id(channelid);
            }

            recommendGetReqDto.setSequence_id(reqDto.getSequenceId());
            recommendGetReqDto.setStore_ids(Lists.newArrayList("0"));
            recommendGetReqDto.setItem_id(reqDto.getItemId());
            recommendGetReqDto.setExclude_item_ids(reqDto.getExcludeItemIds());
            recommendGetReqDto.setPage_no(reqDto.getPageNum());
            recommendGetReqDto.setPage_size(reqDto.getPageSize());
            recommendGetReqDto.setReq_hot_recommend(reqDto.getReqHotRecommend());
            baseRecommendDto = recommendFeignClient.recommendGet(recommendGetReqDto);
            if (baseRecommendDto.isSuccess()) {
                RecommendGetRespDto data = baseRecommendDto.getData();
                List<RecommendGetRespDto.item> item_list = data.getItem_list();
                spuCodes = item_list.stream().map(RecommendGetRespDto.item::getItem_id).collect(Collectors.toList());

                total = baseRecommendDto.getHits();
                itemMap = data.getItem_list().stream().collect(Collectors.toMap(RecommendGetRespDto.item::getItem_id, Function.identity(),(v1,v2)->v1));
            }
            productSearch.setPageNum(1);
        }else{
            productSearch.setPageNum(reqDto.getPageNum());
        }

        productSearch.setStoreId(storeId);
        productSearch.setPageSize(reqDto.getPageSize());
        //只查询上架状态的数据。
        productSearch.setStatus(1);
        productSearch.setSpuCodeList(spuCodes);
        if(Objects.isNull(userInfoInTokenBO)){
            productSearch.setSort(2);
        }
        PageVO<SpuCommonVO> page = spuService.commonPage(productSearch);
        if(total > 0) {
            page.setTotal(total);
            page.setPages(PageUtil.getPages(total,reqDto.getPageSize()));
        }

        if(page.getTotal() < 1 ){
            productSearch.setSpuCodeList(null);
            page = spuService.commonPage(productSearch);
        }

        ZhlsCommodityVO zhlsCommodityVO = new ZhlsCommodityVO();
        zhlsCommodityVO.setPages(page.getPages());
        zhlsCommodityVO.setTotal(page.getTotal());
        if(baseRecommendDto.isSuccess()) {
            zhlsCommodityVO.setSequenceId(baseRecommendDto.getSequence_id());
            zhlsCommodityVO.setCommon_outer_service(baseRecommendDto.getData().getCommon_outer_service());
        }

        List<ZhlsCommodityVO.ZhlsCommodity> list = Lists.newArrayList();
        for (SpuCommonVO spuCommonVO : page.getList()) {
            ZhlsCommodityVO.ZhlsCommodity zhlsCommodity = new ZhlsCommodityVO.ZhlsCommodity();
            BeanUtils.copyProperties(spuCommonVO,zhlsCommodity);
            RecommendGetRespDto.item item = itemMap.get(spuCommonVO.getSpuCode());
            zhlsCommodity.setOuter_service(Objects.isNull(item)?null:item.getOuter_service());
            list.add(zhlsCommodity);
        }
        zhlsCommodityVO.setList(list);

        return zhlsCommodityVO;
    }

    private void invokeUpdateSkuInfo(List<Sku> list) {
        if(list.size()>50){
            List<List<Sku>> partition = Lists.partition(list, 50);
            for (List<Sku> skuList : partition) {
                SaveSkuDto saveSkuDto = new SaveSkuDto();
                saveSkuDto.setSkus(skuList);
                recommendFeignClient.updateSkuInfo(saveSkuDto);
            }
        }else{
            SaveSkuDto saveSkuDto = new SaveSkuDto();
            saveSkuDto.setSkus(list);
            recommendFeignClient.updateSkuInfo(saveSkuDto);
        }
    }

    private void setSku(List<Long> skuIds, List<Sku> list) {
        List<com.mall4j.cloud.product.model.Sku> skus = skuMapper.selectBatchIds(skuIds);
        if(CollectionUtils.isNotEmpty(skus)){
            List<Long> spuIds = skus.stream().map(com.mall4j.cloud.product.model.Sku::getSpuId).collect(Collectors.toList());
            List<SkuStore> skuStores = skuStoreMapper.selectList(Wrappers.lambdaQuery(SkuStore.class).in(SkuStore::getSkuId, skuIds).eq(SkuStore::getStoreId, 1));
            Map<Long, SkuStore> skuStoreMap = skuStores.stream().collect(Collectors.toMap(SkuStore::getSkuId, Function.identity(),(v1,v2)->v1));
            List<Spu> spus = spuMapper.selectBatchIds(spuIds);
            Map<Long, Spu> spuMap = spus.stream().collect(Collectors.toMap(Spu::getSpuId, Function.identity(),(v1,v2)->v1));

            List<Category> categories = RedisUtil.get(ProductCacheNames.CATEGORY_ALL);
            if(CollectionUtils.isEmpty(categories)) {
                categories = categoryMapper.selectList(Wrappers.emptyWrapper());
                RedisUtil.set(ProductCacheNames.CATEGORY_ALL,categories,60*60*24);
            }
            Map<Long, Category> categoryMap = categories.stream().collect(Collectors.toMap(Category::getCategoryId, Function.identity(),(v1,v2)->v1));

            skus.forEach(sku -> {
                try {
                    Spu spu = spuMap.get(sku.getSpuId());
                    Sku reSku = new Sku();
                    reSku.setExternal_sku_id(sku.getSkuCode());
                    reSku.setExternal_spu_id(spu.getSpuCode());

                    MediaInfo mediaInfo = new MediaInfo();
                    Img img = new Img();
                    img.setImg_url(convertImgUrl(spu.getMainImgUrl()));
                    mediaInfo.setPrimary_imgs(img);
                    List<Img> imgs = Lists.newArrayList();

                    String[] split = StringUtils.isNotBlank(spu.getImgUrls())?spu.getImgUrls().split(","):null;
                    if(split!= null && split.length>0){
                        for (String s : split) {
                            Img img1 = new Img();
                            img1.setImg_url(convertImgUrl(s));
                            imgs.add(img1);
                        }
                    }
                    mediaInfo.setImgs(imgs);
                    reSku.setMedia_info(mediaInfo);

                    List<CategoryInfo> categoryInfos = Lists.newArrayList();

                    //后台类目
                    CategoryInfo categoryInfo = new CategoryInfo();
                    Category category3 = categoryMap.get(spu.getCategoryId());
                    if(Objects.nonNull(category3)) {
                        Category category2 = categoryMap.get(category3.getParentId());
                        Category category1 = categoryMap.get(Objects.nonNull(category2) ? category2.getParentId() : null);
                        categoryInfo.setCategory_type(2);
                        setCategory(categoryInfo, category3, category2, category1);
                        categoryInfos.add(categoryInfo);
                    }
                    //前台类目
                    CategoryInfo shopCategoryInfo = new CategoryInfo();
                    Category shopCategory3 = categoryMap.get(spu.getShopCategoryId());
                    if(Objects.nonNull(shopCategory3)) {
                        Category shopCategory2 = categoryMap.get(shopCategory3.getParentId());
                        Category shopCategory1 = categoryMap.get(Objects.nonNull(shopCategory2) ? shopCategory2.getParentId() : null);
                        shopCategoryInfo.setCategory_type(1);
                        setCategory(shopCategoryInfo, shopCategory3, shopCategory2, shopCategory1);
                        categoryInfos.add(shopCategoryInfo);
                    }

                    reSku.setCategory_info(categoryInfos);

                    SkuStore skuStore = skuStoreMap.get(sku.getSkuId());
                    SalesInfo salesInfo = new SalesInfo();
                    salesInfo.setIs_available(spu.getStatus()==1);
                    if(Objects.nonNull(skuStore)) {
                        salesInfo.setSku_stock_status(skuStore.getStock() > 0 ? 1 : 2);
                    }else{
                        salesInfo.setSku_stock_status(0);
                    }
                    salesInfo.setProduct_type(1);
                    reSku.setSales_info(salesInfo);

                    DescInfo descInfo = new DescInfo();
                    descInfo.setProduct_name_chinese(spu.getName());
                    descInfo.setProduct_desc(spu.getSellingPoint());
                    reSku.setDesc_info(descInfo);

                    Price price = new Price();
                    price.setCurrent_price(sku.getPriceFee()/100f);
                    price.setSku_price(sku.getMarketPriceFee()/100f);
                    reSku.setPrice(price);

                    list.add(reSku);
                } catch (Exception e) {
                    log.error("同步推荐商品数据异常:{}",e);
                }
            });
        }
    }

    /**
     * 设置分类
     * @param shopCategoryInfo
     * @param shopCategory3
     * @param shopCategory2
     * @param shopCategory1
     */
    private void setCategory(CategoryInfo shopCategoryInfo, Category shopCategory3, Category shopCategory2, Category shopCategory1) {
        shopCategoryInfo.setCategory_level_1_id(Objects.nonNull(shopCategory1) ? shopCategory1.getCategoryId() + "" : "");
        shopCategoryInfo.setCategory_level_1_name(Optional.ofNullable(shopCategory1).orElse(new Category()).getName());
        shopCategoryInfo.setCategory_level_2_id(Objects.nonNull(shopCategory2) ? shopCategory2.getCategoryId() + "" : "");
        shopCategoryInfo.setCategory_level_2_name(Optional.ofNullable(shopCategory2).orElse(new Category()).getName());
        shopCategoryInfo.setCategory_level_3_id(Objects.nonNull(shopCategory3) ? shopCategory3.getCategoryId() + "" : "");
        shopCategoryInfo.setCategory_level_3_name(Optional.ofNullable(shopCategory3).orElse(new Category()).getName());
    }

    private void invokeAddSkuInfo(List<Sku> list) {
        if(list.size()>50){
            List<List<Sku>> partition = Lists.partition(list, 50);
            for (List<Sku> skuList : partition) {
                SaveSkuDto saveSkuDto = new SaveSkuDto();
                saveSkuDto.setSkus(skuList);
                recommendFeignClient.addSkuInfo(saveSkuDto);
            }
        }else{
            SaveSkuDto saveSkuDto = new SaveSkuDto();
            saveSkuDto.setSkus(list);
            recommendFeignClient.addSkuInfo(saveSkuDto);
        }
    }

    private String convertImgUrl(String url){
        if (StringUtils.isBlank(url)) {
            return null;
        }
        /**
         * 有多重图片格式 `/` 表示需要单独拼接oss地址
         * `https` 表示不需要拼接
         */
        if (url.startsWith("//") ){
            url = "https:" + url;
        } else if (url.startsWith("/")) {
            url = imgDomain + url;
        }
        return url;
    }
}
