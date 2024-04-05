package com.mall4j.cloud.biz.service.cp.impl;

import cn.hutool.core.date.DateUtil;
import com.mall4j.cloud.biz.wx.cp.constant.SendStatus;
import com.mall4j.cloud.biz.dto.cp.TaskSendDetailDTO;
import com.mall4j.cloud.biz.dto.cp.wx.NotifyDto;
import com.mall4j.cloud.biz.mapper.cp.TaskSendDetailMapper;
import com.mall4j.cloud.biz.model.cp.TaskSendDetail;
import com.mall4j.cloud.biz.service.cp.GroupCreateTaskService;
import com.mall4j.cloud.biz.service.cp.TaskSendDetailService;
import com.mall4j.cloud.biz.service.cp.WxCpPushService;
import com.mall4j.cloud.biz.vo.cp.TaskAttachmentVO;
import com.mall4j.cloud.biz.vo.cp.TaskSendDetailCountVO;
import com.mall4j.cloud.biz.vo.cp.TaskSendDetailVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;

import com.mall4j.cloud.common.database.vo.PageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 群发任务明细表
 *
 * @author hwy
 * @date 2022-02-18 18:17:52
 */
@RequiredArgsConstructor
@Service
public class TaskSendDetailServiceImpl implements TaskSendDetailService {

    private final TaskSendDetailMapper taskSendDetailMapper;
    private final GroupCreateTaskService groupCreateTaskService;
    private final WxCpPushService wxCpPushService;


    @Override
    public PageVO<TaskSendDetailVO> page(PageDTO pageDTO, TaskSendDetailDTO request) {
        return PageUtil.doPage(pageDTO, () -> taskSendDetailMapper.list(request));
    }

    @Override
    public TaskSendDetail getById(Long id) {
        return taskSendDetailMapper.getById(id);
    }

    @Override
    public void save(TaskSendDetail taskSendDetail) {
        taskSendDetailMapper.save(taskSendDetail);
    }

    @Override
    public void update(TaskSendDetail taskSendDetail) {
        taskSendDetailMapper.update(taskSendDetail);
    }

    @Override
    public void deleteById(Long id) {
        taskSendDetailMapper.deleteById(id);
    }

    @Override
    public void deleteByPushId(Long taskId) {
        taskSendDetailMapper.deleteByPushId(taskId);
    }

    @Override
    public List<TaskAttachmentVO> queryTaskList(Long staffId, String userId, Integer status) {
        List<TaskAttachmentVO> result = new ArrayList<>();
        List<TaskAttachmentVO> taskList = taskSendDetailMapper.queryTaskList(null,staffId,userId,status);
        List<TaskAttachmentVO> groupTasks = groupCreateTaskService.queryTaskList(null,staffId,userId,status);
        if(!CollectionUtils.isEmpty(taskList)){
            result.addAll(taskList);
        }
        if(!CollectionUtils.isEmpty(groupTasks)){
            result.addAll(groupTasks);
        }
        return result;
    }
    /**
     * 提供给望伟的小程序端，点去执行进行闭环
     * @param id 任务详情id  对应TaskAttachmentVO的id
     * @param type 闭环哪里任务 0 对应 TaskAttachmentVO的type是groupCode  1对应TaskAttachmentVO的type是非groupCode
     */
    @Override
    public void completeTask(Long id,int type ) {
        if(1==type) {
            TaskSendDetail taskSendDetail = new TaskSendDetail();
            taskSendDetail.setId(id);
            taskSendDetail.setIsRelay(1);
            taskSendDetail.setCompleteTime(new Date());
            taskSendDetailMapper.update(taskSendDetail);
        }else{
            groupCreateTaskService.complete(id);
        }
    }

    /**
     * 提供给望伟的小程序端，详情
     * @param id 任务详情id  对应TaskAttachmentVO的id
     * @param type 闭环哪里任务 0 对应 TaskAttachmentVO的type是groupCode  1对应TaskAttachmentVO的type是非groupCode
     */
    @Override
    public TaskAttachmentVO getTaskDetail(Long id, int type) {
        if(1==type) {
            List<TaskAttachmentVO> taskList = taskSendDetailMapper.queryTaskList(id,null,null,null);
            if(!CollectionUtils.isEmpty(taskList)){
                return taskList.get(0);
            }
        }else{
            List<TaskAttachmentVO> groupTasks = groupCreateTaskService.queryTaskList(id,null,null,null);
            if(!CollectionUtils.isEmpty(groupTasks)){
                return groupTasks.get(0);
            }
        }
        return null;
    }

    @Override
    public TaskSendDetailCountVO count(Long pushId) {
        return taskSendDetailMapper.count(pushId);
    }



    @Override
    public List<TaskSendDetail> listSendTask() {
        return taskSendDetailMapper.listSendTask();
    }

    @Override
    public void send(TaskSendDetail detail) {
        NotifyDto notifyDto = new NotifyDto();
        notifyDto.setTitle("新分享任务提醒");
        notifyDto.setContent("你有新的分享任务");
        notifyDto.setUrl("packageGuideShop/pages/home/todo-detail/todo-detail?type=1&id="+detail.getId());
        notifyDto.setStaffUserId(detail.getUserId());
        notifyDto.setPushDate(DateUtil.format(new Date(),"yyyy年MM月dd日"));
        notifyDto.setBtnText("点击查看详情");
        notifyDto.setEmphasisFirstItem(false);
        boolean result = wxCpPushService.createMiniprogramPush(notifyDto);
        detail.setSendStatus(result?SendStatus.SUCCESS.getCode():SendStatus.FAIL.getCode());
        detail.setUpdateTime(new Date());
        this.update(detail);
    }
}
