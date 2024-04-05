package com.mall4j.cloud.biz.service.cp.impl;

import com.mall4j.cloud.biz.mapper.cp.CpChatScriptStoreMapper;
import com.mall4j.cloud.biz.model.cp.CpChatScriptStore;
import com.mall4j.cloud.biz.service.cp.CpChatScriptStoreService;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 话术部门 表
 *
 * @author FrozenWatermelon
 * @date 2023-10-26 15:58:03
 */
@Service
public class CpChatScriptStoreServiceImpl implements CpChatScriptStoreService {

    @Autowired
    private CpChatScriptStoreMapper cpChatScriptStoreMapper;

    @Override
    public PageVO<CpChatScriptStore> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> cpChatScriptStoreMapper.list());
    }

    @Override
    public CpChatScriptStore getById(Long id) {
        return cpChatScriptStoreMapper.getById(id);
    }

    @Override
    public void save(CpChatScriptStore cpChatScriptStore) {
        cpChatScriptStoreMapper.save(cpChatScriptStore);
    }

    @Override
    public void update(CpChatScriptStore cpChatScriptStore) {
        cpChatScriptStoreMapper.update(cpChatScriptStore);
    }

    @Override
    public void deleteById(Long id) {
        cpChatScriptStoreMapper.deleteById(id);
    }

    @Override
    public void deleteByScriptId(Long scriptId) {
        cpChatScriptStoreMapper.deleteByScriptId(scriptId);
    }
}
