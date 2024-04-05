package com.mall4j.cloud.platform.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.platform.model.RenoApply;
import com.mall4j.cloud.platform.mapper.RenoApplyMapper;
import com.mall4j.cloud.platform.service.RenoApplyService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 装修适用门店
 *
 * @author FrozenWatermelon
 * @date 2022-01-27 02:00:54
 */
@Service
public class RenoApplyServiceImpl implements RenoApplyService {

    @Autowired
    private RenoApplyMapper renoApplyMapper;

    @Override
    public PageVO<RenoApply> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> renoApplyMapper.list());
    }

    @Override
    public RenoApply getById(Long id) {
        return renoApplyMapper.getById(id);
    }

    @Override
    public void save(RenoApply renoApply) {
        renoApplyMapper.save(renoApply);
    }

    @Override
    public void update(RenoApply renoApply) {
        renoApplyMapper.update(renoApply);
    }

    @Override
    public void deleteById(Long id) {
        renoApplyMapper.deleteById(id);
    }

    @Override
    public void deleteByRenoIdAndStroreId(Long renoId, Long stroreId) {
        renoApplyMapper.deleteByRenoIdAndStroreId(renoId,stroreId);
    }

    @Override
    public void deleteByRenoId(Long renoId) {
        renoApplyMapper.deleteByRenoId(renoId);
    }

    @Override
    public List<Long> listByRenoId(Long renovationId) {
        return renoApplyMapper.listByRenoId(renovationId);
    }

    @Override
    public void updateStoreId(List<Long> renoApplyStoreList, Long renovationId) {

    }
}
