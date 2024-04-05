package com.mall4j.cloud.platform.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.platform.mapper.TzTagMapper;
import com.mall4j.cloud.platform.model.TzTag;
import com.mall4j.cloud.platform.model.TzTagStaff;
import com.mall4j.cloud.platform.mapper.TzTagStaffMapper;
import com.mall4j.cloud.platform.service.TzTagStaffService;
import com.mall4j.cloud.platform.vo.TzTagStaffVO;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 标签关联员工
 *
 * @author gmq
 * @date 2022-09-13 12:01:45
 */
@Service
public class TzTagStaffServiceImpl extends ServiceImpl<TzTagStaffMapper, TzTagStaff> implements TzTagStaffService {

    @Autowired
    private TzTagStaffMapper tzTagStaffMapper;

    @Override
    public PageVO<TzTagStaffVO> pageStaff(PageDTO pageDTO,Long tagId) {
        PageVO<TzTagStaffVO> pageVO=PageUtil.doPage(pageDTO, () -> tzTagStaffMapper.listByTagId(tagId));
        return pageVO;
    }

    @Override
    public List<Long> listByTagId(Long tagId) {
        List<TzTagStaff> tzTagStaffList=this.list(new LambdaQueryWrapper<TzTagStaff>()
                .eq(TzTagStaff::getTagId,tagId)
                .eq(TzTagStaff::getDelFlag,0)
                .groupBy(TzTagStaff::getStaffId));
        if(CollectionUtil.isNotEmpty(tzTagStaffList)){
            List<Long> ids = tzTagStaffList.stream().map(TzTagStaff::getStaffId).collect(Collectors.toList());
            return ids;
        }
        return null;
    }

    @Override
    public void deleteById(Long id) {
        tzTagStaffMapper.deleteById(id);
    }

    @Override
    public void deleteByTagId(Long tagId) {
        this.update(new LambdaUpdateWrapper<TzTagStaff>().set(TzTagStaff::getDelFlag,1).eq(TzTagStaff::getTagId,tagId));
//        tzTagStaffMapper.delete(new LambdaQueryWrapper<TzTagStaff>().eq(TzTagStaff::getTagId,tagId));
    }
}
