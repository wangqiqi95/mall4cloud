package com.mall4j.cloud.product.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.product.dto.*;
import com.mall4j.cloud.product.model.CategoryNavigation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.product.vo.CategoryNavigationVO;
import com.mall4j.cloud.product.vo.CategorySpuVO;
import com.mall4j.cloud.product.vo.SpuPageVO;

import java.util.List;

/**
 *
 */
public interface CategoryNavigationService extends IService<CategoryNavigation> {

    /**
     * 获取所有分类 并排序
     * @return list vo
     */
    List<CategoryNavigationVO> listAllOrder();

    /**
     * 保存一条数据
     * @param categoryNavigationVO vo
     */
    void saveCategory(CategoryNavigationVO categoryNavigationVO);

    /**
     * 修改一条数据
     * @param categoryNavigationVO vo
     */
    void updateCategory(CategoryNavigationVO categoryNavigationVO);

    /**
     * 启用或禁用分类
     * <p>
     *     假如该分类有下级分类，下级分类也将被禁用，商品也会同步下架掉
     * </p>
     * @param categoryNavigationStatusDTO dto
     */
    void enableOrDisable(CategoryNavigationStatusDTO categoryNavigationStatusDTO);


    /**
     * 删除分类
     * @param categoryId 分类ID
     */
    void removeCategory(Long categoryId);

    /**
     * 添加商品
     * @param categoryNavigationSpuRelationDTO dto
     */
    void addSpu(CategoryNavigationSpuRelationDTO categoryNavigationSpuRelationDTO);

    /**
     * 删除商品
     * @param categoryNavigationSpuRelationDTO dto
     */
    void removeSpu(CategoryNavigationSpuRelationDTO categoryNavigationSpuRelationDTO);

    /**
     * 分页查询类目绑定的商品信息
     *
     * @param pageDTO    page dto
     * @param categoryId 分类ID
     * @param storeId 门店ID
     * @return page vo
     */
    PageVO<CategorySpuVO> pageSpu(PageDTO pageDTO, Long categoryId, Long storeId);

    /**
     * 查询级联分类（缓存）
     * @param categoryId 主分类ID
     * @param isCascade 是否级联查询
     * @return cache list
     */
    List<CategoryNavigationVO> listCacheCascadeCategory(Long categoryId, Integer isCascade);

    /**
     * 查询 spuId 下的所有分类信息
     * @param spuId 商品ID
     * @return list vo
     */
    List<CategoryNavigationVO> listCategoryBySpuId(Long spuId);

    /**
     * app 端商品分页信息
     * @param spuPageSearchDTO spu查询dto
     * @param categoryId 分类ID
     * @param storeId 门店ID
     * @return list pageSpu
     */
    PageVO<SpuAppPageVO> appPageSpu(SpuPageSearchDTO spuPageSearchDTO, Long categoryId, Long storeId);

    /**
     * 修改商品的分类信息
     * @param categoryNavigationSpuUpdateDTO update dto
     */
    void updateSpuCategory(CategoryNavigationSpuUpdateDTO categoryNavigationSpuUpdateDTO);
}
