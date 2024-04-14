package com.mall4j.cloud.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.dto.TaskExecuteDetailInfoSearchParamDTO;
import com.mall4j.cloud.biz.model.TaskExecuteDetailInfo;
import com.mall4j.cloud.biz.vo.cp.taskInfo.ShoppingGuideTaskClientVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 任务调度详情信息
 */
public interface TaskExecuteDetailInfoMapper extends BaseMapper<TaskExecuteDetailInfo> {

}

