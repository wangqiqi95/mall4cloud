package com.mall4j.cloud.biz.service.cp.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.biz.dto.cp.CpPhoneTaskStaffDTO;
import com.mall4j.cloud.biz.dto.cp.SelectCpPhoneTaskRelDTO;
import com.mall4j.cloud.biz.mapper.cp.CpPhoneTaskStaffMapper;
import com.mall4j.cloud.biz.model.cp.CpPhoneTaskStaff;
import com.mall4j.cloud.biz.service.cp.CpPhoneTaskStaffService;
import com.mall4j.cloud.biz.vo.cp.CpPhoneTaskStaffVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.LambdaUtils;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 引流手机号任务关联员工
 *
 * @author gmq
 * @date 2023-10-30 17:13:37
 */
@Service
public class CpPhoneTaskStaffServiceImpl extends ServiceImpl<CpPhoneTaskStaffMapper, CpPhoneTaskStaff> implements CpPhoneTaskStaffService {

    @Autowired
    private CpPhoneTaskStaffMapper cpPhoneTaskRelMapper;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private StaffFeignClient staffFeignClient;

    @Override
    public PageVO<CpPhoneTaskStaffVO> page(PageDTO pageDTO, SelectCpPhoneTaskRelDTO dto) {
        PageVO<CpPhoneTaskStaffVO> pageVO=PageUtil.doPage(pageDTO, () -> cpPhoneTaskRelMapper.list(dto));

        //获取员工信息
        List<Long> staffIds=pageVO.getList().stream().map(item->item.getStaffId()).collect(Collectors.toList());
        if(CollUtil.isNotEmpty(staffIds)){
            ServerResponseEntity<List<StaffVO>> responseEntity= staffFeignClient.getStaffByIds(staffIds);
            ServerResponseEntity.checkResponse(responseEntity);

            Map<Long,StaffVO> staffVOMap= LambdaUtils.toMap(responseEntity.getData(),StaffVO::getId);

            for (CpPhoneTaskStaffVO cpPhoneTaskRelVO : pageVO.getList()) {
                if(staffVOMap.containsKey(cpPhoneTaskRelVO.getStaffId())){
                    cpPhoneTaskRelVO.setStaffName(staffVOMap.get(cpPhoneTaskRelVO.getStaffId()).getStaffName());
                }
            }
        }

        return pageVO;
    }

    @Override
    public CpPhoneTaskStaff getById(Long id) {
        return cpPhoneTaskRelMapper.getById(id);
    }

    @Override
    public void deleteByTaskId(Long taskId) {
        this.update(new LambdaUpdateWrapper<CpPhoneTaskStaff>()
                .set(CpPhoneTaskStaff::getIsDelete,1)
                .set(CpPhoneTaskStaff::getUpdateBy, AuthUserContext.get().getUsername())
                .set(CpPhoneTaskStaff::getUpdateTime,new Date())
                .eq(CpPhoneTaskStaff::getTaskId,taskId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveStaffs(Long taskId, List<CpPhoneTaskStaffDTO> staffs) {

        //先删除
        this.deleteByTaskId(taskId);

        List<CpPhoneTaskStaff> taskRels=mapperFacade.mapAsList(staffs, CpPhoneTaskStaff.class);
        for (CpPhoneTaskStaff taskRel : taskRels) {
            taskRel.setTaskId(taskId);
            taskRel.setIsDelete(0);
            taskRel.setCreateBy(AuthUserContext.get().getUsername());
            taskRel.setCreateTime(new Date());
        }
        this.saveBatch(taskRels);
    }
}
