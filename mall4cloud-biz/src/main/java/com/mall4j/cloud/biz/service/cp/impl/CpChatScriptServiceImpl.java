package com.mall4j.cloud.biz.service.cp.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.biz.dto.cp.CpChatScriptPageDTO;
import com.mall4j.cloud.biz.dto.cp.MaterialUseRecordPageDTO;
import com.mall4j.cloud.biz.mapper.cp.CpChatScriptMapper;
import com.mall4j.cloud.biz.mapper.cp.CpChatScriptStoreMapper;
import com.mall4j.cloud.biz.model.cp.CpChatScript;
import com.mall4j.cloud.biz.model.cp.CpChatScriptStore;
import com.mall4j.cloud.biz.model.cp.CpMaterialUseRecord;
import com.mall4j.cloud.biz.service.cp.CpChatScriptService;
import com.mall4j.cloud.biz.service.cp.CpChatScriptStoreService;
import com.mall4j.cloud.biz.vo.cp.CpChatScriptpageVO;
import com.mall4j.cloud.biz.vo.cp.MaterialBrowseRecordByDayVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 话术表
 *
 * @author FrozenWatermelon
 * @date 2023-10-26 15:58:03
 */
@Service
public class CpChatScriptServiceImpl implements CpChatScriptService {

    @Autowired
    private CpChatScriptMapper cpChatScriptMapper;
    @Autowired
    CpChatScriptStoreService chatScriptStoreService;
    @Autowired
    CpChatScriptStoreMapper cpChatScriptStoreMapper;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private StaffFeignClient staffFeignClient;

    @Override
    public PageVO<CpChatScriptpageVO> page(PageDTO pageDTO, CpChatScriptPageDTO request) {
        PageVO<CpChatScriptpageVO> pageVO = PageUtil.doPage(pageDTO, () -> cpChatScriptMapper.page(request));
        for (CpChatScriptpageVO scriptpageVO : pageVO.getList()) {
            List<CpChatScriptStore> stores = cpChatScriptStoreMapper.getByScriptId(scriptpageVO.getId());
            List<Long> storeIds = stores.stream().map(CpChatScriptStore::getStoreId).collect(Collectors.toList());
            scriptpageVO.setStoreId(storeIds);
        }
        return pageVO;
    }

    @Override
    public List<CpChatScript> getByIds(CpChatScriptPageDTO request) {
        return cpChatScriptMapper.list(request);
    }

    @Override
    public PageVO<CpChatScriptpageVO> appPage(PageDTO pageDTO, CpChatScriptPageDTO request) {
        //根据当前员工部门筛选
        ServerResponseEntity<List<Long>> responseEntity=staffFeignClient.getOrgAndChildByStaffIds(Arrays.asList(AuthUserContext.get().getUserId()));
        ServerResponseEntity.checkResponse(responseEntity);
        request.setOrgIds(responseEntity.getData());
        PageVO<CpChatScriptpageVO> pageVO = PageUtil.doPage(pageDTO, () -> cpChatScriptMapper.appPage(request));
        return pageVO;
    }

    @Override
    public CpChatScript getById(Long id) {
        return cpChatScriptMapper.getById(id);
    }

    @Override
    public CpChatScriptpageVO getDetailById(Long id) {
        CpChatScript cpChatScript = cpChatScriptMapper.getById(id);
        CpChatScriptpageVO cpChatScriptpageVO = mapperFacade.map(cpChatScript, CpChatScriptpageVO.class);

        List<CpChatScriptStore> stores = cpChatScriptStoreMapper.getByScriptId(id);
        List<Long> storeIds = stores.stream().map(CpChatScriptStore::getStoreId).collect(Collectors.toList());
        cpChatScriptpageVO.setStoreId(storeIds);

        return cpChatScriptpageVO;
    }

    @Override
    @Transactional
    public void save(CpChatScript cpChatScript) {
        cpChatScriptMapper.save(cpChatScript);

        if(cpChatScript.getIsAllShop()==0 && CollectionUtil.isNotEmpty(cpChatScript.getStoreIds())){
            for (Long storeId : cpChatScript.getStoreIds()) {
                CpChatScriptStore cpChatScriptStore = new CpChatScriptStore();
                cpChatScriptStore.setScriptId(cpChatScript.getId());
                cpChatScriptStore.setType(cpChatScript.getType());
                cpChatScriptStore.setStoreId(storeId);
                cpChatScriptStore.setCreateTime(new Date());
                cpChatScriptStore.setUpdateTime(new Date());
                chatScriptStoreService.save(cpChatScriptStore);
            }

        }
    }

    @Override
    @Transactional
    public void update(CpChatScript cpChatScript) {

        chatScriptStoreService.deleteByScriptId(cpChatScript.getId());

        cpChatScriptMapper.update(cpChatScript);

        if(cpChatScript.getIsAllShop()==0 && CollectionUtil.isNotEmpty(cpChatScript.getStoreIds())){
            for (Long storeId : cpChatScript.getStoreIds()) {
                CpChatScriptStore cpChatScriptStore = new CpChatScriptStore();
                cpChatScriptStore.setScriptId(cpChatScript.getId());
                cpChatScriptStore.setType(cpChatScript.getType());
                cpChatScriptStore.setStoreId(storeId);
                cpChatScriptStore.setCreateTime(new Date());
                cpChatScriptStore.setUpdateTime(new Date());
                chatScriptStoreService.save(cpChatScriptStore);
            }
        }

    }

    @Override
    public void disableMat(CpChatScript cpChatScript) {
        cpChatScriptMapper.updateStatus(cpChatScript);
    }

    @Override
    public void deleteById(Long id) {
        cpChatScriptMapper.deleteById(id);
    }

    @Override
    public void logicDeleteById(Long id) {
        cpChatScriptMapper.logicDeleteById(id);
    }

    @Override
    public void updateUseNum(CpChatScript cpChatScript) {
        cpChatScriptMapper.updateUseNum(cpChatScript);
    }

}
