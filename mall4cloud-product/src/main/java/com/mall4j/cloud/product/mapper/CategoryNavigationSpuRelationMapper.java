package com.mall4j.cloud.product.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mall4j.cloud.product.model.CategoryNavigationSpuRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.product.vo.CategorySpuVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Entity com.mall4j.cloud.product.model.CategoryNavigationSpuRelation
 */
public interface CategoryNavigationSpuRelationMapper extends BaseMapper<CategoryNavigationSpuRelation> {

    /**
     * 根据 categoryIdList 修改状态为 isEnable
     * @param categoryIdList 类目ID集合
     * @param isEnable 是否启用
     */
    default void updateEnableByInCategoryId(List<Long> categoryIdList, Integer isEnable){
        update(null, Wrappers.lambdaUpdate(CategoryNavigationSpuRelation.class)
                .set(CategoryNavigationSpuRelation::getIsEnable, isEnable)
                .in(CategoryNavigationSpuRelation::getCategoryId, categoryIdList));
    }

    /**
     * 根据 categoryId and spuId 查询一条
     * @param categoryId 分类ID
     * @param spuId spuId
     * @return bo
     */
    default CategoryNavigationSpuRelation selectByCategoryIdAndSpuId(Long categoryId, Long spuId){
        return selectOne(Wrappers.lambdaQuery(CategoryNavigationSpuRelation.class)
                .eq(CategoryNavigationSpuRelation::getCategoryId, categoryId)
                .eq(CategoryNavigationSpuRelation::getSpuId, spuId));
    }

    /**
     * 根据 categoryId and in spuIds 删除
     * @param categoryId 分类ID
     * @param spuIds 商品ID
     */
    default void deleteByCategoryIdAndInSpuIds(Long categoryId, List<Long> spuIds){
        delete(Wrappers.lambdaQuery(CategoryNavigationSpuRelation.class)
                .eq(CategoryNavigationSpuRelation::getCategoryId, categoryId)
                .in(CategoryNavigationSpuRelation::getSpuId, spuIds));
    }

    /**
     * 批量新增
     * @param categoryNavigationSpuRelationList bo list
     */
    void insertBatch(@Param("categoryNavigationSpuRelationList") List<CategoryNavigationSpuRelation> categoryNavigationSpuRelationList);

    /**
     * 新增一条
     * @param categoryNavigationSpuRelation bo
     */
    default void insertCategoryNavigationSpuRelation(CategoryNavigationSpuRelation categoryNavigationSpuRelation) {
        insert(categoryNavigationSpuRelation);
    }

    /**
     * 根据 categoryId 查询所有
     * @param categoryId 分类ID
     * @return list
     */
    List<CategorySpuVO> selectSpuVOListByCategoryId(@Param("categoryId") Long categoryId);

    /**
     * 根据 spuId 查询关系
     * @param spuId 商品ID
     * @return list bo
     */
    default List<CategoryNavigationSpuRelation> selectListBySpuId(Long spuId) {
        return selectList(Wrappers.lambdaQuery(CategoryNavigationSpuRelation.class)
                .eq(CategoryNavigationSpuRelation::getSpuId, spuId));
    }

    /**
     * 根据 categoryId 查询所有
     * @param categoryIdList 分类ID
     * @return list bo
     */
    default List<CategoryNavigationSpuRelation> selectListByInCategoryId(List<Long> categoryIdList) {
        return selectList(Wrappers.lambdaQuery(CategoryNavigationSpuRelation.class)
                .in(CategoryNavigationSpuRelation::getCategoryId, categoryIdList));
    }

    /**
     * 根据 categoryId 删除所有
     * @param categoryId 分类ID
     */
    default void deleteByCategoryId(Long categoryId) {
        delete(Wrappers.lambdaQuery(CategoryNavigationSpuRelation.class)
                .eq(CategoryNavigationSpuRelation::getCategoryId, categoryId));
    }
}
