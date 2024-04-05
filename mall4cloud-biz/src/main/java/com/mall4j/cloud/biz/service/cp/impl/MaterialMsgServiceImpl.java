package com.mall4j.cloud.biz.service.cp.impl;

import com.mall4j.cloud.biz.wx.cp.constant.OriginEumn;
import com.mall4j.cloud.biz.mapper.cp.MaterialMsgMapper;
import com.mall4j.cloud.biz.model.cp.MaterialMsg;
import com.mall4j.cloud.biz.service.cp.MaterialMsgService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 素材消息表
 *
 * @author hwy
 * @date 2022-01-25 20:33:45
 */
@RequiredArgsConstructor
@Service
public class MaterialMsgServiceImpl implements MaterialMsgService {

    private final MaterialMsgMapper materialMsgMapper;

    @Override
    public void save(MaterialMsg materialMsg) {
        materialMsgMapper.save(materialMsg);
    }

    @Override
    public void deleteByMatId(Long matId,OriginEumn originEumn) {
        materialMsgMapper.deleteByMatId(matId,originEumn.getCode());
    }

    @Override
    public List<MaterialMsg> listByMatId(Long matId,OriginEumn originEumn) {
        return materialMsgMapper.listByMatId( matId,originEumn.getCode());
    }

    @Override
    public List<MaterialMsg> waitRefreshMediaIdList() {
        return materialMsgMapper.waitRefreshMediaIdList();
    }

    @Override
    public void refreshMediaId(Long id, String mediaId) {
        materialMsgMapper.refreshMediaId(id,mediaId);
    }
}
