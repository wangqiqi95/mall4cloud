package com.mall4j.cloud.biz.service.cp.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.mall4j.cloud.biz.mapper.cp.CpChatScriptMenuMapper;
import com.mall4j.cloud.biz.model.cp.CpChatScriptMenu;
import com.mall4j.cloud.biz.service.cp.CpChatScriptMenuService;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 话术分类表
 *
 * @author FrozenWatermelon
 * @date 2023-10-26 15:58:03
 */
@Service
public class CpChatScriptMenuServiceImpl implements CpChatScriptMenuService {

    @Autowired
    private CpChatScriptMenuMapper cpChatScriptMenuMapper;

    @Override
    public PageVO<CpChatScriptMenu> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> cpChatScriptMenuMapper.list());
    }

    @Override
    public CpChatScriptMenu getById(Long id) {
        return cpChatScriptMenuMapper.getById(id);
    }

    @Override
    public void save(CpChatScriptMenu cpChatScriptMenu) {
        cpChatScriptMenuMapper.save(cpChatScriptMenu);
    }

    @Override
    public void update(CpChatScriptMenu cpChatScriptMenu) {
        cpChatScriptMenuMapper.update(cpChatScriptMenu);
    }

    @Override
    public void deleteById(Long id) {
        cpChatScriptMenuMapper.deleteById(id);
    }

    @Override
    public List<CpChatScriptMenu> listParent() {
        return cpChatScriptMenuMapper.listParent();
    }

    @Override
    public List<CpChatScriptMenu> listChildren(Long id) {
        return cpChatScriptMenuMapper.listChildren(id);
    }

    @Override
    public List<CpChatScriptMenu> listParentContainChildren(Integer type, Integer isShow) {
        return cpChatScriptMenuMapper.listParentContainChildren(type,isShow);
    }

    @Override
    public void logicDeleteById(Long id) {
        CpChatScriptMenu menu = this.getById(id);

        // 如果传了父分类ID的话就校验是否存在二级分类列表
        List<Integer> matTypeIdList = new ArrayList<>();
        if(Objects.nonNull(menu.getParentId()) && menu.getParentId()!=0){
            //删除二级分类只查询自己下面有没有素材
            matTypeIdList.add(menu.getId());
        }else {
            //删除一级分类，查询出下面子类有没有素材
            List<Integer> checkMaterial = cpChatScriptMenuMapper.checkMaterial(id);
            if(CollectionUtil.isEmpty(checkMaterial)){
//                matTypeIdList.add(id.intValue());
            }else {
                matTypeIdList.addAll(checkMaterial);
            }
        }
        if(CollUtil.isNotEmpty(matTypeIdList)){
            Integer materialCountByMatTypeId = cpChatScriptMenuMapper.getMaterialCountByMatTypeId(matTypeIdList);
            if(materialCountByMatTypeId > 0){
                Assert.faild("该分类下仍有话术，无法删除！");
            }
        }
        cpChatScriptMenuMapper.logicDeleteById(id);
    }
}
