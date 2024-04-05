package com.mall4j.cloud.biz.service.cp.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.biz.dto.cp.CpAutoGroupCodeStaffSelectDTO;
import com.mall4j.cloud.biz.mapper.cp.CpAutoGroupCodeStaffMapper;
import com.mall4j.cloud.biz.model.cp.CpAutoGroupCodeStaff;
import com.mall4j.cloud.biz.service.cp.CpAutoGroupCodeStaffService;
import com.mall4j.cloud.biz.vo.cp.CpAutoGroupCodeStaffVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 *
 * @author gmq
 * @date 2023-10-27 16:59:44
 */
@Service
public class CpAutoGroupCodeStaffServiceImpl extends ServiceImpl<CpAutoGroupCodeStaffMapper, CpAutoGroupCodeStaff> implements CpAutoGroupCodeStaffService {

    @Autowired
    private CpAutoGroupCodeStaffMapper cpAutoGroupCodeStaffMapper;

    @Override
    public PageVO<CpAutoGroupCodeStaffVO> page(PageDTO pageDTO,CpAutoGroupCodeStaffSelectDTO dto) {
        return PageUtil.doPage(pageDTO, () -> cpAutoGroupCodeStaffMapper.list(dto));
    }

    @Override
    public CpAutoGroupCodeStaff getById(Long id) {
        return cpAutoGroupCodeStaffMapper.getById(id);
    }

    @Override
    public boolean save(CpAutoGroupCodeStaff cpAutoGroupCodeStaff) {
        return this.save(cpAutoGroupCodeStaff);
    }

    @Override
    public void update(CpAutoGroupCodeStaff cpAutoGroupCodeStaff) {
        cpAutoGroupCodeStaffMapper.update(cpAutoGroupCodeStaff);
    }

    @Override
    public void deleteById(Long id) {
        cpAutoGroupCodeStaffMapper.deleteById(id);
    }
}
