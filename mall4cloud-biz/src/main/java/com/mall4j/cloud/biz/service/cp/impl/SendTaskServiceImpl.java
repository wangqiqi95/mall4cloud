package com.mall4j.cloud.biz.service.cp.impl;

import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.biz.wx.cp.constant.TaskType;
import com.mall4j.cloud.biz.dto.cp.SendTaskPageDTO;
import com.mall4j.cloud.biz.dto.cp.TaskPushDTO;
import com.mall4j.cloud.biz.mapper.cp.SendTaskMapper;
import com.mall4j.cloud.biz.model.cp.*;
import com.mall4j.cloud.biz.service.cp.*;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;

import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.util.Json;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 群发任务表
 *
 * @author hwy
 * @date 2022-02-18 18:17:52
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SendTaskServiceImpl implements SendTaskService {

    private final SendTaskMapper sendTaskMapper;
    private final CpTaskStaffRefService taskStaffRefService;
    private final TaskPushService pushService;
    private final TaskSendDetailService  taskSendDetailService;
    @Override
    public PageVO<SendTask> page(PageDTO pageDTO, SendTaskPageDTO request) {
        return PageUtil.doPage(pageDTO, () -> sendTaskMapper.list(request));
    }

    @Override
    public SendTask getById(Long id) {
        return sendTaskMapper.getById(id);
    }

    @Override
    public void save(SendTask sendTask) {
        sendTaskMapper.save(sendTask);
    }

    @Override
    public void update(SendTask sendTask) {
        sendTaskMapper.update(sendTask);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(Long id) {
        sendTaskMapper.deleteById(id);
        taskStaffRefService.deleteByTaskId(id, TaskType.TASK.getCode());
        pushService.deleteByTaskId(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void createOrUpdate(SendTask sendTask, List<StaffVO> staffList, List<TaskPushDTO> pushList,  boolean  isChange) {
        log.info(isChange+"===sendTask="+ Json.toJsonString(sendTask));
        boolean isNew = sendTask.getId()==null;
        //对任务进行操作
        if(isNew){
            this.save(sendTask);
        }else {
            this.update(sendTask);
        }
        //员工及标签选择有变化  并且不是新增，先删除原来的，再添加
        if(!isNew && isChange){
            taskStaffRefService.deleteByTaskId(sendTask.getId(), TaskType.TASK.getCode());
        }
        //添加员工关联
        if(isChange){
            staffList.forEach(staff -> {
                CpTaskStaffRef taskStaffRef = new CpTaskStaffRef();
                taskStaffRef.setStaffId(staff.getId());
                taskStaffRef.setUserId(staff.getQiWeiUserId());
                taskStaffRef.setStaffName(staff.getStaffName());
                taskStaffRef.setCreateTime(new Date());
                taskStaffRef.setTaskId(sendTask.getId());
                taskStaffRef.setType(TaskType.TASK.getCode());
                taskStaffRefService.save(taskStaffRef);
            });
        }
        //更新推送及推送详情/附近信息
        if(isNew) {
            pushService.createBatchPush(sendTask, staffList, pushList);
        }else{
            pushService.updateBatchPush(isChange,sendTask, staffList, pushList);
        }
    }

    @Override
    public void updateSendTaskCompleteSend(Long id) {
        sendTaskMapper.updateSendTaskCompleteSend(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void send(TaskSendDetail detail) {
        taskSendDetailService.send(detail);
        pushService.updateTaskPushCompleteSend(detail.getPushId());
        TaskPush taskPush =  pushService.getById(detail.getPushId());
        this.updateSendTaskCompleteSend(taskPush.getTaskId());

    }

}
