package com.mall4j.cloud.biz.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall4j.cloud.biz.dto.TaskManagementPageDTO;
import com.mall4j.cloud.biz.model.TaskManagement;
import com.mall4j.cloud.biz.vo.TaskManagementVO;
import org.apache.ibatis.annotations.Param;

public interface TaskManagementMapper{
    Page<TaskManagementVO> list(@Param("page") IPage page, @Param("param") TaskManagementPageDTO pageDTO);

    void modifyTaskStatus(Long id);

    TaskManagementVO getId(Long id);
//
    void addTaskManagement(TaskManagement taskManagement);

}
