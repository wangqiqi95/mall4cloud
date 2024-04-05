package com.mall4j.cloud.product.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.product.model.CategoryNavigation;
import com.mall4j.cloud.product.model.CategoryNavigationRelation;
import com.mall4j.cloud.product.service.CategoryNavigationRelationService;
import com.mall4j.cloud.product.mapper.CategoryNavigationRelationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 */
@Service
public class CategoryNavigationRelationServiceImpl extends ServiceImpl<CategoryNavigationRelationMapper, CategoryNavigationRelation>
    implements CategoryNavigationRelationService{

    @Autowired
    private CategoryNavigationRelationMapper categoryNavigationRelationMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertCategoryRelation(CategoryNavigation categoryNavigation){
        // 查询新增分类所属父级的 父级关系 -> 将需要新增的分类 增加到其父级的子级列表中去
        List<CategoryNavigationRelation> categoryNavigationRelationList = categoryNavigationRelationMapper
                .selectListByDescendantCategoryId(categoryNavigation.getParentId())
                .stream()
                .peek(relation -> relation.setDescendantCategoryId(categoryNavigation.getCategoryId()))
                .collect(Collectors.toList());
        if (CollUtil.isNotEmpty(categoryNavigationRelationList)){
            categoryNavigationRelationMapper.insertBatchCategoryNavigationRelation(categoryNavigationRelationList);
        }

        CategoryNavigationRelation categoryNavigationRelation = new CategoryNavigationRelation();
        categoryNavigationRelation.setAncestorCategoryId(categoryNavigation.getCategoryId());
        categoryNavigationRelation.setDescendantCategoryId(categoryNavigation.getCategoryId());
        categoryNavigationRelationMapper.insertCategoryNavigationRelation(categoryNavigationRelation);
    }

    @Override
    public void deleteAllCategoryRelation(Long categoryId){
        categoryNavigationRelationMapper.deleteCategoryRelationsByCategoryId(categoryId);
    }

    @Override
    public void updateCategoryRelation(CategoryNavigationRelation categoryNavigationRelation){
        categoryNavigationRelationMapper.deleteCategoryRelations(categoryNavigationRelation);
        categoryNavigationRelationMapper.insertCategoryRelations(categoryNavigationRelation);
    }
}




