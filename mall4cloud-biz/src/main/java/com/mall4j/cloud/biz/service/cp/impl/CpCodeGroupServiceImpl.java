package com.mall4j.cloud.biz.service.cp.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.biz.mapper.cp.CpAutoGroupCodeMapper;
import com.mall4j.cloud.biz.mapper.cp.CpCodeGroupMapper;
import com.mall4j.cloud.biz.mapper.cp.CpStaffCodePlusMapper;
import com.mall4j.cloud.biz.mapper.cp.GroupCodeMapper;
import com.mall4j.cloud.biz.model.cp.CpAutoGroupCode;
import com.mall4j.cloud.biz.model.cp.CpCodeGroup;
import com.mall4j.cloud.biz.model.cp.CpGroupCode;
import com.mall4j.cloud.biz.model.cp.CpStaffCodePlus;
import com.mall4j.cloud.biz.service.cp.CpCodeGroupService;
import com.mall4j.cloud.biz.vo.cp.CpCodeGroupVO;
import com.mall4j.cloud.common.exception.LuckException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 企微活码分组
 *
 * @author gmq
 * @date 2023-10-23 14:03:45
 */
@Service
public class CpCodeGroupServiceImpl extends ServiceImpl<CpCodeGroupMapper, CpCodeGroup> implements CpCodeGroupService {

    @Autowired
    private CpCodeGroupMapper cpCodeGroupMapper;
    @Autowired
    private CpStaffCodePlusMapper cpStaffCodeMapper;
    @Autowired
    private GroupCodeMapper groupCodeMapper;
    @Autowired
    private CpAutoGroupCodeMapper autoGroupCodeMapper;

    @Override
    public List<CpCodeGroupVO> list(Integer type,String name) {
        return cpCodeGroupMapper.list(type,name);
    }

    @Override
    public CpCodeGroup getById(Long id) {
        return cpCodeGroupMapper.getById(id);
    }

    @Override
    public boolean saveTo(CpCodeGroup cpCodeGroup) {
        return this.save(cpCodeGroup);
    }

    @Override
    public void update(CpCodeGroup cpCodeGroup) {
        cpCodeGroupMapper.update(cpCodeGroup);
    }

    @Override
    public void deleteById(Long id) {
        //校验是否使用中
        if(cpStaffCodeMapper.selectCount(new LambdaQueryWrapper<CpStaffCodePlus>().eq(CpStaffCodePlus::getGroupId,id).eq(CpStaffCodePlus::getFlag,0))>0){
            throw new LuckException("当前分组存在使用中的渠道活码，请移除后再操作");
        }
        if(groupCodeMapper.selectCount(new LambdaQueryWrapper<CpGroupCode>().eq(CpGroupCode::getGroupId,id).eq(CpGroupCode::getFlag,0))>0){
            throw new LuckException("当前分组存在使用中的群活码，请移除后再操作");
        }
        if(autoGroupCodeMapper.selectCount(new LambdaQueryWrapper<CpAutoGroupCode>().eq(CpAutoGroupCode::getGroupId,id).eq(CpAutoGroupCode::getFlag,0))>0){
            throw new LuckException("当前分组存在使用中的自动拉群活码，请移除后再操作");
        }
        cpCodeGroupMapper.deleteById(id);
    }
}
