package com.mall4j.cloud.user.task;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall4j.cloud.api.delivery.vo.AreaVO;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.user.bo.GroupPushTaskCpRelationBO;
import com.mall4j.cloud.user.constant.FriendStatusTypeEnum;
import com.mall4j.cloud.user.constant.GroupPushTaskOperateStatusEnum;
import com.mall4j.cloud.user.manager.GroupPushSonTaskManager;
import com.mall4j.cloud.user.mapper.GroupPushSonTaskMapper;
import com.mall4j.cloud.user.model.GroupPushSonTask;
import com.mall4j.cloud.user.model.StaffBatchSendCpMsg;
import com.mall4j.cloud.user.service.GroupPushSonTaskService;
import com.mall4j.cloud.user.service.GroupPushTaskService;
import com.mall4j.cloud.user.service.GroupPushTaskVipRelationService;
import com.mall4j.cloud.user.service.StaffBatchSendCpMsgService;
import com.mall4j.cloud.user.service.UserStaffCpRelationService;
import com.mall4j.cloud.user.vo.GroupPushTaskVipRelationByFriendStateVO;
import com.mall4j.cloud.user.vo.StaffBatchSendCpMsgVO;
import com.xxl.job.core.handler.annotation.XxlJob;
import java.util.Date;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.util.CollectionUtils;

/**
 * @author cl
 * @date 2021-07-22 16:38:42
 */
@Component
public class GroupPushTask {

    private static final Logger log = LoggerFactory.getLogger(GroupPushTask.class);

//    @Autowired
//    private OnsMQTemplate endGroupPushTemplate;
    @Autowired
    private GroupPushSonTaskService groupPushSonTaskService;
    @Autowired
    private StaffBatchSendCpMsgService staffBatchSendCpMsgService;
    @Autowired
    private GroupPushTaskService groupPushTaskService;
    @Resource
    private GroupPushSonTaskMapper groupPushSonTaskMapper;
    /**
     * 获取企业微信群发任务结果
     */
    @XxlJob("syncTaskResult")
    public void syncGroupMessageSendResult(){
        log.info("==============定时获取企业微信群发任务结果进行同步开始===================");
        // 获取企业微信群发任务结果进行同步
        groupPushSonTaskService.syncGroupMessageSendResult();
        log.info("==============定时获取企业微信群发任务结果进行同步结束===================");
    }

