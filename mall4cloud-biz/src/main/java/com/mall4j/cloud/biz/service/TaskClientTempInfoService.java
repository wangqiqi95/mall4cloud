package com.mall4j.cloud.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.dto.TaskClientInfoDTO;
import com.mall4j.cloud.biz.model.TaskClientTempInfo;
import com.mall4j.cloud.biz.vo.TaskClientTempInfoVO;

import java.util.List;

/**
 * 任务客户临时表
 */
public interface TaskClientTempInfoService extends IService<TaskClientTempInfo> {
    /**
     * 批量插入任务客户
     * @param taskClientInfoList 任务客户信息
     * @param uuid 任务临时id
     */
    void batchInsert(List<TaskClientInfoDTO> taskClientInfoList, String uuid);

    /**
     * 获取导入的客户
     * @param uuid 任务临时id
     */
    List<TaskClientTempInfoVO> listTaskClientTempInfoByUuid(String uuid);
}

