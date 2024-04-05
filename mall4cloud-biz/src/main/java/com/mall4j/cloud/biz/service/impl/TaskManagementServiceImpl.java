package com.mall4j.cloud.biz.service.impl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall4j.cloud.biz.dto.AddTaskManagementDTO;
import com.mall4j.cloud.biz.dto.TaskManagementPageDTO;
import com.mall4j.cloud.biz.mapper.TaskManagementMapper;
import com.mall4j.cloud.biz.mapper.TaskShoppingGuideMapper;
import com.mall4j.cloud.biz.model.TaskManagement;
import com.mall4j.cloud.biz.model.TaskShoppingGuide;
import com.mall4j.cloud.biz.service.TaskManagementService;
import com.mall4j.cloud.biz.vo.TaskManagementVO;
import com.mall4j.cloud.common.constant.TaskTypeEnum;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import lombok.AllArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


/**
 * 任务service
 * @author hlc
 * @date 2024/4/1 11:36
 */
@Service
@AllArgsConstructor
public class TaskManagementServiceImpl implements TaskManagementService {


    private final TaskManagementMapper taskManagementMapper;
    private final TaskShoppingGuideMapper taskShoppingGuideMapper;
    @Override
    public ServerResponseEntity<PageVO<TaskManagementVO>> queryPageTask(TaskManagementPageDTO pageDTO) {
        IPage<TaskManagementVO> page = new Page<>(pageDTO.getPageNum(), pageDTO.getPageSize());
        Page<TaskManagementVO> voPage = taskManagementMapper.list(page, pageDTO);
        PageVO<TaskManagementVO> pageVO = new PageVO<>();
        pageVO.setPages((int) voPage.getPages());
        pageVO.setList(voPage.getRecords());
        pageVO.setTotal(voPage.getTotal());
        return ServerResponseEntity.success(pageVO);
    }

    @Override
    public ServerResponseEntity update(Long id) {
        TaskManagementVO taskManagement = taskManagementMapper.getId(id);
        if (taskManagement != null) {
            taskManagementMapper.modifyTaskStatus(id);
            return ServerResponseEntity.success();
        }
        return ServerResponseEntity.showFailMsg("任务id不存在");
    }

    /**
     * 新增任务
     * @author 知章
     * @date 2024/4/2 14:22
     * @param addTaskManagementDTO
     */
    @Override
    @Transactional
    public ServerResponseEntity save(AddTaskManagementDTO addTaskManagementDTO) {

        // 任务主表插入记录
        TaskManagement taskManagement = taskManagementSave(addTaskManagementDTO);
        taskManagementMapper.addTaskManagement(taskManagement);

        // 插入任务导购关系表
        TaskShoppingGuide taskShoppingGuide = new TaskShoppingGuide();
        taskShoppingGuide.setTaskId(taskManagement.getId());
        taskShoppingGuide.setStoreId(taskShoppingGuide.getStoreId());



        return ServerResponseEntity.success();
    }

    private static TaskManagement taskManagementSave(AddTaskManagementDTO addTaskManagementDTO) {
        TaskManagement taskManagement = new TaskManagement();
        taskManagement.setTaskName(addTaskManagementDTO.getTaskName());
        taskManagement.setStoreSelectionType(addTaskManagementDTO.getStoreSelectionType());
        taskManagement.setUserRangeType(addTaskManagementDTO.getUserRangeType());
        taskManagement.setTaskType(addTaskManagementDTO.getTaskType());
        taskManagement.setAffiliatedCompanyType(addTaskManagementDTO.getAffiliatedCompanyType());
        taskManagement.setQuantityAllotted(addTaskManagementDTO.getQuantityAllotted());
        taskManagement.setExecutionMode(addTaskManagementDTO.getExecutionMode());
        taskManagement.setScriptContent(addTaskManagementDTO.getScriptContent());
        taskManagement.setTaskObjectiveRatio(addTaskManagementDTO.getTaskObjectiveRatio());
        taskManagement.setTaskStartTime(addTaskManagementDTO.getTaskStartTime());
        taskManagement.setTaskEndTime(addTaskManagementDTO.getTaskEndTime());
        taskManagement.setReturnVisitResult(addTaskManagementDTO.getReturnVisitResult());
        taskManagement.setCreateTime(new Date());

        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime taskStartTime = addTaskManagementDTO.getTaskStartTime()
                .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime taskEndTime = addTaskManagementDTO.getTaskEndTime()
                .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        TaskTypeEnum status;
        if (currentTime.isBefore(taskStartTime)) {
            status = TaskTypeEnum.HAVE_NOT_STARTED;
        } else if (currentTime.isAfter(taskEndTime)) {
            status = TaskTypeEnum.COUPON_ARRIVAL;
        } else {
            status = TaskTypeEnum.COUPON_EXPIRE;
        }
        taskManagement.setStatus(status.getValue());
        return taskManagement;
    }

}
