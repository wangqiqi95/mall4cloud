package com.mall4j.cloud.product.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.aliyun.openservices.ons.api.SendResult;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.api.product.bo.PlatformCommissionOrderItemBO;
import com.mall4j.cloud.api.product.constant.CategoryLevel;
import com.mall4j.cloud.api.product.dto.IphSyncCategoryDto;
import com.mall4j.cloud.api.product.util.ProductLangUtil;
import com.mall4j.cloud.common.cache.constant.CacheNames;
import com.mall4j.cloud.common.cache.constant.ProductCacheNames;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.constant.StatusEnum;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.i18n.LanguageEnum;
import com.mall4j.cloud.common.product.vo.CategoryLangVO;
import com.mall4j.cloud.common.product.vo.CategoryVO;
import com.mall4j.cloud.common.product.vo.app.CategoryAppVO;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.product.bo.CategoryRateBO;
import com.mall4j.cloud.product.config.ProductConfigProperties;
import com.mall4j.cloud.product.dto.CategoryDTO;
import com.mall4j.cloud.product.dto.CategoryLangDTO;
import com.mall4j.cloud.product.mapper.CategoryMapper;
import com.mall4j.cloud.product.mapper.SpuMapper;
import com.mall4j.cloud.product.model.Category;
import com.mall4j.cloud.product.service.*;
import com.mall4j.cloud.product.vo.CategoryUseNumVO;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 分类信息
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
@Service
@Slf4j
@RefreshScope
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CategoryLangService categoryLangService;
    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SpuTagReferenceService spuTagReferenceService;
    @Autowired
    private SpuOfflineService spuOfflineService;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private CategoryShopService categoryShopService;

    @Autowired
    private OnsMQTemplate syncZhlsProductTemplate;

    @Autowired
    private ProductConfigProperties productConfigProperties;

    @Override
    public CategoryVO getInfo(Long categoryId) {
        CategoryVO category = categoryMapper.getById(categoryId);
        if (Objects.isNull(category)) {
            return category;
        }
        List<CategoryVO> paths = new ArrayList<>();
        paths.add(category);
        getPathNames(paths);
        if (CollUtil.isEmpty(category.getCategories())) {
            category.setCategories(new ArrayList<>());
        }
        return category;
    }


    @Override
