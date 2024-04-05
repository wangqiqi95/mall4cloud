package com.mall4j.cloud.biz.service.cp.impl;

import com.mall4j.cloud.biz.mapper.cp.CustAssginDetailMapper;
import com.mall4j.cloud.biz.model.cp.CpCustAssginDetail;
import com.mall4j.cloud.biz.service.cp.CustAssginDetailService;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 离职客户分配表
 *
 * @author hwy
 * @date 2022-01-24 11:05:43
 */
@RequiredArgsConstructor
@Service
public class CustAssginDetailServiceImpl implements CustAssginDetailService {

    private CustAssginDetailMapper custAssginDetailMapper;

    @Override
    public PageVO<CpCustAssginDetail> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> custAssginDetailMapper.list());
    }

    @Override
    public CpCustAssginDetail getById(Long id) {
        return custAssginDetailMapper.getById(id);
    }

    @Override
    public void save(CpCustAssginDetail custAssginDetail) {
        custAssginDetailMapper.save(custAssginDetail);
    }

    @Override
    public void update(CpCustAssginDetail custAssginDetail) {
        custAssginDetailMapper.update(custAssginDetail);
    }

    @Override
    public void deleteById(Long id) {
        custAssginDetailMapper.deleteById(id);
    }
}
