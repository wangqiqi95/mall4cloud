package com.mall4j.cloud.delivery.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.delivery.model.Transfee;
import com.mall4j.cloud.delivery.mapper.TransfeeMapper;
import com.mall4j.cloud.delivery.service.TransfeeService;
import com.mall4j.cloud.delivery.vo.TransfeeVO;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 运费项
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:01
 */
@Service
public class TransfeeServiceImpl implements TransfeeService {

    @Autowired
    private TransfeeMapper transfeeMapper;

    @Override
    public PageVO<TransfeeVO> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> transfeeMapper.list());
    }

    @Override
    public TransfeeVO getByTransfeeId(Long transfeeId) {
        return transfeeMapper.getByTransfeeId(transfeeId);
    }

    @Override
    public void save(Transfee transfee) {
        transfeeMapper.save(transfee);
    }

    @Override
    public void update(Transfee transfee) {
        transfeeMapper.update(transfee);
    }

    @Override
    public void deleteById(Long transfeeId) {
        transfeeMapper.deleteById(transfeeId);
    }
}
