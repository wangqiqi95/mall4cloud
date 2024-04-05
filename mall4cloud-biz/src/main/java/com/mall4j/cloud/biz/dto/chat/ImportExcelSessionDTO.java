package com.mall4j.cloud.biz.dto.chat;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ImportExcelSessionDTO implements Serializable {

    @ExcelProperty("id")
    private String id;

    @ExcelProperty("企业微信Seq")
    private String seq;

    @ExcelProperty("企业微信消息Id")
    private String msgId;

    @ExcelProperty("企业微信消息动作")
    private String msgAction;

    @ExcelProperty("发送人UserId")
    private String sendUserId;

    @ExcelProperty("接收人UserIds")
    private String receiveUserIds;

    @ExcelProperty("群ID")
    private String chatId;

    @ExcelProperty("消息时间")
    private String msgTime;

    @ExcelProperty("消息类型")
    private String msgType;

    @ExcelProperty("消息内容")
    private String msgContent;

    @ExcelProperty("发送人头像")
    private String sendUserAvater;

    @ExcelProperty("发送人姓名")
    private String sendUserName;

    @ExcelProperty("群名")
    private String chatName;

    @ExcelProperty("附件id")
    private String mediaId;


}
