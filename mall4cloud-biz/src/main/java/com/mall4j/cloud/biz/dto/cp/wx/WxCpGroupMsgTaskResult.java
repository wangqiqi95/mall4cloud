package com.mall4j.cloud.biz.dto.cp.wx;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import me.chanjar.weixin.cp.bean.WxCpBaseResp;
import me.chanjar.weixin.cp.bean.external.contact.WxCpGroupMsgSendResult;
import me.chanjar.weixin.cp.util.json.WxCpGsonBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * 获取群发成员发送任务列表响应参数
 * @author Peter_Tan
 */
@Data
public class WxCpGroupMsgTaskResult extends WxCpBaseResp implements Serializable {
    private static final long serialVersionUID = -5166048319463473186L;
    @SerializedName("task_list")
    private List<WxCpGroupMsgTaskResult.ExternalContactGroupMsgTaskInfo> taskList;
    @SerializedName("next_cursor")
    private String nextCursor;

    public WxCpGroupMsgTaskResult() {
    }

    public static WxCpGroupMsgSendResult fromJson(String json) {
        return (WxCpGroupMsgSendResult) WxCpGsonBuilder.create().fromJson(json, WxCpGroupMsgSendResult.class);
    }

    public List<WxCpGroupMsgTaskResult.ExternalContactGroupMsgTaskInfo> getTaskList() {
        return this.taskList;
    }

    public String getNextCursor() {
        return this.nextCursor;
    }

    public void setTaskList(List<WxCpGroupMsgTaskResult.ExternalContactGroupMsgTaskInfo> taskList) {
        this.taskList = taskList;
    }

    public void setNextCursor(String nextCursor) {
        this.nextCursor = nextCursor;
    }

    public static class ExternalContactGroupMsgTaskInfo implements Serializable {
        private static final long serialVersionUID = 1500416806087532531L;
        @SerializedName("userid")
        private String userId;
        private Integer status;
        @SerializedName("send_time")
        private Long sendTime;

        public ExternalContactGroupMsgTaskInfo() {
        }

        public String getUserId() {
            return this.userId;
        }

        public Integer getStatus() {
            return this.status;
        }

        public Long getSendTime() {
            return this.sendTime;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public void setSendTime(Long sendTime) {
            this.sendTime = sendTime;
        }
    }

}
