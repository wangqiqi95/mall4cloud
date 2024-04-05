package com.mall4j.cloud.biz.dto.cp;

import com.mall4j.cloud.biz.model.cp.SendTask;
import com.mall4j.cloud.biz.model.cp.CpTaskStaffRef;
import com.mall4j.cloud.biz.vo.cp.TaskPushVO;
import lombok.Data;

import java.util.List;

@Data
public class SendTaskDetailDTO {
    public SendTaskDetailDTO(SendTask sendTask,List<TaskPushVO> pushList,List<CpTaskStaffRef> staffList){
        this.pushList = pushList;
        this.sendTask = sendTask;
        this.staffList = staffList;
    }
    private SendTask sendTask;
    private List<CpTaskStaffRef> staffList;
    private List<TaskPushVO> pushList;

}
