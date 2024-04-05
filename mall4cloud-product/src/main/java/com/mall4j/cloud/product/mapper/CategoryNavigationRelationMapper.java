package com.mall4j.cloud.product.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mall4j.cloud.product.model.CategoryNavigationRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Entity com.mall4j.cloud.product.model.CategoryNavigationRelation
 */
public interface CategoryNavigationRelationMapper extends BaseMapper<CategoryNavigationRelation> {

    /**
     * 新增一条关系
     * @param categoryNavigationRelation bo
     * @return i
     */
    default int insertCategoryNavigationRelation(CategoryNavigationRelation categoryNavigationRelation){
        return insert(categoryNavigationRelation);
    }

    /**
     * 批量新增
     * @param categoryNavigationRelationList bo list
     */
    void insertBatchCategoryNavigationRelation(@Param("categoryNavigationRelationList") List<CategoryNavigationRelation> categoryNavigationRelationList);

    /**
     * 删除分类 > 删除所有关联此分类子节点的闭包关系
     * @param categoryId 分类ID
     */
    void deleteCategoryRelationsByCategoryId(@Param("categoryId") Long categoryId);

    /**
     * 删除节点数据
     * @param categoryNavigationRelation 关系节点
     */
    void deleteCategoryRelations(@Param("categoryNavigationRelation") CategoryNavigationRelation categoryNavigationRelation);

    /**
     * 新增节点数据
     * @param categoryNavigationRelation 关系节点
     */
    void insertCategoryRelations(@Param("categoryNavigationRelation") CategoryNavigationRelation categoryNavigationRelation);

    /**
     * 根据 DescendantCategoryId 查询关系信息
     * @param descendantCategoryId  子级节点
     * @return list
     */
    default List<CategoryNavigationRelation> selectListByDescendantCategoryId(Long descendantCategoryId){
        return selectList(Wrappers.lambdaQuery(CategoryNavigationRelation.class)
                .eq(CategoryNavigationRelation::getDescendantCategoryId, descendantCategoryId));
    }

    /**
     * 根据 ancestorCategoryId 查询其所有下级
     * @param ancestorCategoryId 祖节点
     * @return list
     */
    default List<CategoryNavigationRelation> selectListByAncestorCategoryId(Long ancestorCategoryId){
        return selectList(Wrappers.lambdaQuery(CategoryNavigationRelation.class)
                .eq(CategoryNavigationRelation::getAncestorCategoryId, ancestorCategoryId));
    }
}

