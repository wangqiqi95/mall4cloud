package com.mall4j.cloud.biz.model.cp;

import com.mall4j.cloud.biz.wx.cp.constant.SendStatus;
import com.mall4j.cloud.biz.wx.cp.constant.StatusType;
import com.mall4j.cloud.biz.dto.cp.TaskPushDTO;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 推送任务表
 *
 * @author hwy
 * @date 2022-02-18 18:17:52
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TaskPush extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    public TaskPush(){}
    public TaskPush(TaskPushDTO pushDTO, SendTask sendTask){
        this.setTaskId(sendTask.getId());
        this.setCreateBy(sendTask.getCreateBy());
        this.setCreateName(sendTask.getCreateName());
        this.setCreateTime(sendTask.getCreateTime());
        this.setUpdateTime(sendTask.getUpdateTime());
        this.setFlag(StatusType.WX.getCode());
        this.setStatus(SendStatus.WAIT.getCode());
        this.setPushName(pushDTO.getPushName());
        this.setContent(pushDTO.getContent());
        this.setStartTime(pushDTO.getStartTime());
        this.setEndTime(pushDTO.getEndTime());
        this.pushDTO = pushDTO;
    }
    private TaskPushDTO pushDTO;

    /**
     * 
     */
    private Long id;

    /**
     * 任务名称
     */
    private Long taskId;


    /**
     * 推送名称
     */
    private String pushName;

    /**
     * 推送开始时间
     */
    private Date startTime;

    /**
     * 推送截止时间
     */
    private Date endTime;

    /**
     * 推送内容
     */
    private String content;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 删除标识
     */
    private Integer flag;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 创建人
     */
    private String createName;

    /**
     * 创建时间
     */
    private Date createTime;

}
