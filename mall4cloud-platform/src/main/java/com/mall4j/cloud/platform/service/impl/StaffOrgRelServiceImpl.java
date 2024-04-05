package com.mall4j.cloud.platform.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.api.platform.vo.StaffOrgVO;
import com.mall4j.cloud.platform.mapper.OrganizationMapper;
import com.mall4j.cloud.platform.mapper.StaffOrgRelMapper;
import com.mall4j.cloud.platform.model.Organization;
import com.mall4j.cloud.platform.model.StaffOrgRel;
import com.mall4j.cloud.platform.service.StaffOrgRelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 员工部门关联表
 *
 * @author gmq
 * @date 2023-10-27 10:44:14
 */
@Service
public class StaffOrgRelServiceImpl extends ServiceImpl<StaffOrgRelMapper, StaffOrgRel> implements StaffOrgRelService {

    @Autowired
    private StaffOrgRelMapper staffOrgRelMapper;
    @Autowired
    private OrganizationMapper organizationMapper;

    @Override
    public List<StaffOrgVO> getStaffAndOrgs(List<Long> staffIds) {
        if(CollUtil.isEmpty(staffIds)){
            return ListUtil.empty();
        }
        List<StaffOrgVO> staffOrgVOS=staffOrgRelMapper.selectStaffAndOrgs(staffIds);
        return CollUtil.isNotEmpty(staffOrgVOS)?staffOrgVOS:ListUtil.empty();
    }

    @Override
    public void deleteByStaffId(Long staffId) {
        staffOrgRelMapper.deleteByStaffId(staffId);
    }

    @Override
    public List<Long> getOrgAndChildByStaffIds(List<Long> staffIds) {
        List<StaffOrgVO> staffOrgVOS=staffOrgRelMapper.selectStaffAndOrgs(staffIds);
        if(CollUtil.isNotEmpty(staffOrgVOS)){
            List<Organization> orgIds=organizationMapper.getStoreOrgIdByPaths(staffOrgVOS.stream().map(item->item.getPath()).distinct().collect(Collectors.toList()));
            return orgIds.stream().map(item->item.getOrgId()).collect(Collectors.toList());
        }
        return null;
    }
}
