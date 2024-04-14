package com.mall4j.cloud.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.dto.TaskExecuteInfoSearchParamDTO;
import com.mall4j.cloud.biz.model.TaskExecuteInfo;
import com.mall4j.cloud.biz.vo.cp.taskInfo.TaskExecuteInfoVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 任务调度信息
 */
public interface TaskExecuteInfoMapper extends BaseMapper<TaskExecuteInfo> {


    List<TaskExecuteInfoVO> list(@Param("et") TaskExecuteInfoSearchParamDTO taskExecuteInfoSearchParamDTO);
}

