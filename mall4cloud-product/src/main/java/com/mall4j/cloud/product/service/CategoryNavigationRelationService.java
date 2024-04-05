package com.mall4j.cloud.product.service;

import com.mall4j.cloud.product.model.CategoryNavigation;
import com.mall4j.cloud.product.model.CategoryNavigationRelation;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 */
public interface CategoryNavigationRelationService extends IService<CategoryNavigationRelation> {


    /**
     * 新建分类关系
     * @param categoryNavigation 分类信息
     */
    void insertCategoryRelation(CategoryNavigation categoryNavigation);

    /**
     * 通过ID删除分类关系
     * @param categoryId 分类ID
     */
    void deleteAllCategoryRelation(Long categoryId);

    /**
     * 更新部门关系
     * @param categoryNavigationRelation bo
     */
    void updateCategoryRelation(CategoryNavigationRelation categoryNavigationRelation);

}