    /**
     * 将群发任务中导购与会员会员关系不存在的数据重新判断会员关系,进行同步
     */
    @XxlJob("pushTask")
    public void pushTask() {
        log.info("发送群发任务开始");
        // 获取待发送的子任务
        Date now = new Date();
        LambdaQueryWrapper<GroupPushSonTask> lambdaQueryWrapper=new LambdaQueryWrapper<GroupPushSonTask>();
        lambdaQueryWrapper.eq(GroupPushSonTask::getDeleteFlag, 0);
        lambdaQueryWrapper.le(GroupPushSonTask::getStartTime, now);
        lambdaQueryWrapper.ge(GroupPushSonTask::getEndTime, now);
        lambdaQueryWrapper.or().eq(GroupPushSonTask::getTaskType,1);//CDP任务无时间范围限制
        List<GroupPushSonTask> sonTaskList = groupPushSonTaskMapper.selectList(lambdaQueryWrapper);
        if (CollectionUtils.isEmpty(sonTaskList)) {
            log.info("暂无待发送子任务");
            return;
        }
        List<Long> sonTaskIdList = sonTaskList.stream().map(GroupPushSonTask::getGroupPushSonTaskId).collect(Collectors.toList());
        Map<Long, GroupPushSonTask> sonTaskMap = sonTaskList.stream()
            .collect(Collectors.toMap(GroupPushSonTask::getGroupPushSonTaskId, v -> v));

        // 主任务
        List<Long> taskIdList = sonTaskList.stream().map(GroupPushSonTask::getGroupPushTaskId).collect(Collectors.toList());
        List<com.mall4j.cloud.user.model.GroupPushTask> taskList = groupPushTaskService
            .list(new LambdaQueryWrapper<com.mall4j.cloud.user.model.GroupPushTask>()
                .in(com.mall4j.cloud.user.model.GroupPushTask::getGroupPushTaskId, taskIdList));
        Map<Long, com.mall4j.cloud.user.model.GroupPushTask> taskMap = taskList.stream()
            .collect(Collectors.toMap(com.mall4j.cloud.user.model.GroupPushTask::getGroupPushTaskId, v -> v));

        // 获取子任务下待发送的销售记录
        List<StaffBatchSendCpMsg> staffBatchSendCpMsgList = staffBatchSendCpMsgService.list(
            new LambdaQueryWrapper<StaffBatchSendCpMsg>()
                    .in(StaffBatchSendCpMsg::getPushSonTaskId, sonTaskIdList)
                    .in(StaffBatchSendCpMsg::getSendStatus, 0));
//                .isNull(StaffBatchSendCpMsg::getMsgId));
        for (StaffBatchSendCpMsg staffBatchSendCpMsg : staffBatchSendCpMsgList) {
            com.mall4j.cloud.user.model.GroupPushTask mainTask = taskMap.get(staffBatchSendCpMsg.getPushTaskId());
            //校验主任务是否正常
            if(mainTask.getOperateStatus()!= GroupPushTaskOperateStatusEnum.SUCCESS.getOperateStatus()){
                log.info("任务执行失败，主任务未启用：{}", JSON.toJSONString(mainTask));
                continue;
            }
            GroupPushSonTask sonTask = sonTaskMap.get(staffBatchSendCpMsg.getPushSonTaskId());
            log.info("正在发送子任务 id = {} 销售id = {}", staffBatchSendCpMsg.getPushSonTaskId(), staffBatchSendCpMsg.getStaffId());
            groupPushSonTaskService.pushTask(staffBatchSendCpMsg, sonTask, mainTask);
        }
        log.info("发送群发任务结束");
    }

  /*  *//**
     * 将群发任务中导购与会员会员关系不存在的数据重新判断会员关系,进行同步
     *//*
    @XxlJob("groupPushTaskUserRelationSyncV2")
    public void groupPushTaskUserRelationSyncV2() {
        log.info("将群发任务中导购与会员会员关系不存在的数据重新判断会员关系,进行同步》》》》》》》》》》》》》》》》》》》》》");

        // 查询出所有未结束的群发任务信息
        List<Long> finishedGroupPushTaskList = groupPushSonTaskManager.getFinishedGroupPushTaskList();
        if(CollectionUtil.isEmpty(finishedGroupPushTaskList)){
            return;
        }

        int syncUserCount = groupPushTaskVipRelationService.updateBatchTaskVipRelationFriendStateByTaskIds(finishedGroupPushTaskList);

        log.info("执行群发任务同步导购与会员关系，当前任务修改数据量为：{}", syncUserCount);

        log.info("群发任务中导购与会员会员关系不存在的数据已同步》》》》》》》》》》》》》》》》》》》》》");
    }

    *//**
     * 将群发任务中结束时间比当前时间还早的任务且状态还是在进行中的任务筛选出来，调用企业微信接口对群发任务进行停止
     *//*
    @XxlJob("expiredGroupPushTaskStop")
    public void expiredGroupPushTaskStop() {
        log.info("==============将群发任务中结束时间比当前时间还早的任务且状态还是在进行中的任务筛选出来，调用企业微信接口对群发任务进行停止——开始==============");

        //查询需要删除的子任务
        List<Long> removeList = groupPushSonTaskManager.getExpiredGroupPushTaskList();

        //当存在需要删除的子任务
        if (CollectionUtil.isNotEmpty(removeList)){
            *//**
             * 为避免出现子任务在平台端显示异常问题，只做企业微信群发任务暂停操作
             *//*
            // 批量删除子任务
            // groupPushSonTaskManager.removeBySonTaskIdList(removeList);
            //发送异步消息，删除的子任务相关的企微推送任务需要取消
//            endGroupPushTemplate.syncSend(removeList);
        }

        log.info("==============将群发任务中结束时间比当前时间还早的任务且状态还是在进行中的任务筛选出来，调用企业微信接口对群发任务进行停止——结束==============");
    }*/

}
