package com.mall4j.cloud.user.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GroupSonTaskSendRecordVO {

    @ApiModelProperty(value = "主键")
    @TableId(value = "task_finish_record_id", type = IdType.AUTO)
    private Long taskFinishRecordId;

    @ApiModelProperty(value = "群发任务ID")
    private Long pushTaskId;

    @ApiModelProperty(value = "群发子任务ID")
    private Long pushSonTaskId;

    @ApiModelProperty(value = "用户ID")
    private Long vipUserId;

    @ApiModelProperty(value = "导购ID")
    private Long staffId;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "0-进行中 1-已发送 2-因客户不是好友导致发送失败 3-因客户已经收到其他群发消息导致发送失败")
    private Integer sendStatus;

    @ApiModelProperty("发送类型，1：1v1，2：批量群发")
    private Integer sendModel;

    @ApiModelProperty("用户企业微信ID")
    private String vipCpUserId;

    @ApiModelProperty(value = "完成时间")
    private LocalDateTime finishTime;

}
