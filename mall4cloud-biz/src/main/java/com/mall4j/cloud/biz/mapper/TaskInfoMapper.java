package com.mall4j.cloud.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.dto.TaskInfoSearchParamDTO;
import com.mall4j.cloud.biz.model.TaskInfo;
import com.mall4j.cloud.biz.vo.cp.taskInfo.TaskInfoPageVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 任务信息表
 */
public interface TaskInfoMapper extends BaseMapper<TaskInfo> {


    /**
     * 获取任务信息
     * @param taskInfoSearchParamDTO 任务查询条件
     */
    List<TaskInfoPageVO> list(@Param("et") TaskInfoSearchParamDTO taskInfoSearchParamDTO);
}

