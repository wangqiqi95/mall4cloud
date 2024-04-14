package com.mall4j.cloud.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.bo.TaskAllocateBO;
import com.mall4j.cloud.biz.dto.TaskExecuteDetailInfoSearchParamDTO;
import com.mall4j.cloud.biz.model.TaskClientInfo;
import com.mall4j.cloud.biz.model.TaskExecuteDetailInfo;
import com.mall4j.cloud.biz.vo.cp.taskInfo.ShoppingGuideTaskClientGroupVO;
import com.mall4j.cloud.biz.vo.cp.taskInfo.ShoppingGuideTaskClientVO;

import java.util.List;

/**
 * 任务调度详情信息
 */
public interface TaskExecuteDetailInfoService extends IService<TaskExecuteDetailInfo> {
    List<TaskExecuteDetailInfo> buildTaskExecuteDetailInfoList(TaskAllocateBO taskAllocateBO);

    /**
     * 完成任务调度详情
     * @param id 任务调度详情id
     */
    void endTaskExecuteDetailInfo(Long id);

    /**
     * 更新加企微好友状态
     * @param id 任务调度详情id
     * @param addStatus 添加状态
     */
    void updateTaskExecuteDetailAddStatus(Long id, Integer addStatus);


    /**
     * 获取客户信息
     * @param taskExecuteDetailInfoSearchParamDTO 查询条件
     */
    List<ShoppingGuideTaskClientVO> listTaskClientInfo(TaskExecuteDetailInfoSearchParamDTO taskExecuteDetailInfoSearchParamDTO);

    /**
     * 获取客户群信息
     * @param taskExecuteDetailInfoSearchParamDTO 查询条件
     */
    List<ShoppingGuideTaskClientGroupVO> listTaskClientGroupInfo(TaskExecuteDetailInfoSearchParamDTO taskExecuteDetailInfoSearchParamDTO);
}

