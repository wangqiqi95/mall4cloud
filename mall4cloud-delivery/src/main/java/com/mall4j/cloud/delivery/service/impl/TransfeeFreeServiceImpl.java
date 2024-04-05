package com.mall4j.cloud.delivery.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.delivery.mapper.TransfeeFreeMapper;
import com.mall4j.cloud.delivery.service.TransfeeFreeService;
import com.mall4j.cloud.delivery.vo.TransfeeFreeVO;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 指定条件包邮项
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:01
 */
@Service
public class TransfeeFreeServiceImpl implements TransfeeFreeService {

    @Autowired
    private TransfeeFreeMapper transfeeFreeMapper;

    @Override
    public PageVO<TransfeeFreeVO> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> transfeeFreeMapper.list());
    }

    @Override
    public TransfeeFreeVO getByTransfeeFreeId(Long transfeeFreeId) {
        return transfeeFreeMapper.getByTransfeeFreeId(transfeeFreeId);
    }
}