//    @Cacheable(cacheNames = CacheNames.CATEGORY_INFO, key = "#categoryId")
    public CategoryVO getById(Long categoryId) {
        CategoryVO category = categoryMapper.getById(categoryId);
        if (Objects.isNull(category)) {
            throw new LuckException("分类不存在");
        }
        // 如果是三级分类，添加一级分类id
        if (Objects.equals(category.getLevel(), CategoryLevel.THIRD.value())) {
            CategoryVO categoryVO = categoryMapper.getById(category.getParentId());
            category.setPrimaryCategoryId(categoryVO.getParentId());
            category.setSecondaryCategoryId(category.getParentId());
        }
        return category;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(CategoryDTO categoryDTO) {
        categoryDTO.setCategoryId(null);
        Category category = mapperFacade.map(categoryDTO, Category.class);
//        Long countByName = categoryMapper.getCountByName(categoryDTO.getName(), categoryDTO.getShopId(), categoryDTO.getCategoryId());
//        if (countByName!= null && countByName > 0){
//            throw new LuckException("分类名" + categoryDTO.getName() + "已存在，请重新输入");
//        }
        String path = "";
        if (!Objects.equals(CategoryLevel.First.value(), category.getLevel())) {
            String parentId = String.valueOf(category.getParentId());
            CategoryVO categoryDb = categoryMapper.getById(category.getParentId());
            category.setStatus(categoryDb.getStatus());
            if (StrUtil.isBlank(categoryDb.getPath())) {
                path = parentId;
            } else {
                path = categoryDb.getPath() + Constant.CATEGORY_INTERVAL + parentId;
            }
        }
        category.setPath(path);
        categoryMapper.save(category);

        if(productConfigProperties.getSyncZhlsData()){
            RedisUtil.del(ProductCacheNames.CATEGORY_ALL);
            SendResult sendResult = this.syncZhlsProductTemplate.syncSend(category, RocketMqConstant.SYNC_ZHLS_CATEGORY_UPDATE_TAG);
            log.info("新增分类同步有数:{}",JSON.toJSONString(sendResult));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveAndReturnId(CategoryDTO categoryDTO) {
        categoryDTO.setCategoryId(null);
        Category category = mapperFacade.map(categoryDTO, Category.class);
        categoryLangService.existCategoryName(categoryDTO);
        String path = "";
        if (!Objects.equals(CategoryLevel.First.value(), category.getLevel())) {
            String parentId = String.valueOf(category.getParentId());
            CategoryVO categoryDb = categoryMapper.getById(category.getParentId());
            category.setStatus(categoryDb.getStatus());
            if (StrUtil.isBlank(categoryDb.getPath())) {
                path = parentId;
            } else {
                path = categoryDb.getPath() + Constant.CATEGORY_INTERVAL + parentId;
            }
        }
        category.setPath(path);
        categoryMapper.save(category);
        categoryLangService.save(categoryDTO.getCategoryLangList(), category.getCategoryId());
        return category.getCategoryId();
    }

    @Override
    public void update(CategoryDTO categoryDTO) {
        Category category = mapperFacade.map(categoryDTO, Category.class);
        CategoryVO categoryDb = this.getById(category.getCategoryId());
        if (Objects.equals(categoryDb.getCategoryId(), category.getParentId())) {
            throw new LuckException("分类不能成为本身的上级分类");
        }
//        Long countByName = categoryMapper.getCountByName(categoryDTO.getName(), categoryDTO.getShopId(), categoryDTO.getCategoryId());
//        if (countByName!= null && countByName > 0){
//            throw new LuckException("分类名" + categoryDTO.getName() + "已存在，请重新输入");
//        }
        categoryMapper.update(category);
        if (Objects.equals(categoryDb.getShopId(), Constant.PLATFORM_SHOP_ID)) {
            if (Objects.equals(categoryDTO.getLevel(), CategoryLevel.SECOND.value())) {
                List<Long> categoryIds = categoryMapper.listCategoryIdByShopIdAndParentId(Constant.PLATFORM_SHOP_ID, category.getCategoryId());
                categoryShopService.removeCacheByChangeCategoryIds(categoryIds);
            } else if (Objects.equals(categoryDTO.getLevel(), CategoryLevel.THIRD.value())) {
                // 如果更新的分类为三级分类，需要更新店铺签约分类缓存
                categoryShopService.removeCacheByChangeCategoryId(categoryDTO.getCategoryId());
            }
        }

        if(productConfigProperties.getSyncZhlsData()){
            RedisUtil.del(ProductCacheNames.CATEGORY_ALL);
            SendResult sendResult = this.syncZhlsProductTemplate.syncSend(category, RocketMqConstant.SYNC_ZHLS_CATEGORY_UPDATE_TAG);
            log.info("修改分类同步有数:{}",JSON.toJSONString(sendResult));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long categoryId, Long shopId) {
        CategoryVO categoryVO = categoryMapper.selectById(categoryId);

        List<CategoryUseNumVO> numList = categoryMapper.getCategoryUseNum(categoryId,shopId);
        verifyCategoryUseNum(numList);
        categoryMapper.deleteById(categoryId);

        if(productConfigProperties.getSyncZhlsData()){
            RedisUtil.del(ProductCacheNames.CATEGORY_ALL);
            SendResult sendResult = this.syncZhlsProductTemplate.syncSend(categoryVO, RocketMqConstant.SYNC_ZHLS_CATEGORY_DEL_TAG);
            log.info("删除分类同步有数:{}",JSON.toJSONString(sendResult));
        }
    }

    /**
     * 检查分类的使用数量
     * @param numList
     */
    private void verifyCategoryUseNum(List<CategoryUseNumVO> numList) {
        StringBuilder stringBuilder = new StringBuilder("还有");
        int count = 0;
        for (CategoryUseNumVO categoryUseNumVO : numList) {
            if (categoryUseNumVO.getNum() > 0) {
                switch (categoryUseNumVO.getType()) {
                    case 1:
                        stringBuilder.append(categoryUseNumVO.getNum()).append("个分类,");
                        break;
                    case 2:
                        stringBuilder.append(categoryUseNumVO.getNum()).append("个属性,");
                        break;
                    case 3:
                        stringBuilder.append(categoryUseNumVO.getNum()).append("个品牌,");
                        break;
                    case 4:
                        stringBuilder.append(categoryUseNumVO.getNum()).append("件商品,");
                        break;
                    default:
                }
            }
            count = count + categoryUseNumVO.getNum();
        }
        if (count > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            stringBuilder.append("在使用该分类，不能进行删除操作");
            throw new LuckException(stringBuilder.toString());
        }
    }

    @Override
    public List<CategoryAppVO> list(Long shopId) {
        List<CategoryVO> list = categoryMapper.list(shopId, null);
//        ProductLangUtil.categoryList(list);
        return mapperFacade.mapAsList(list, CategoryAppVO.class);
    }

    @Override
    public List<Long> listCategoryId(Long shopId, Long parentId) {
        return categoryMapper.listCategoryId(shopId, parentId);
    }

    @Override
    public void removeCategoryCache(Long shopId, Long categoryId) {
        List<String> key = new ArrayList<>();
        if (Objects.nonNull(categoryId)) {
            key.add(CacheNames.CATEGORY_INFO + CacheNames.UNION + categoryId);
        }
        if (Objects.equals(shopId,Constant.PLATFORM_SHOP_ID)) {
            key.add(CacheNames.CATEGORY_RATE + CacheNames.UNION + "0");
        }
        key.add(CacheNames.CATEGORY_LIST_OF_SHOP + CacheNames.UNION + shopId + Constant.COLON + LanguageEnum.LANGUAGE_ZH_CN.getLang());
        key.add(CacheNames.CATEGORY_LIST_OF_SHOP + CacheNames.UNION + shopId + Constant.COLON + LanguageEnum.LANGUAGE_EN.getLang());
        key.add(CacheNames.CATEGORY_LIST_OF_SHOP + CacheNames.UNION + shopId );
        if (CollUtil.isNotEmpty(key)) {
            RedisUtil.deleteBatch(key);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void categoryEnableOrDisable(CategoryDTO categoryDTO) {
        CategoryVO categoryDb = getById(categoryDTO.getCategoryId());
//        if (!Objects.equals(categoryDb.getShopId(), Constant.PLATFORM_SHOP_ID) &&
//                !Objects.equals(categoryDb.getShopId(), AuthUserContext.get().getTenantId())) {
//            throw new LuckException(ResponseEnum.UNAUTHORIZED);
//        }
        // 如果是重复提交，则直接返回
        if (Objects.equals(categoryDb.getStatus(), categoryDTO.getStatus())) {
            return;
        }
        // 如果该分类不是一级分类，并且是上架操作，需要校验该分类的上级分类是否为上架状态
        if (!Objects.equals(categoryDb.getLevel(), CategoryLevel.First.value()) && Objects.equals(categoryDTO.getStatus(), StatusEnum.ENABLE.value())) {
            CategoryVO parentCategoryVO = getById(categoryDb.getParentId());
            if (!Objects.equals(parentCategoryVO.getStatus(), StatusEnum.ENABLE.value())) {
                throw new LuckException("上架该分类前，需要把该分类的上级分类上架");
            }
        }
        List<Long> updateList = new ArrayList<>();
        List<Long> thirdIdList = new ArrayList<>();
        List<Long> platformThirdIdList = new ArrayList<>();
        if (!categoryDb.getLevel().equals(CategoryLevel.THIRD.value())) {
            // 如果是店铺的二级还需要将分类id放进去
            if(!Objects.equals(categoryDb.getShopId(),Constant.PLATFORM_SHOP_ID)){
                thirdIdList.add(categoryDb.getCategoryId());
            }
            if (Objects.equals(categoryDTO.getStatus(), StatusEnum.DISABLE.value()) || Objects.equals(categoryDTO.getStatus(), StatusEnum.OFFLINE.value())) {
                List<Category> categoryList = categoryMapper.getChildCategory(categoryDb.getCategoryId());
                categoryList.forEach(category -> {
                    updateList.add(category.getCategoryId());
                    if (Objects.equals(categoryDb.getShopId(), Constant.PLATFORM_SHOP_ID) && Objects.equals(category.getLevel(), CategoryLevel.THIRD.value())) {
                        thirdIdList.add(category.getCategoryId());
                        platformThirdIdList.add(category.getCategoryId());
                    } else if (!Objects.equals(categoryDb.getShopId(), Constant.PLATFORM_SHOP_ID) && Objects.equals(category.getLevel(), CategoryLevel.SECOND.value())) {
                        thirdIdList.add(category.getCategoryId());
                    }
                });
            }
        } else {
            updateList.add(categoryDb.getCategoryId());
            thirdIdList.add(categoryDb.getCategoryId());
            platformThirdIdList.add(categoryDb.getCategoryId());
        }
        // 更新该分类的下级分类状态
        updateList.add(categoryDb.getCategoryId());
        categoryMapper.updateBatchOfStatus(updateList, categoryDTO.getStatus());

        if(productConfigProperties.getSyncZhlsData()){
            RedisUtil.del(ProductCacheNames.CATEGORY_ALL);
            Category category = mapperFacade.map(categoryDb, Category.class);
            category.setStatus(categoryDTO.getStatus());
            SendResult sendResult = this.syncZhlsProductTemplate.syncSend(category, RocketMqConstant.SYNC_ZHLS_CATEGORY_UPDATE_TAG);
            log.info("修改分类同步有数:{}",JSON.toJSONString(sendResult));
        }

        // 更新店铺签约分类缓存
        categoryShopService.removeCacheByChangeCategoryIds(platformThirdIdList);

        // 分类下架后， 下架分类中的商品
        if (Objects.equals(categoryDTO.getStatus(), StatusEnum.DISABLE.value()) || Objects.equals(categoryDTO.getStatus(), StatusEnum.OFFLINE.value())) {
            if (CollUtil.isEmpty(thirdIdList)) {
                return;
            }
            // 查询出秒杀or团购的商品进行下线
            // 下线掉关联商品的活动
            spuOfflineService.offlineSpuStatusAndActivity(2,null,categoryDb.getShopId(),thirdIdList, null);
            spuTagReferenceService.deleteSpuTagByShopIdAndCategoryIds(categoryDb.getShopId(), thirdIdList);
        }
    }

    @Override
    public void getPathNames(List<CategoryVO> categories) {
        if (CollUtil.isEmpty(categories)) {
            return;
        }
        // 获取分类的所有上级分类id集合
        Set<Long> paths = new HashSet<>();
        for (CategoryVO category : categories) {
            if (Objects.isNull(category) || StrUtil.isBlank(category.getPath())) {
                continue;
            }
            String[] parentIds = category.getPath().split(Constant.CATEGORY_INTERVAL);
            for (String parentId : parentIds) {
                paths.add(Long.valueOf(parentId));
            }
        }
        if (CollUtil.isEmpty(paths)) {
            return;
        }

        // 获取所有上级分类id列表
        List<CategoryVO> listByCategoryIds = categoryMapper.getListByCategoryIds(paths);
        ProductLangUtil.categoryList(listByCategoryIds);
        Map<Long, CategoryVO> categoryMap = listByCategoryIds.stream().collect(Collectors.toMap(CategoryVO::getCategoryId, c -> c));
        // 获取每个分类的上级分类名称集合
        for (CategoryVO category : categories) {
            if (StrUtil.isBlank(category.getPath())) {
                continue;
            }
            String[] parentIdArray = category.getPath().split(Constant.CATEGORY_INTERVAL);
            category.setCategories(new ArrayList<>());
            for (String s : parentIdArray) {
                CategoryVO categoryVO = categoryMap.get(Long.valueOf(s));
                category.getCategories().add(categoryVO);
            }
        }
    }

    @Override
    public List<CategoryAppVO> listByShopIdAndParenId(Long shopId, Long parentId, Integer lang) {
        CategoryServiceImpl categoryService = (CategoryServiceImpl) AopContext.currentProxy();
        List<CategoryAppVO> allList = categoryService.shopCategoryList(shopId);
        List<CategoryAppVO> categoryList = new ArrayList<>();
        for (CategoryAppVO categoryVO : allList) {
            // 一级分类
            if (Objects.equals(parentId, Constant.DEFAULT_ID)) {
                categoryVO.setCategories(null);
                categoryList.add(categoryVO);
                continue;
            }
            // 二级分类
            if (Objects.equals(parentId, categoryVO.getCategoryId())) {
                for (CategoryAppVO category : categoryVO.getCategories()) {
                    category.setCategories(null);
                    categoryList.add(category);
                    continue;
                }
                return categoryList;
            }
            // 三级分类
            for (CategoryAppVO category : categoryVO.getCategories()) {
                if (Objects.equals(parentId, category.getCategoryId())) {
                    categoryList.addAll(category.getCategories());
                    return categoryList;
                }
            }
        }
        return categoryList;
    }

    @Override
    @Cacheable(cacheNames = CacheNames.CATEGORY_LIST_OF_SHOP, key = "#shopId")
    public List<CategoryAppVO> shopCategoryList(Long shopId) {
        List<CategoryVO> list = categoryMapper.listByShopIdAndParenId(shopId, null);
//        ProductLangUtil.categoryList(list);

        Map<Integer, List<CategoryAppVO>> categoryMap = mapperFacade.mapAsList(list, CategoryAppVO.class).stream().collect(Collectors.groupingBy(CategoryAppVO::getLevel));

        List<CategoryAppVO> secondCategories = categoryMap.get(CategoryLevel.SECOND.value());
//        if (Objects.equals(shopId, Constant.PLATFORM_SHOP_ID)) {
            // 三级分类
            List<CategoryAppVO> thirdCategories = categoryMap.get(CategoryLevel.THIRD.value());
            //二级分类
            setChildCategory(secondCategories, thirdCategories);
//        }
        //一级分类
        List<CategoryAppVO> firstCategories = categoryMap.get(CategoryLevel.First.value());
        setChildCategory(firstCategories, secondCategories);
        if (Objects.isNull(firstCategories)) {
            firstCategories = new ArrayList<>();
        }
        firstCategories.sort(Comparator.comparing(CategoryAppVO::getSeq).reversed().thenComparing(CategoryAppVO::getCategoryId));
        return firstCategories;
    }

    @Override
    @Cacheable(cacheNames = CacheNames.LIST_SIGNING_CATEGORY, key = "#shopId + ':' + #lang")
    public List<CategoryAppVO> listSigningCategory(Long shopId, Integer lang) {
        List<CategoryVO> threeCategoryList = categoryMapper.listSigningCategoryByShopIdAndStatus(shopId, StatusEnum.ENABLE.value());
        if (Objects.isNull(threeCategoryList) || threeCategoryList.size() == 0) {
            // 签约分类为空时
            return new ArrayList<>();
        }
        ProductLangUtil.categoryList(threeCategoryList);
        List<CategoryVO> twoCategoryList = categoryMapper.getListByCategoryIds(threeCategoryList.stream().map(CategoryVO::getParentId).collect(Collectors.toSet()));
        ProductLangUtil.categoryList(twoCategoryList);
        List<CategoryVO> oneCategoryList = categoryMapper.getListByCategoryIds(twoCategoryList.stream().map(CategoryVO::getParentId).collect(Collectors.toSet()));
        ProductLangUtil.categoryList(oneCategoryList);
        threeCategoryList.addAll(twoCategoryList);
        threeCategoryList.addAll(oneCategoryList);
        return mapperFacade.mapAsList(threeCategoryList, CategoryAppVO.class);
    }

    @Override
    public List<CategoryVO> listByCategoryIds(Set<Long> categoryIds) {
        return categoryMapper.listByCategoryIds(categoryIds);
    }

    @Override
    public List<CategoryVO> listAndLangInfoByShopId(Long shopId) {
        return categoryMapper.list(shopId, CategoryLevel.SECOND.value());
    }

    @Override
    public List<CategoryVO> getShopSigningCategoryAndLangInfo(Long shopId) {
        return categoryMapper.listSigningCategoryByShopIdAndStatus(shopId, null);
    }

    @Override
    public List<CategoryAppVO> platformCategories(Long shopId) {
        List<CategoryAppVO> list = list(shopId);
        // 排序规则：分类等级正序-> 分类序号倒序-> 分类id正序
        list.sort(Comparator.comparingInt(CategoryAppVO::getLevel)
                .thenComparingInt(CategoryAppVO::getSeq).reversed()
                .thenComparingLong(CategoryAppVO::getCategoryId)
        );
        return list;
    }

    @Override
    public List<Long> getParentIdsByCategoryId(List<Long> categoryIds) {
        if (CollUtil.isEmpty(categoryIds)) {
            return null;
        }
        return categoryMapper.getParentIdsByCategoryId(categoryIds);
    }

    @Override
    public List<PlatformCommissionOrderItemBO> listBySkuIds(List<PlatformCommissionOrderItemBO> platformCommissionOrderItems) {
        return categoryMapper.listBySkuIds(platformCommissionOrderItems);
    }

    @Override
    @Cacheable(cacheNames = CacheNames.CATEGORY_RATE, key = "'0'")
    public List<CategoryRateBO> listRate() {
        return categoryMapper.listRate();
    }

    private void setChildCategory(List<CategoryAppVO> categories, List<CategoryAppVO> childCategories) {
        if (CollUtil.isEmpty(categories)) {
            return;
        }
        if (CollUtil.isEmpty(childCategories)) {
            categories.clear();
            return;
        }
        Map<Long, List<CategoryAppVO>> secondCategoryMap = childCategories.stream().collect(Collectors.groupingBy(CategoryAppVO::getParentId));
        Iterator<CategoryAppVO> iterator = categories.iterator();
        while (iterator.hasNext()) {
            CategoryAppVO categoryVO = iterator.next();
            List<CategoryAppVO> categoryList = secondCategoryMap.get(categoryVO.getCategoryId());
            if (CollUtil.isEmpty(categoryList)) {
                iterator.remove();
                continue;
            }
            categoryList.forEach(item -> item.setParentName(categoryVO.getName()));
            categoryList.sort(Comparator.comparingInt(CategoryAppVO::getSeq).reversed().thenComparing(CategoryAppVO::getCategoryId));
            categoryVO.setCategories(categoryList);
        }
    }

    /**
     * 处理分类名称
     *
     * @param categoryLangList 分类国际化信息
     * @param lang             语言
     * @return 分类名称
     */
    private String handleCategoryName(List<CategoryLangVO> categoryLangList, Integer lang) {
        if (CollUtil.isEmpty(categoryLangList)) {
            return null;
        }
        String categoryName = null;
        for (CategoryLangVO categoryLangVO : categoryLangList) {
            if (Objects.equals(categoryLangVO.getLang(), lang)) {
                categoryName = categoryLangVO.getName();
                break;
            }
        }
        return categoryName;
    }

    /**
     * 爱铺货分类信息同步入库
     *
     * @param iphSyncCategoryDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long sycnIPH(IphSyncCategoryDto iphSyncCategoryDto) {
        Long parentId = iphSyncCategoryDto.getParentId();

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName(iphSyncCategoryDto.getName());

        List<CategoryLangDTO> categoryLangList = new ArrayList<>();
        CategoryLangDTO categoryLangDTO = new CategoryLangDTO();
        categoryLangDTO.setName(iphSyncCategoryDto.getName());
        categoryLangDTO.setLang(1);
        categoryLangList.add(categoryLangDTO);
        categoryDTO.setCategoryLangList(categoryLangList);

        categoryDTO.setShopId(1l);
        categoryDTO.setStatus(1);
        // 第一级节点
        if (parentId == null || parentId == 0l) {
            categoryDTO.setParentId(0l);
            categoryDTO.setLevel(0);
        } else {
            categoryDTO.setParentId(parentId);
            CategoryVO categoryVO = categoryMapper.selectById(parentId);
            categoryDTO.setLevel(categoryVO.getLevel() + 1);
        }
        return this.saveAndReturnId(categoryDTO);
    }

    @Override
    public Boolean categoryShopUpdateBatch(List<CategoryDTO> categoryDTOList) {
        List<Category> categoryList = categoryDTOList.stream().map(categoryDTO -> {
            Category category = new Category();
            BeanUtils.copyProperties(categoryDTO, category);
            category.setUpdateTime(new Date());
            return category;
        }).collect(Collectors.toList());
        return this.updateBatchById(categoryList);
    }

    @Override
    public List<CategoryVO> listbyParntId(Long parentId, Long shopId) {
        return categoryMapper.listbyParentId(parentId, shopId);
    }
}
