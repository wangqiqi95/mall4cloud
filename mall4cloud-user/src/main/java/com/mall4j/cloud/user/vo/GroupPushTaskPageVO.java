package com.mall4j.cloud.user.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mall4j.cloud.user.model.GroupPushTaskStaffRelation;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class GroupPushTaskPageVO {

    @ApiModelProperty(value = "主键")
    private Long groupPushTaskId;

    @ApiModelProperty(value = "任务模式：0-客户群发 1-群群发")
    private Integer taskMode;

    @ApiModelProperty(value = "任务名称")
    private String taskName;

    @ApiModelProperty(value = "执行人列表")
    private  List<GroupPushTaskStaffRelation> staffRelationList;

    @ApiModelProperty(value = "任务类型：0-SCRM任务 1-CDP任务")
    private Integer taskType;

    @ApiModelProperty(value = "创建人")
    private Long createUserId;

    @ApiModelProperty(value = "创建人昵称")
    private String createUserNickName;

    @ApiModelProperty(value = "创建状态，0创建中，1创建完成，2创建失败")
    private Integer createStatus;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "操作状态，0 创建中 1 启用中（对应之前的 创建中） 2 创建失败 3 编辑中 4 编辑失败 5 草稿 6 启用中 7 启用失败 8 未启用")
    private Integer operateStatus;

    @ApiModelProperty("是否允许成员在待发送客户列表中重新进行选择：0否/1是")
    private Integer allowSelect;


}
