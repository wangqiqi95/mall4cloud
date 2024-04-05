package com.mall4j.cloud.user.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 推送完成记录表
 * </p>
 *
 * @author ben
 * @since 2023-03-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_group_son_task_send_record")
@ApiModel(value="GroupSonTaskSendRecord对象", description="推送完成记录表")
public class GroupSonTaskSendRecord extends Model<GroupSonTaskSendRecord> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "task_finish_record_id", type = IdType.AUTO)
    private Long taskFinishRecordId;

    @ApiModelProperty(value = "群发任务ID")
    private Long pushTaskId;

    @ApiModelProperty(value = "群发子任务ID")
    private Long pushSonTaskId;

    @ApiModelProperty(value = "推送名称（子任务名称）")
    private String sonTaskName;

    @ApiModelProperty(value = "任务模式：0-客户群发 1-群群发")
    private Integer taskMode;

    @ApiModelProperty(value = "任务类型：0-SCRM任务 1-CDP任务")
    private Integer taskType;

    @ApiModelProperty(value = "员工id")
    private Long staffId;

    @ApiModelProperty(value = "员工name")
    private String staffName;

    @ApiModelProperty(value = "员工企微ID")
    private String staffCpUserId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "0-待发送 1-已发送 2-因客户不是好友导致发送失败 3-因客户已经收到其他群发消息导致发送失败")
    private Integer sendStatus;

    @ApiModelProperty("发送类型，1：1v1，2：批量群发")
    private Integer sendModel;

    @ApiModelProperty("用户企业微信ID 或者 群id")
    private String vipCpUserId;

    @ApiModelProperty("统一id")
    private String unionId;

    @ApiModelProperty("企业微信昵称")
    private String qiWeiNickName;

    @ApiModelProperty("该成员对此外部联系人的备注")
    private String cpRemark;

    @ApiModelProperty("该成员对此客户备注的手机号码")
    private String cpRemarkMobiles;

    @ApiModelProperty(value = "完成时间")
    private Date finishTime;

    public GroupSonTaskSendRecord(Long groupPushSonTaskId, Long groupPushTaskId, Integer taskMode, Integer taskType,
        String sonTaskName, Long staffId, String staffName, String qiWeiStaffId, int sendStatus, int sendModel,
        String qiWeiUserId, String qiWeiNickName, String cpRemark, String cpRemarkMobiles, String unionId) {
        this.pushTaskId = groupPushTaskId;
        this.pushSonTaskId = groupPushSonTaskId;
        this.taskMode = taskMode;
        this.taskType = taskType;
        this.sonTaskName = sonTaskName;
        this.staffId = staffId;
        this.staffName = staffName;
        this.staffCpUserId = qiWeiStaffId;
        this.sendStatus = sendStatus;
        this.sendModel = sendModel;
        this.sendStatus = sendStatus;
        this.vipCpUserId = qiWeiUserId;
        this.qiWeiNickName = qiWeiNickName;
        this.cpRemark = cpRemark;
        this.sendStatus = sendStatus;
        this.cpRemarkMobiles = cpRemarkMobiles;
        this.unionId = unionId;
    }

    public GroupSonTaskSendRecord() {

    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

}
