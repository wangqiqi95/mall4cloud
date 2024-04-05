package com.mall4j.cloud.api.biz.dto.cp.crm;

import lombok.Data;
import me.chanjar.weixin.cp.bean.WxCpDepart;
import me.chanjar.weixin.cp.bean.WxCpUser;
import me.chanjar.weixin.cp.bean.external.WxCpUserExternalGroupChatInfo;
import me.chanjar.weixin.cp.bean.external.contact.WxCpExternalContactInfo;
import me.chanjar.weixin.cp.bean.message.WxCpXmlMessage;

@Data
public class PushCDPCpMsgEventDTO {

    /**
     * 推送数云枚举类型: PushCrmCpTypeEnums
     */
    private String msgType;

    /**
     * 接收企微消息信息
     */
    private WxCpXmlMessage wxMessage;


    /**
     * 外部联系人信息
     */
    private WxCpExternalContactInfo wxCpExternalContactInfo;

    /**
     * 群聊信息【包含群成员】
     */
    private WxCpUserExternalGroupChatInfo.GroupChat groupChat;

    /**
     * 部门信息
     */
    private WxCpDepart depart;

    /**
     * 员工信息
     */
    private WxCpUser wxCpUser;

    private String changetype;

    private String staffUserId;
}
