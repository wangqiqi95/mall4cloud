package com.mall4j.cloud.biz.service.cp.impl;

import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.biz.wx.cp.constant.OriginEumn;
import com.mall4j.cloud.biz.dto.cp.MaterialMsgDTO;
import com.mall4j.cloud.biz.dto.cp.TaskPushDTO;
import com.mall4j.cloud.biz.mapper.cp.TaskPushMapper;
import com.mall4j.cloud.biz.model.cp.MaterialMsg;
import com.mall4j.cloud.biz.model.cp.SendTask;
import com.mall4j.cloud.biz.model.cp.TaskPush;
import com.mall4j.cloud.biz.model.cp.TaskSendDetail;
import com.mall4j.cloud.biz.service.cp.MaterialMsgService;
import com.mall4j.cloud.biz.service.cp.TaskPushService;
import com.mall4j.cloud.biz.service.cp.TaskSendDetailService;
import com.mall4j.cloud.common.util.Json;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 任务表
 *
 * @author hwy
 * @date 2022-02-18 18:17:51
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class TaskPushServiceImpl implements TaskPushService {
    private final TaskPushMapper taskPushMapper;
    private final MaterialMsgService msgService;
    private final TaskSendDetailService taskSendDetailService;
    @Override
    public List<TaskPush> listPushTaskByTaskId(Long taskId) {
        return taskPushMapper.list(taskId);
    }

    @Override
    public TaskPush getById(Long id) {
        return taskPushMapper.getById(id);
    }

    @Override
    public void save(TaskPush taskPush) {
        taskPushMapper.save(taskPush);
    }

    @Override
    public void update(TaskPush taskPush) {
        taskPushMapper.update(taskPush);
    }

    @Override
    public void deleteById(Long id) {
        taskPushMapper.deleteById(id);
        taskSendDetailService.deleteByPushId(id);
        msgService.deleteByMatId(id, OriginEumn.GROUP_TASK);
    }

    @Override
    public void deleteByTaskId(Long taskId) {
        List<TaskPush>  list = listPushTaskByTaskId(taskId);
        list.forEach(taskPush -> {
            taskSendDetailService.deleteByPushId(taskPush.getId());
            msgService.deleteByMatId(taskPush.getId(), OriginEumn.GROUP_TASK);
        });
        taskPushMapper.deleteByTaskId(taskId);
    }

    @Override
    public void createBatchPush(SendTask sendTask, List<StaffVO> staffList, List<TaskPushDTO> pushList) {
         for(TaskPushDTO pushDTO:pushList){
             TaskPush taskPush = new TaskPush(pushDTO,sendTask);
             this.save(taskPush);
             MaterialMsgDTO msgDTO =  pushDTO.getMsgDTO();
             //添加附件
             MaterialMsg msg = new MaterialMsg(msgDTO, OriginEumn.GROUP_TASK);
             msg.setMatId(taskPush.getId());
             msg.setCreateBy(sendTask.getCreateBy());
             msg.setCreateTime(sendTask.getCreateTime());
             msg.setUpdateTime(sendTask.getUpdateTime());
             msgService.save(msg);
             //添加推送详情
             staffList.forEach(staff->taskSendDetailService.save(new TaskSendDetail(taskPush,staff)));
         }
    }

    @Override
    public void updateBatchPush(boolean isChange, SendTask sendTask, List<StaffVO> staffList, List<TaskPushDTO> pushList) {
        List<TaskPush> addChangeTaskPush = Lists.newArrayList();
        List<TaskPush> updateChangeTaskPush = Lists.newArrayList();
        List<TaskPush> deleteChangeTaskPush = Lists.newArrayList();
        //先看看推送列表有没变
        List<TaskPush> oldList = this.listPushTaskByTaskId(sendTask.getId());
        for (TaskPush push: oldList ){
            if(pushList.stream().filter(item->item.getId()!=null&&push.getId().longValue()==item.getId()).count()<=0){
                deleteChangeTaskPush.add(push);
            }
            pushList.stream().filter(item->item.getId()!=null&&push.getId().longValue()==item.getId()).findFirst().ifPresent(dto->{
                  push.setPushDTO(dto);
                  updateChangeTaskPush.add(push);
            });

        }
        pushList.forEach(item->{if(item.getId()==null){addChangeTaskPush.add(new TaskPush(item,sendTask));}});
        //对新增的进行新增，更新的更新，删除的删除
        addChangeTaskPush.forEach(this::createTaskPushAndMsg);
        updateChangeTaskPush.forEach(this::createTaskPushAndMsg);
        deleteChangeTaskPush.forEach(item->this.deleteById(item.getId()));
        log.info(isChange+"----"+ Json.toJsonString(addChangeTaskPush));
        //员工已改变，发送详情页要改
        if(isChange){
            for(TaskPush taskPush : updateChangeTaskPush){
                taskSendDetailService.deleteByPushId(taskPush.getId());
                staffList.forEach(staffVO -> taskSendDetailService.save(new TaskSendDetail(taskPush,staffVO)));
            }
        }
        for(TaskPush taskPush : addChangeTaskPush){
            staffList.forEach(staffVO -> taskSendDetailService.save(new TaskSendDetail(taskPush,staffVO)));
        }
    }


    private void createTaskPushAndMsg(TaskPush taskPush) {
        if(taskPush.getId()==null) {
            taskPushMapper.save(taskPush);
        }else{
            taskPushMapper.update(taskPush);
            msgService.deleteByMatId(taskPush.getId(), OriginEumn.GROUP_TASK);
        }
        MaterialMsg msg = new MaterialMsg(taskPush.getPushDTO().getMsgDTO(), OriginEumn.GROUP_TASK);
        msg.setMatId(taskPush.getId());
        msg.setCreateBy(taskPush.getCreateBy());
        msg.setCreateTime(taskPush.getCreateTime());
        msg.setUpdateTime(taskPush.getUpdateTime());
        msgService.save(msg);
    }

    public void updateTaskPushCompleteSend(Long  pushId) {
        taskPushMapper.updateTaskPushCompleteSend(pushId);
    }

}
