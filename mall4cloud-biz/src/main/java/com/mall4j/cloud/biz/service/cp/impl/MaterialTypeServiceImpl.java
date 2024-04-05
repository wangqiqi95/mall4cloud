package com.mall4j.cloud.biz.service.cp.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.mall4j.cloud.biz.dto.cp.MaterialTypeDTO;
import com.mall4j.cloud.biz.manager.CpMaterialManager;
import com.mall4j.cloud.biz.mapper.cp.MaterialTypeMapper;
import com.mall4j.cloud.biz.model.cp.MaterialType;
import com.mall4j.cloud.biz.service.cp.MaterialTypeService;
import com.mall4j.cloud.biz.vo.cp.MaterialTypeVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;


/**
 * 素材分类表
 *
 * @author hwy
 * @date 2022-01-25 20:33:45
 */
@RequiredArgsConstructor
@Service
public class MaterialTypeServiceImpl implements MaterialTypeService {

    private final MaterialTypeMapper materialTypeMapper;

    @Autowired
    private CpMaterialManager cpMaterialManager;

    @Override
    public MaterialType getById(Long id) {
        return materialTypeMapper.getById(id);
    }

    @Override
    public void save(MaterialType materialType) {
        materialTypeMapper.save(materialType);
    }

    @Override
    public void update(MaterialType materialType) {
        materialTypeMapper.update(materialType);
    }

    @Override
    public void deleteById(Long id) {
        materialTypeMapper.deleteById(id);
    }

    @Override
    public List<MaterialType> listParent( ) {
        return materialTypeMapper.listParent();
    }
    @Override
    public List<MaterialType> listChildren(Long parentId) {
        return materialTypeMapper.listChildren(parentId);
    }
    @Override
    public List<MaterialTypeVO> listParentContainChildren( ) {
        return materialTypeMapper.listParentContainChildren();
    }

    /**
     * 校验数据是否允许删除
     * @param materialTypeDTO 素材分类表入参
     * @return
     */
    @Override
    public Integer checkMaterial(MaterialTypeDTO materialTypeDTO) {
        // 如果传了父分类ID的话就校验是否存在二级分类列表
        List<Integer> matTypeIdList = new ArrayList<>();
        if(Objects.nonNull(materialTypeDTO.getParentId()) && materialTypeDTO.getParentId()!=0){
            //删除二级分类只查询自己下面有没有素材
            matTypeIdList.add(materialTypeDTO.getId());
        }else {
            //删除一级分类，查询出下面子类有没有素材
            List<Integer> checkMaterial = materialTypeMapper.checkMaterial(materialTypeDTO.getId());
            if(CollectionUtil.isEmpty(checkMaterial)){
                matTypeIdList.add(materialTypeDTO.getId());
            }else {
                matTypeIdList.addAll(checkMaterial);
            }
        }
        Integer materialCountByMatTypeId = cpMaterialManager.getMaterialCountByMatTypeId(matTypeIdList,1);
        if(materialCountByMatTypeId > 0){
            return 1;
        }
        return 0;
    }
}
