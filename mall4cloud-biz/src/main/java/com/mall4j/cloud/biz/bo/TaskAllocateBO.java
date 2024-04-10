package com.mall4j.cloud.biz.bo;

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
    private Long taskId;
    private Integer taskType;
    private Date taskTime;
    private String shoppingGuideId;
    private List<TaskClientInfo> taskClientInfoList;

    private Long executeId;

}
