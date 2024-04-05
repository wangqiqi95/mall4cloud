package com.mall4j.cloud.biz.dto.cp.wx;

import com.google.gson.annotations.SerializedName;
import me.chanjar.weixin.cp.bean.WxCpBaseResp;
import me.chanjar.weixin.cp.util.json.WxCpGsonBuilder;

import java.util.List;

public class WxBizCpUserExternalGroupChatTransferResp extends WxCpBaseResp {
    private static final long serialVersionUID = -943124579487821819L;
    @SerializedName("failed_chat_list")
    private List<WxBizCpUserExternalGroupChatTransferResp.GroupChatFailedTransfer> failedChatList;
    public WxBizCpUserExternalGroupChatTransferResp() {
    }

    public static WxBizCpUserExternalGroupChatTransferResp fromJson(String json) {
        return (WxBizCpUserExternalGroupChatTransferResp)WxCpGsonBuilder.create().fromJson(json, WxBizCpUserExternalGroupChatTransferResp.class);
    }

    public String toJson() {
        return WxCpGsonBuilder.create().toJson(this);
    }

    public List<WxBizCpUserExternalGroupChatTransferResp.GroupChatFailedTransfer> getFailedChatList() {
        return this.failedChatList;
    }

    public void setFailedChatList(List<WxBizCpUserExternalGroupChatTransferResp.GroupChatFailedTransfer> failedChatList) {
        this.failedChatList = failedChatList;
    }
    public static class GroupChatFailedTransfer extends WxCpBaseResp {
        private static final long serialVersionUID = -5836775099634587239L;
        @SerializedName("chat_id")
        private String chatId;

        public GroupChatFailedTransfer() {
        }

        public static WxBizCpUserExternalGroupChatTransferResp.GroupChatFailedTransfer fromJson(String json) {
            return (WxBizCpUserExternalGroupChatTransferResp.GroupChatFailedTransfer) WxCpGsonBuilder.create().fromJson(json, WxBizCpUserExternalGroupChatTransferResp.GroupChatFailedTransfer.class);
        }

        public String toJson() {
            return WxCpGsonBuilder.create().toJson(this);
        }

        public String getChatId() {
            return this.chatId;
        }

        public void setChatId(String chatId) {
            this.chatId = chatId;
        }
    }
}
