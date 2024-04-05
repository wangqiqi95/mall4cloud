package com.mall4j.cloud.biz.service.cp.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.biz.mapper.cp.CpCodeChannelMapper;
import com.mall4j.cloud.biz.model.cp.CpCodeChannel;
import com.mall4j.cloud.biz.service.cp.CpCodeChannelService;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.security.AuthUserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 活码渠道标识表
 *
 * @author gmq
 * @date 2023-11-01 10:33:33
 */
@Service
public class CpCodeChannelServiceImpl extends ServiceImpl<CpCodeChannelMapper,CpCodeChannel> implements CpCodeChannelService {

    @Autowired
    private CpCodeChannelMapper cpCodeChannelMapper;

    @Override
    public PageVO<CpCodeChannel> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> cpCodeChannelMapper.list());
    }

    @Override
    public CpCodeChannel getById(Long id) {
        return cpCodeChannelMapper.getById(id);
    }

    @Override
    public CpCodeChannel getBySourceState(String sourceState) {
        return StrUtil.isNotBlank(sourceState)?cpCodeChannelMapper.getBySourceState(sourceState):null;
    }

    @Override
    public List<CpCodeChannel> getBySourceStates(List<String> sourceStates) {
        return CollUtil.isNotEmpty(sourceStates)?cpCodeChannelMapper.getBySourceStates(sourceStates): ListUtil.empty();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveCpCodeChannel(Integer sourceFrom, String sourceId,String baseId,String sourceState) {
        CpCodeChannel cpCodeChannel=new CpCodeChannel();
        cpCodeChannel.setSourceFrom(sourceFrom);
        cpCodeChannel.setSourceId(sourceId);
        cpCodeChannel.setBaseId(baseId);
        cpCodeChannel.setSourceState(sourceState);
        cpCodeChannel.setCreateBy(AuthUserContext.get().getUsername());
        cpCodeChannel.setCreateTime(new Date());
        cpCodeChannel.setIsDelete(0);
        this.save(cpCodeChannel);
    }

    @Override
    public void saveCpCodeChannel(List<CpCodeChannel> cpCodeChannels) {
        cpCodeChannels.forEach(cpCodeChannel->{
            cpCodeChannel.setCreateBy(AuthUserContext.get().getUsername());
            cpCodeChannel.setCreateTime(new Date());
            cpCodeChannel.setIsDelete(0);
        });
        this.saveBatch(cpCodeChannels);
    }

    @Override
    public void deleteById(Long id) {
        cpCodeChannelMapper.deleteById(id);
    }
}
