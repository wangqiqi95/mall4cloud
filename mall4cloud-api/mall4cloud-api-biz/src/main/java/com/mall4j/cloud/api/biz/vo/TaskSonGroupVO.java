package com.mall4j.cloud.api.biz.vo;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;

/**
 * 导购端推送任务-子任务触达人群
 */
@Data
public class TaskSonGroupVO implements Serializable {

    @ApiModelProperty(value = "群名称")
    private String groupName;

    @ApiModelProperty(value = "群id")
    private String groupId;

    @ApiModelProperty(value = "群人数")
    private Integer total;

    @ApiModelProperty(value = "群主名称")
    private String ownerName;

    @ApiModelProperty(value = "0-待发送 1-已发送 2-因客户不是好友导致发送失败 3-因客户已经收到其他群发消息导致发送失败")
    private Integer sendStatus;

}
