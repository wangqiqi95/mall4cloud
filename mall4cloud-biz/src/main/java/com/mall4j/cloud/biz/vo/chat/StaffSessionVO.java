package com.mall4j.cloud.biz.vo.chat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 员工收发消息VO
 *
 */
@Data
public class StaffSessionVO {
    @ApiModelProperty("员工名称")
    private String staffName;
    @ApiModelProperty("部门")
    private String emp;
    @ApiModelProperty("互动个数")
    private Integer interactionCount;
    @ApiModelProperty("发送消息")
    private Integer sendCount;
    @ApiModelProperty("接收消息")
    private Integer receiveCount;
    @ApiModelProperty("活跃个数")
    private Integer activeCont;
    @ApiModelProperty("群发送消息")
    private Integer roomSend;
    @ApiModelProperty("群接收消息")
    private Integer roomReceive;
    @ApiModelProperty("发送消息总数/会话总数")
    private Integer sendSum;
    @ApiModelProperty("接收消息总数")
    private Integer receiveSum;
    private String from;
}
