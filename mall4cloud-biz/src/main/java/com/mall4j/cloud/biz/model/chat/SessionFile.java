package com.mall4j.cloud.biz.model.chat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 会话存档表
 *
 */
@Data
public class SessionFile implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    //消息id
    private String msgid;
    //消息动作，目前有send(发送消息)/recall(撤回消息)/switch(切换企业日志)三种类型
    private String action;
    //消息发送方id
    private String from;
    //消息接收方列表
    private String tolist;
    //群聊消息的群id
    private String roomid;
    //消息发送时间戳
    private Long msgtime;
    //消息类型
    private String msgtype;
    //消息内容
    private String content;

    private Long seq;

    private String avter;
    //会话总数
    private Integer sessionCount;
    //员工会话总数
    private Integer staffCount;
    //好友会话总数
    private Integer userCount;

    private String staffName;
    private String staffAvatar;
    private String userName;
    private String userAvatar;

    private Date createTime;

    private Date updateTime;

    @ApiModelProperty(value = "文件base64数据(目前仅表情包使用)")
    private String streamBaseData;

    @ApiModelProperty(value = "撤回标识")
    private String revoke;
}
