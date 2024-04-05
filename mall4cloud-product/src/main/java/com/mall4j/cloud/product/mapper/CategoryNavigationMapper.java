package com.mall4j.cloud.product.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mall4j.cloud.product.model.CategoryNavigation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Objects;

/**
 * @Entity com.mall4j.cloud.product.model.CategoryNavigation
 */
public interface CategoryNavigationMapper extends BaseMapper<CategoryNavigation> {

    /**
     * 查询所有分类信息，不包括删除的数据
     * @return list
     */
    default List<CategoryNavigation> selectListAll(Integer status){
        return selectList(Wrappers.lambdaQuery(CategoryNavigation.class)
                .eq(CategoryNavigation::getIsDelete, 0)
                .eq(Objects.nonNull(status), CategoryNavigation::getStatus, status));
    }

    /**
     * 插入一条数据
     * @param categoryNavigation bo
     * @return int
     */
    default int insertCategory(CategoryNavigation categoryNavigation){
        return insert(categoryNavigation);
    }

    /**
     * 更新一条数据
     * @param categoryNavigation bo
     */
    default int updateCategory(CategoryNavigation categoryNavigation){
        return updateById(categoryNavigation);
    }

    /**
     * 统计 categoryId IN categoryIdList AND status = status 的数量
     * @param categoryIdList 分类ID集合
     * @param status 状态 1 启用 0 未启用
     * @return count
     */
    default Integer selectCountByInCategoryIdsAndStatus(List<Long> categoryIdList, int status){
        return selectCount(Wrappers.lambdaQuery(CategoryNavigation.class)
                .eq(CategoryNavigation::getIsDelete, 0)
                .in(CategoryNavigation::getCategoryId, categoryIdList)
                .eq(CategoryNavigation::getStatus, status));
    }

    /**
     * 根据 id 修改分类状态为 isEnable
     * @param categoryId 分类ID
     * @param isEnable 状态 1启用 0禁用
     */
    default void updateStatusById(Long categoryId, Integer isEnable){
        update(null, Wrappers.lambdaUpdate(CategoryNavigation.class)
                .set(CategoryNavigation::getStatus, isEnable)
                .eq(CategoryNavigation::getCategoryId, categoryId));
    }

    /**
     * 根据 categoryIdList 修改分类状态为 isEnable
     * @param categoryIdList 分类ID集合
     * @param isEnable 状态 1启用 0禁用
     */
    default void updateStatusByInIds(List<Long> categoryIdList, Integer isEnable){
        update(null, Wrappers.lambdaUpdate(CategoryNavigation.class)
                .set(CategoryNavigation::getStatus, isEnable)
                .in(CategoryNavigation::getCategoryId, categoryIdList));
    }

    /**
     * 假删
     * @param categoryId id
     */
    default void fakeDelete(Long categoryId){
        update(null, Wrappers.lambdaUpdate(CategoryNavigation.class)
                .set(CategoryNavigation::getIsDelete, 1)
                .eq(CategoryNavigation::getCategoryId, categoryId));
    }

    /**
     * 修改路径
     * @param categoryId id
     * @param path 路径
     */
    default void updateCategoryPath(Long categoryId, String path){
        update(null, Wrappers.lambdaUpdate(CategoryNavigation.class)
                .set(CategoryNavigation::getPath, path)
                .eq(CategoryNavigation::getCategoryId, categoryId));
    }

    /**
     * 根据 parentId 查询所有
     * @param parentId 父级ID
     * @return list
     */
    default List<CategoryNavigation> selectListByParentId(Long parentId) {
        return selectList(Wrappers.lambdaQuery(CategoryNavigation.class)
                .eq(CategoryNavigation::getIsDelete, 0)
                .eq(CategoryNavigation::getParentId, parentId));
    }

    /**
     * 查询所有 isLastLevel 的分类
     * @param isLastLevel 是否最后一级
     * @return list
     */
    default List<CategoryNavigation> selectListByIsLastLevel(Integer isLastLevel) {
        return selectList(Wrappers.lambdaQuery(CategoryNavigation.class)
                .eq(CategoryNavigation::getIsDelete, 0)
                .eq(CategoryNavigation::getIsLastLevel, isLastLevel));
    }
}




