package com.mall4j.cloud.biz.vo.chat;

import java.io.Serializable;
import java.util.Date;

/**
 * 会话存档VO
 *
 */
public class SessionFileVO implements Serializable {
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

}
