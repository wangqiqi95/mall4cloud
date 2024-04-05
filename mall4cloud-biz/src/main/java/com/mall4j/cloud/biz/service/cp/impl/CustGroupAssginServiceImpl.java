package com.mall4j.cloud.biz.service.cp.impl;

import com.mall4j.cloud.biz.mapper.cp.CustGroupAssginMapper;
import com.mall4j.cloud.biz.model.cp.CustGroupAssgin;
import com.mall4j.cloud.biz.service.cp.CustGroupAssginService;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 客群分配表 
 *
 * @author hwy
 * @date 2022-01-24 11:05:43
 */
@RequiredArgsConstructor
@Service
public class CustGroupAssginServiceImpl implements CustGroupAssginService {

    private CustGroupAssginMapper custGroupAssginMapper;

    @Override
    public PageVO<CustGroupAssgin> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> custGroupAssginMapper.list());
    }

    @Override
    public CustGroupAssgin getById(Long id) {
        return custGroupAssginMapper.getById(id);
    }

    @Override
    public void save(CustGroupAssgin custGroupAssgin) {
        custGroupAssginMapper.save(custGroupAssgin);
    }

    @Override
    public void update(CustGroupAssgin custGroupAssgin) {
        custGroupAssginMapper.update(custGroupAssgin);
    }

    @Override
    public void deleteById(Long id) {
        custGroupAssginMapper.deleteById(id);
    }
}
