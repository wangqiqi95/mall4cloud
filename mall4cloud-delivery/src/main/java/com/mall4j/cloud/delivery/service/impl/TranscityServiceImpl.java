package com.mall4j.cloud.delivery.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.delivery.model.Transcity;
import com.mall4j.cloud.delivery.mapper.TranscityMapper;
import com.mall4j.cloud.delivery.service.TranscityService;
import com.mall4j.cloud.delivery.vo.TranscityVO;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 运费项和运费城市关联信息
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:01
 */
@Service
public class TranscityServiceImpl implements TranscityService {

    @Autowired
    private TranscityMapper transcityMapper;

    @Override
    public PageVO<TranscityVO> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> transcityMapper.list());
    }

    @Override
    public TranscityVO getByTranscityId(Long transcityId) {
        return transcityMapper.getByTranscityId(transcityId);
    }

    @Override
    public void save(Transcity transcity) {
        transcityMapper.save(transcity);
    }

    @Override
    public void update(Transcity transcity) {
        transcityMapper.update(transcity);
    }

    @Override
    public void deleteById(Long transcityId) {
        transcityMapper.deleteById(transcityId);
    }
}
