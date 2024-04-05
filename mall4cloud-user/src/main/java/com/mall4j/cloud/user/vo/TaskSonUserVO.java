package com.mall4j.cloud.user.vo;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;

/**
 * 导购端推送任务-子任务触达人群
 */
@Data
public class TaskSonUserVO implements Serializable {

    @ApiModelProperty(value = "发送记录id")
    private Long taskFinishRecordId;

    @ApiModelProperty(value = "企微userId")
    private String qiWeiUserId;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "头像")
    private String pic;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "0-待发送 1-已发送 2-因客户不是好友导致发送失败 3-因客户已经收到其他群发消息导致发送失败")
    private Integer sendStatus;

}
