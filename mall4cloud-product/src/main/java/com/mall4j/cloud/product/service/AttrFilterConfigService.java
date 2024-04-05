package com.mall4j.cloud.product.service;

import com.mall4j.cloud.common.product.dto.SpuFilterPropertiesDTO;
import com.mall4j.cloud.product.dto.AttrFilterDto;
import com.mall4j.cloud.product.vo.AppAttrVo;

import java.util.List;

/**
 * @Description
 * @Author axin
 * @Date 2023-06-12
 **/
public interface AttrFilterConfigService {


    /**
     * 查询人群及分类映射
     * @return
     */
    SpuFilterPropertiesDTO getFilterProperties();

    /**
     * 根据人群及属性分类值查询 属性及属性值
     * @param dto
     */
    List<AppAttrVo> getFilterPropertiesByAttrValue(AttrFilterDto dto);

    /**
     * 保存映射属性
     * @param dto
     */
    void save(SpuFilterPropertiesDTO dto);

    /**
     * 修改默认属性
     * @param attrId
     */
    void updateDefaultProp(Long attrId,Boolean defaultProp);

    /**
     * 根据属性id查询详情
     * @param attrId
     * @return
     */
    SpuFilterPropertiesDTO getByAttrId(Long attrId);

    /**
     * 批量查询
     * @param attrId
     * @return
     */
    List<SpuFilterPropertiesDTO> getByAttrIds(List<Long> attrId);

    /**
     * 删除属性
     * @param attrId
     */
    void deleteByAttrId(Long attrId);

    /**
     * 删除属性筛选缓存
     */
    void removeCacheAttrFilter();
}
