package com.mall4j.cloud.user.vo;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;

@Data
public class GroupPushTaskDetailStatisticVO {

    @ApiModelProperty("任务模式：0-客户群发 1-群群发")
    private Integer taskMode;
    @ApiModelProperty("销售名称")
    private String staffName;
    @ApiModelProperty("创建时间")
    private Date creatTime;
    @ApiModelProperty("推送名称")
    private String sonTaskName;
    @ApiModelProperty("接受者昵称")
    private String userName;
    @ApiModelProperty("接受者备注")
    private String userRemark;
    @ApiModelProperty("手机号")
    private String phone;
    @ApiModelProperty("0-待发送 1-已发送 2-因客户不是好友导致发送失败 3-因客户已经收到其他群发消息导致发送失败")
    private Integer sendStatus;
    @ApiModelProperty("任务类型：0-SCRM任务 1-CDP任务")
    private Integer taskType;

}
