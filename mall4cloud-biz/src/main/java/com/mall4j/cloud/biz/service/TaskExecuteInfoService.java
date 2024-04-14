package com.mall4j.cloud.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.dto.*;
import com.mall4j.cloud.biz.model.TaskExecuteInfo;
import com.mall4j.cloud.biz.model.TaskInfo;
import com.mall4j.cloud.biz.vo.cp.taskInfo.*;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

import java.util.List;

/**
 * 任务调度信息
 */
public interface TaskExecuteInfoService extends IService<TaskExecuteInfo> {
    /**
     * 生成未启动任务的导购任务
     * 注意：请确保定时任务的频率为半小时执行一次
     */
    void generateNotStartTaskExecuteInfo();

    /**
     * 生成进行中的任务信息
     * 注意：请确保每天执行一次
     */
    void generateProcessTaskExecuteInfo();

    /**
     * 获取导购对应的任务信息
     * @param pageDTO 分页对象
     * @param taskExecuteInfoSearchParamDTO 查询条件
     */
    PageVO<TaskExecuteInfoVO> page(PageDTO pageDTO, TaskExecuteInfoSearchParamDTO taskExecuteInfoSearchParamDTO);

    /**
     * 获取任务详情
     * @param executeId 任务调度id
     */
    TaskExecuteDetailInfoVO getTaskExecuteDetailInfo(Long executeId);

    /**
     * 获取任务对应的调度信息
     * @param pageDTO 分页对象
     * @param taskExecuteInfoSearchParamDTO 查询条件
     */
    PageVO<TaskExecutePageInfoVO> taskExecutePage(PageDTO pageDTO, TaskExecuteInfoSearchParamDTO taskExecuteInfoSearchParamDTO);


    /**
     * 获取任务调度信息
     * @param executeId 调度id
     */
    TaskExecuteInfo getTaskExecuteInfo(Long executeId);

}

