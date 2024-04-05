package com.mall4j.cloud.api.biz.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 会话存档上下文Vo
 *
 */
@Data
public class SessionFileVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    //消息id
    @ApiModelProperty(value = "消息id")
    private String msgid;
    //消息动作，目前有send(发送消息)/recall(撤回消息)/switch(切换企业日志)三种类型
    @ApiModelProperty(value = "消息动作")
    private String action;
    //消息发送方id
    @ApiModelProperty(value = "消息发送方id")
    private String from;
    @ApiModelProperty(value = "消息接收方")
    //消息接收方列表
    private String tolist;
    @ApiModelProperty(value = "群id")
    //群聊消息的群id
    private String roomid;
    //消息发送时间戳
    @ApiModelProperty(value = "消息发送时间戳")
    private Long msgtime;
    //消息类型
    @ApiModelProperty(value = "消息类型")
    private String msgtype;
    //消息内容
    @ApiModelProperty(value = "消息内容")
    private String content;

    private Long seq;

    private String avter;
    //会话总数
    private Integer sessionCount;
    //员工会话总数
    private Integer staffCount;
    //好友会话总数
    private Integer userCount;
    @ApiModelProperty(value = "员工名称")
    private String staffName;
    @ApiModelProperty(value = "员工头像")
    private String staffAvatar;
    @ApiModelProperty(value = "用户名称")
    private String userName;
    @ApiModelProperty(value = "用户头像")
    private String userAvatar;

    private Date createTime;

    private Date updateTime;
    @ApiModelProperty(value = "撤回标识")
    private String revoke;


}
