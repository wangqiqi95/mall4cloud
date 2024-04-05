package com.mall4j.cloud.product.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.common.cache.constant.ProductCacheNames;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.product.dto.*;
import com.mall4j.cloud.product.mapper.CategoryNavigationRelationMapper;
import com.mall4j.cloud.product.mapper.CategoryNavigationSpuRelationMapper;
import com.mall4j.cloud.product.model.CategoryNavigation;
import com.mall4j.cloud.product.model.CategoryNavigationRelation;
import com.mall4j.cloud.product.model.CategoryNavigationSpuRelation;
import com.mall4j.cloud.product.service.CategoryNavigationRelationService;
import com.mall4j.cloud.product.service.CategoryNavigationService;
import com.mall4j.cloud.product.mapper.CategoryNavigationMapper;
import com.mall4j.cloud.product.service.SkuService;
import com.mall4j.cloud.product.service.SpuService;
import com.mall4j.cloud.product.vo.CategoryNavigationVO;
import com.mall4j.cloud.product.vo.CategorySpuVO;
import com.mall4j.cloud.product.vo.SpuPageVO;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 */
@Service
public class CategoryNavigationServiceImpl extends ServiceImpl<CategoryNavigationMapper, CategoryNavigation>
    implements CategoryNavigationService{

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private CategoryNavigationMapper categoryNavigationMapper;

    @Autowired
    private CategoryNavigationRelationService categoryNavigationRelationService;

    @Autowired
    private CategoryNavigationRelationMapper categoryNavigationRelationMapper;

    @Autowired
    private CategoryNavigationSpuRelationMapper categoryNavigationSpuRelationMapper;

    @Autowired
    private SpuService spuService;

    @Autowired
    private SkuService skuService;

    @Autowired
    private StoreFeignClient storeFeignClient;

    @Override
    public List<CategoryNavigationVO> listAllOrder() {
        List<CategoryNavigation> categoryNavigations = categoryNavigationMapper.selectListAll(null);
        categoryNavigations.sort(Comparator.comparingInt(CategoryNavigation::getLevel)
                .thenComparingInt(CategoryNavigation::getSeq).reversed()
                .thenComparingLong(CategoryNavigation::getCategoryId)
        );

        Set<Long> idSet = categoryNavigationMapper.selectListByIsLastLevel(1).stream().map(CategoryNavigation::getParentId).collect(Collectors.toSet());
        Map<Long, CategoryNavigationVO> map = mapperFacade.mapAsList(categoryNavigations, CategoryNavigationVO.class).stream().collect(Collectors.toMap(CategoryNavigationVO::getCategoryId, Function.identity()));
        for (Long aLong : idSet) {
            CategoryNavigationVO categoryNavigationVO = map.get(aLong);
            if (Objects.nonNull(categoryNavigationVO)) {
                categoryNavigationVO.setIsLastLevelChild(1);
            }
        }

        return new ArrayList<>(map.values());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = ProductCacheNames.CATEGORY_NAVIGATION_ALL, allEntries = true)
    public void saveCategory(CategoryNavigationVO categoryNavigationVO) {
        CategoryNavigation categoryNavigation = mapperFacade.map(categoryNavigationVO, CategoryNavigation.class);
        if (categoryNavigation.getLevel() > 4) {
            throw new LuckException("最大层级不能大于 4层");
        }

        // 如果层级等于4， 则必须为末级
        if (categoryNavigation.getIsLastLevel() == 4) {
            categoryNavigation.setIsLastLevel(1);
        }

        categoryNavigation.setCategoryId(null);
        categoryNavigationMapper.insertCategory(categoryNavigation);
        // 关系维护
        categoryNavigationRelationService.insertCategoryRelation(categoryNavigation);
        // 路径维护
        updateCategoryPath(categoryNavigation.getCategoryId());
    }


    /**
     * 类目路径维护
     * @param categoryId 类目ID
     */
    private void updateCategoryPath(Long categoryId) {
        List<Long> ancestorCategoryIdList = categoryNavigationRelationMapper
                .selectListByDescendantCategoryId(categoryId)
                .stream()
                .map(CategoryNavigationRelation::getAncestorCategoryId)
                .collect(Collectors.toList());
        if (!ancestorCategoryIdList.isEmpty()) {
            String path = StrUtil.join(",", ancestorCategoryIdList);
            categoryNavigationMapper.updateCategoryPath(categoryId, path);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = ProductCacheNames.CATEGORY_NAVIGATION_ALL, allEntries = true)
    public void updateCategory(CategoryNavigationVO categoryNavigationVO) {
        CategoryNavigation categoryNavigation = mapperFacade.map(categoryNavigationVO, CategoryNavigation.class);
        // 这里不能修改状态
        categoryNavigation.setStatus(null);
        // 这里不能修改父级
        categoryNavigation.setParentId(null);
        categoryNavigationMapper.updateCategory(categoryNavigation);
        // 关系维护
//        CategoryNavigationRelation categoryNavigationRelation = new CategoryNavigationRelation();
//        categoryNavigationRelation.setAncestorCategoryId(categoryNavigation.getParentId());
//        categoryNavigationRelation.setDescendantCategoryId(categoryNavigation.getCategoryId());
//        categoryNavigationRelationService.updateCategoryRelation(categoryNavigationRelation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = ProductCacheNames.CATEGORY_NAVIGATION_ALL, allEntries = true)
    public void enableOrDisable(CategoryNavigationStatusDTO categoryNavigationStatusDTO) {
        Long categoryId = categoryNavigationStatusDTO.getCategoryId();
        Integer isEnable = categoryNavigationStatusDTO.getIsEnable();

        CategoryNavigation categoryNavigation = categoryNavigationMapper.selectById(categoryId);
        if (Objects.isNull(categoryNavigation)) {
            throw new LuckException("类目不存在");
        }
        if (Objects.equals(isEnable, categoryNavigation.getStatus())) {
            throw new LuckException(Objects.equals(isEnable, 1)? "该分类已启用" : "该分类已禁用");
        }

        // 如果是启用 则需检测上级是否启用
        if (Objects.equals(isEnable, 1)){
            checkAncestorIsEnable(categoryId);
            categoryNavigationMapper.updateStatusById(categoryId, isEnable);
        } else {
            // 如果是禁用 则需禁用本身以及所有下级
            List<CategoryNavigationRelation> categoryNavigationRelationList = categoryNavigationRelationMapper
                    .selectListByAncestorCategoryId(categoryId);
            List<Long> descendantIdList = categoryNavigationRelationList.stream()
                    .map(CategoryNavigationRelation::getDescendantCategoryId)
                    .collect(Collectors.toList());
            categoryNavigationMapper.updateStatusByInIds(descendantIdList, isEnable);
            // 所属的所有商品也需要全部禁用
            categoryNavigationSpuRelationMapper.updateEnableByInCategoryId(descendantIdList, isEnable);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = ProductCacheNames.CATEGORY_NAVIGATION_ALL, allEntries = true)
    public void removeCategory(Long categoryId) {
        CategoryNavigation categoryNavigation = categoryNavigationMapper.selectById(categoryId);
        if (Objects.isNull(categoryNavigation)) {
            throw new LuckException("类目不存在");
        }

        List<Long> descendantIdList = categoryNavigationRelationMapper.selectListByAncestorCategoryId(categoryId).stream()
                .map(CategoryNavigationRelation::getDescendantCategoryId)
                .collect(Collectors.toList());
        // 当下级数量大于1时（1是自身所以要大于1）
        if (descendantIdList.size() > 1) {
            throw new LuckException("该分类还有下级, 需先删除下级分类");
        }

        // 假删、设置已删除的状态
        categoryNavigationMapper.fakeDelete(categoryId);
        // 关系维护
        categoryNavigationRelationService.deleteAllCategoryRelation(categoryId);
        // 商品分类关系也要删除
        categoryNavigationSpuRelationMapper.deleteByCategoryId(categoryId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addSpu(CategoryNavigationSpuRelationDTO categoryNavigationSpuRelationDTO) {
        Long categoryId = categoryNavigationSpuRelationDTO.getCategoryId();
        List<Long> spuIds = categoryNavigationSpuRelationDTO.getSpuIds();
        CategoryNavigation categoryNavigation = categoryNavigationMapper.selectById(categoryId);
        if (Objects.isNull(categoryNavigation)) {
            throw new LuckException("类目不存在");
        }

        for (Long spuId : spuIds) {
            CategoryNavigationSpuRelation relation = categoryNavigationSpuRelationMapper.selectByCategoryIdAndSpuId(categoryId, spuId);
            if (Objects.nonNull(relation)) {
                throw new LuckException("不能选择相同分类");
            }
        }

        // 先删除关系
        categoryNavigationSpuRelationMapper.deleteByCategoryIdAndInSpuIds(categoryId, spuIds);
        // 再新增关系
        List<CategoryNavigationSpuRelation> categoryNavigationSpuRelationList = new ArrayList<>();
        for (Long spuId : spuIds) {
            CategoryNavigationSpuRelation categoryNavigationSpuRelation = new CategoryNavigationSpuRelation();
            categoryNavigationSpuRelation.setCategoryId(categoryId);
            categoryNavigationSpuRelation.setSpuId(spuId);
            categoryNavigationSpuRelationList.add(categoryNavigationSpuRelation);
        }
        categoryNavigationSpuRelationMapper.insertBatch(categoryNavigationSpuRelationList);
    }

    @Override
    public void removeSpu(CategoryNavigationSpuRelationDTO categoryNavigationSpuRelationDTO) {
        Long categoryId = categoryNavigationSpuRelationDTO.getCategoryId();
        List<Long> spuIds = categoryNavigationSpuRelationDTO.getSpuIds();
        CategoryNavigation categoryNavigation = categoryNavigationMapper.selectById(categoryId);
        if (Objects.isNull(categoryNavigation)) {
            throw new LuckException("类目不存在");
        }
        categoryNavigationSpuRelationMapper.deleteByCategoryIdAndInSpuIds(categoryId, spuIds);
    }

    @Override
    public PageVO<CategorySpuVO> pageSpu(PageDTO pageDTO, Long categoryId, Long storeId) {
        PageVO<CategorySpuVO> vo = PageUtil.doPage(pageDTO, () -> categoryNavigationSpuRelationMapper.selectSpuVOListByCategoryId(categoryId));
        if (vo.getList().isEmpty()){
            return vo;
        }

        List<Long> spuIdList = vo.getList().stream().map(CategorySpuVO::getSpuId).distinct().collect(Collectors.toList());
        List<String> pathList = vo.getList().stream().map(CategorySpuVO::getPath).collect(Collectors.toList());
        Set<String> pathCategoryIdSet = new HashSet<>();
        for (String path : pathList) {
            String[] split = path.split(",");
            pathCategoryIdSet.addAll(Arrays.asList(split));
        }

        Map<Long, CategoryNavigation> categoryNavigationMap = categoryNavigationMapper.selectBatchIds(pathCategoryIdSet).stream().collect(Collectors.toMap(CategoryNavigation::getCategoryId, Function.identity()));
        for (CategorySpuVO categorySpuVO : vo.getList()) {
            String[] split = categorySpuVO.getPath().split(",");
            List<String> pathStrList = new ArrayList<>();
            for (String s : split) {
                Long id = Long.valueOf(s);
                CategoryNavigation orDefault = categoryNavigationMap.getOrDefault(id, new CategoryNavigation());
                pathStrList.add(orDefault.getName());
            }
            categorySpuVO.setPathStr(StrUtil.join("-", pathStrList));
        }

        if(CollectionUtil.isNotEmpty(spuIdList)){
            List<SkuPriceDTO> appSkuPriceBySkuIdList = skuService.getAppSkuPriceBySkuIdList(spuIdList, storeId);
            if(CollectionUtil.isNotEmpty(appSkuPriceBySkuIdList)){
                Map<Long, List<SkuPriceDTO>> skuMap = appSkuPriceBySkuIdList.stream().collect(Collectors.groupingBy(SkuPriceDTO::getSpuId));
                vo.getList().forEach(item -> {
                    List<SkuPriceDTO> skuPriceDTOS=skuMap.get(item.getSpuId());
                    if(CollectionUtil.isNotEmpty(skuPriceDTOS)){
                        SkuPriceDTO skuAppVO1 = skuPriceDTOS.stream().min(Comparator.comparing(SkuPriceDTO::getPriceFee)).get();
                        item.setPriceFee(skuAppVO1.getPriceFee());
                    }
                });
            }
        }

        return vo;
    }

    @Override
    @Cacheable(cacheNames = ProductCacheNames.CATEGORY_NAVIGATION_ALL, sync = true, condition = "#categoryId == 0L and #isCascade == 1")
    public List<CategoryNavigationVO> listCacheCascadeCategory(Long categoryId, Integer isCascade) {
        // 不级联则查询所有 categoryId 的子级
        if (Objects.equals(isCascade, 0)) {
            List<CategoryNavigation> categoryNavigationList = categoryNavigationMapper.selectListByParentId(categoryId);
            categoryNavigationList.sort(Comparator.comparingInt(CategoryNavigation::getLevel)
                    .thenComparingInt(CategoryNavigation::getSeq).reversed()
                    .thenComparingLong(CategoryNavigation::getCategoryId)
            );
            return mapperFacade.mapAsList(categoryNavigationList, CategoryNavigationVO.class);
        } else {
            List<CategoryNavigation> categoryNavigationList;
            // 如果是父级是 0 则表示查询所有
            if (Objects.equals(categoryId, 0L)){
                categoryNavigationList = categoryNavigationMapper.selectListAll(1);
            } else {
                List<Long> descendantCategoryIdList = categoryNavigationRelationMapper.selectListByAncestorCategoryId(categoryId)
                        .stream()
                        .map(CategoryNavigationRelation::getDescendantCategoryId)
                        .collect(Collectors.toList());
                if (descendantCategoryIdList.isEmpty()) {
                    return new ArrayList<>();
                }
                categoryNavigationList = categoryNavigationMapper.selectBatchIds(descendantCategoryIdList);
            }
            categoryNavigationList.sort(Comparator.comparingInt(CategoryNavigation::getLevel)
                    .thenComparingInt(CategoryNavigation::getSeq).reversed()
                    .thenComparingLong(CategoryNavigation::getCategoryId)
            );
            return buildCategoryTree(categoryNavigationList, categoryId);
        }

    }

    @Override
    public List<CategoryNavigationVO> listCategoryBySpuId(Long spuId) {
        List<Long> categoryIdList = categoryNavigationSpuRelationMapper.selectListBySpuId(spuId)
                .stream()
                .map(CategoryNavigationSpuRelation::getCategoryId)
                .collect(Collectors.toList());

        if (categoryIdList.isEmpty()) {
            return new ArrayList<>();
        }

        List<CategoryNavigation> categoryNavigationList = categoryNavigationMapper.selectBatchIds(categoryIdList);
        if (categoryNavigationList.isEmpty()) {
            return new ArrayList<>();
        }

        List<String> pathList = categoryNavigationList.stream().map(CategoryNavigation::getPath).collect(Collectors.toList());
        Set<String> pathCategoryIdSet = new HashSet<>();
        for (String path : pathList) {
            String[] split = path.split(",");
            pathCategoryIdSet.addAll(Arrays.asList(split));
        }

        List<CategoryNavigationVO> voList = mapperFacade.mapAsList(categoryNavigationList, CategoryNavigationVO.class);
        Map<Long, CategoryNavigation> categoryNavigationMap = categoryNavigationMapper.selectBatchIds(pathCategoryIdSet).stream().collect(Collectors.toMap(CategoryNavigation::getCategoryId, Function.identity()));
        for (CategoryNavigationVO categoryNavigationVO : voList) {
            String[] split = categoryNavigationVO.getPath().split(",");
            List<String> pathStrList = new ArrayList<>();
            for (String s : split) {
                Long id = Long.valueOf(s);
                CategoryNavigation orDefault = categoryNavigationMap.getOrDefault(id, new CategoryNavigation());
                pathStrList.add(orDefault.getName());
            }
            categoryNavigationVO.setPathStr(StrUtil.join("-", pathStrList));
        }

        return voList;
    }

    @Override
    public PageVO<SpuAppPageVO> appPageSpu(SpuPageSearchDTO spuPageSearchDTO, Long categoryId, Long storeId) {
        List<Long> descendantCategoryIdList = categoryNavigationRelationMapper.selectListByAncestorCategoryId(categoryId).stream().map(CategoryNavigationRelation::getDescendantCategoryId).collect(Collectors.toList());
        if (descendantCategoryIdList.isEmpty()) {
            PageVO<SpuAppPageVO> vo = new PageVO<>();
            vo.setPages(0);
            vo.setTotal(0L);
            vo.setList(new ArrayList<>());
            return vo;
        }
        PageVO<CategoryNavigationSpuRelation> categoryNavigationSpuRelationPageVO = PageUtil.doPage(spuPageSearchDTO, () ->  categoryNavigationSpuRelationMapper.selectListByInCategoryId(descendantCategoryIdList));
        List<Long> spuIdList = categoryNavigationSpuRelationPageVO.getList().stream().map(CategoryNavigationSpuRelation::getSpuId).collect(Collectors.toList());

        if (spuIdList.isEmpty()) {
            PageVO<SpuAppPageVO> vo = new PageVO<>();
            vo.setPages(0);
            vo.setTotal(0L);
            vo.setList(new ArrayList<>());
            return vo;
        }

        spuPageSearchDTO.setShopCategoryNavigationIdList(descendantCategoryIdList);
        spuPageSearchDTO.setCategoryId(null);
        PageVO<SpuAppPageVO> pageVO = spuService.appPage(spuPageSearchDTO);

        if(CollectionUtil.isNotEmpty(spuIdList)){
            List<SkuPriceDTO> appSkuPriceBySkuIdList = skuService.getAppSkuPriceBySkuIdList(spuIdList, storeId);
            if(CollectionUtil.isNotEmpty(appSkuPriceBySkuIdList)){
                Map<Long, List<SkuPriceDTO>> skuMap = appSkuPriceBySkuIdList.stream().collect(Collectors.groupingBy(SkuPriceDTO::getSpuId));
                pageVO.getList().forEach(item ->{
                    List<SkuPriceDTO> skuPriceDTOS=skuMap.get(item.getSpuId());
                    if(CollectionUtil.isNotEmpty(skuPriceDTOS)){
                        SkuPriceDTO skuAppVO1 = skuPriceDTOS.stream().min(Comparator.comparing(SkuPriceDTO::getPriceFee)).get();
                        item.setPriceFee(skuAppVO1.getPriceFee());
                    }
                });
            }
        }

        return pageVO;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSpuCategory(CategoryNavigationSpuUpdateDTO categoryNavigationSpuUpdateDTO) {
        Long categoryId = categoryNavigationSpuUpdateDTO.getCategoryId();
        Long updatedCategoryId = categoryNavigationSpuUpdateDTO.getUpdatedCategoryId();
        Long spuId = categoryNavigationSpuUpdateDTO.getSpuId();
        if (Objects.equals(categoryId, updatedCategoryId)) {
            throw new LuckException("不能修改为同一个分类");
        }

        CategoryNavigationSpuRelation relation = categoryNavigationSpuRelationMapper.selectByCategoryIdAndSpuId(updatedCategoryId, spuId);
        if (Objects.nonNull(relation)) {
            throw new LuckException("目标分类已存在此商品");
        }

        categoryNavigationSpuRelationMapper.deleteByCategoryIdAndInSpuIds(categoryId, Collections.singletonList(spuId));
        CategoryNavigationSpuRelation spuRelation = new CategoryNavigationSpuRelation();
        spuRelation.setSpuId(spuId);
        spuRelation.setCategoryId(updatedCategoryId);
        categoryNavigationSpuRelationMapper.insertCategoryNavigationSpuRelation(spuRelation);
    }

    /**
     * 构建类目树
     * @param categoryNavigationList 所有类目
     * @param categoryId root id
     * @return tree
     */
    private List<CategoryNavigationVO> buildCategoryTree(List<CategoryNavigation> categoryNavigationList, Long categoryId) {
        List<CategoryNavigationVO> categoryNavigationVOList = mapperFacade.mapAsList(categoryNavigationList, CategoryNavigationVO.class);
        List<CategoryNavigationVO> tree = new ArrayList<>();

        for (CategoryNavigationVO node : categoryNavigationVOList) {
            if (Objects.equals(node.getParentId(), categoryId)) {
                tree.add(node);
            }

            for (CategoryNavigationVO it : categoryNavigationVOList) {
                if (Objects.equals(node.getCategoryId(), it.getParentId())){
                    if (node.getChildren() == null) {
                        node.setChildren(new ArrayList<>());
                    }
                    node.addChildren(it);
                }
            }
        }

        return tree;
    }

    /**
     * 检测其上级分类是否全部启用
     * @param categoryId 本级分类ID
     */
    private void checkAncestorIsEnable(Long categoryId) {
        // 所有上级节点
        List<Long> allAncestorCategoryIdList = categoryNavigationRelationMapper
                .selectListByDescendantCategoryId(categoryId).stream()
                .map(CategoryNavigationRelation::getAncestorCategoryId)
                // 排除自身
                .filter(ancestorCategoryId -> !Objects.equals(categoryId, ancestorCategoryId))
                .collect(Collectors.toList());
        if (allAncestorCategoryIdList.isEmpty()){
            return;
        }
        Integer noEnableCount =  categoryNavigationMapper
               .selectCountByInCategoryIdsAndStatus(allAncestorCategoryIdList, 0);

        if (noEnableCount > 0) {
           throw new LuckException("启用该分类前，请先把上级的分类先启用");
        }
    }


}




