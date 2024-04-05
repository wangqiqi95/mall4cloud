package com.mall4j.cloud.delivery.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.delivery.model.TranscityFree;
import com.mall4j.cloud.delivery.mapper.TranscityFreeMapper;
import com.mall4j.cloud.delivery.service.TranscityFreeService;
import com.mall4j.cloud.delivery.vo.TranscityFreeVO;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 指定条件包邮城市项
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:01
 */
@Service
public class TranscityFreeServiceImpl implements TranscityFreeService {

    @Autowired
    private TranscityFreeMapper transcityFreeMapper;

    @Override
    public PageVO<TranscityFreeVO> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> transcityFreeMapper.list());
    }

    @Override
    public TranscityFreeVO getByTranscityFreeId(Long transcityFreeId) {
        return transcityFreeMapper.getByTranscityFreeId(transcityFreeId);
    }

    @Override
    public void save(TranscityFree transcityFree) {
        transcityFreeMapper.save(transcityFree);
    }

    @Override
    public void update(TranscityFree transcityFree) {
        transcityFreeMapper.update(transcityFree);
    }

    @Override
    public void deleteById(Long transcityFreeId) {
        transcityFreeMapper.deleteById(transcityFreeId);
    }
}
