package com.mall4j.cloud.user.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author ben
 * @since 2023-03-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_staff_batch_send_cp_msg")
@ApiModel(value="StaffBatchSendCpMsg对象", description="")
public class StaffBatchSendCpMsg extends Model<StaffBatchSendCpMsg> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "staff_batch_send_cp_msg_id", type = IdType.AUTO)
    private Long staffBatchSendCpMsgId;

    @ApiModelProperty(value = "推送任务ID")
    private Long pushTaskId;

    @ApiModelProperty(value = "推送子任务ID")
    private Long pushSonTaskId;

    @ApiModelProperty(value = "任务模式：0-客户群发 1-群群发")
    private Integer taskMode;

    @ApiModelProperty(value = "员工ID")
    private Long staffId;

    @ApiModelProperty(value = "导购ID")
    private String staffName;

    @ApiModelProperty(value = "企微群发任务ID")
    private String msgId;

    @ApiModelProperty(value = "0未发送，1已发送，2已停止")
    private Integer sendStatus;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "员工企微ID")
    private String staffCpUserId;

    @ApiModelProperty(value = "总人数")
    private Integer headCount;

    @ApiModelProperty(value = "触达数")
    private Integer reachCount;

    @ApiModelProperty(value = "完成状态；0未完成，1完成")
    private Integer finishState;

    @ApiModelProperty(value = "完成时间")
    private Date finishTime;

    private Date updateTime;

    private String updateBy;

    public StaffBatchSendCpMsg(Long groupPushTaskId, Long groupPushSonTaskId, Integer taskMode, Long staffId, String staffName,
        int sendStatus, String staffCpUserId, int finishState, int reachCount, int headCount) {
        this.pushTaskId = groupPushTaskId;
        this.pushSonTaskId = groupPushSonTaskId;
        this.taskMode = taskMode;
        this.staffId = staffId;
        this.staffName = staffName;
        this.sendStatus = sendStatus;
        this.staffCpUserId = staffCpUserId;
        this.finishState = finishState;
        this.reachCount = reachCount;
        this.headCount = headCount;
    }

    public StaffBatchSendCpMsg() {

    }

    @Override
    protected Serializable pkVal() {
        return this.staffBatchSendCpMsgId;
    }

}
