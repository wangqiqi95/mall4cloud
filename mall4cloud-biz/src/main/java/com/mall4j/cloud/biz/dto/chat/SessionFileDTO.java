package com.mall4j.cloud.biz.dto.chat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("会话存档检索DTO")
public class SessionFileDTO {


    @ApiModelProperty(value = "会话存档id")
    private String id;
    //消息id
    private String msgid;
    //消息动作，目前有send(发送消息)/recall(撤回消息)/switch(切换企业日志)三种类型
    private String action;
    //消息发送方id
    @ApiModelProperty(value = "发送消息人")
    private String from;
    //消息接收方列表
    @ApiModelProperty(value = "接收消息人")
    private String tolist;
    //群聊消息的群id
    private String roomid;
    //消息发送时间戳
    private Long msgtime;
    //消息类型
    private String msgtype;
    //消息内容
    private String content;
    @ApiModelProperty(value = "开始时间")
    private String startTime;
    @ApiModelProperty(value = "结束时间")
    private String endTime;
    @ApiModelProperty("员工ID集合")
    private List<Long> staffIdList;
}
