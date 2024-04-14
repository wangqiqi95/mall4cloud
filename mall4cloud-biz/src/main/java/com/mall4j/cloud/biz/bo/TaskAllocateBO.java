package com.mall4j.cloud.biz.bo;

import com.mall4j.cloud.biz.model.TaskClientGroupInfo;
import com.mall4j.cloud.biz.model.TaskClientInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 任务分配业务对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskAllocateBO {
    /**
     * 任务id
     */
    private Long taskId;
    /**
     * 任务类型
     */
    private Integer taskType;
    /**
     * 任务时间
     */
    private Date taskTime;
    /**
     * 导购id
     */
    private String shoppingGuideId;
    /**
     * 对应客户
     */
    private List<TaskClientInfo> taskClientInfoList;
    /**
     * 对应客户群
     */
    private List<TaskClientGroupInfo> taskClientGroupInfos;
    /**
     * cp_task_execute_info表id
     */
    private Long executeId;

}
