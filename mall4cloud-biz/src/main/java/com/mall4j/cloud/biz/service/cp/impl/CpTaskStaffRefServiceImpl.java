package com.mall4j.cloud.biz.service.cp.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.biz.mapper.cp.TaskStaffRefMapper;
import com.mall4j.cloud.biz.model.cp.CpTaskStaffRef;
import com.mall4j.cloud.biz.service.cp.CpTaskStaffRefService;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.security.AuthUserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 群发任务人工关联表
 *
 * @author hwy
 * @date 2022-02-18 18:17:52
 */
@RequiredArgsConstructor
@Service
public class CpTaskStaffRefServiceImpl extends ServiceImpl<TaskStaffRefMapper, CpTaskStaffRef> implements CpTaskStaffRefService {

    private final TaskStaffRefMapper taskStaffRefMapper;

    @Override
    public PageVO<CpTaskStaffRef> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> taskStaffRefMapper.list());
    }

    @Override
    public CpTaskStaffRef getById(Long id) {
        return taskStaffRefMapper.getById(id);
    }

    @Override
    public void deleteById(Long id) {
        taskStaffRefMapper.deleteById(id);
    }

    @Override
    public void deleteByTaskId(Long taskId, int type) {
//        taskStaffRefMapper.deleteByTaskId(taskId,type);
        this.update(new LambdaUpdateWrapper<CpTaskStaffRef>()
                .eq(CpTaskStaffRef::getTaskId,taskId)
                .eq(CpTaskStaffRef::getType,type)
                .set(CpTaskStaffRef::getIsDelete,1)
                .set(CpTaskStaffRef::getUpdateBy, AuthUserContext.get().getUsername())
                .set(CpTaskStaffRef::getUpdateTime,new Date()));
    }
    @Override
    public List<CpTaskStaffRef> listByTaskId(Long taskId, Integer type) {
        return taskStaffRefMapper.listByTaskId(taskId,type);
    }
}
