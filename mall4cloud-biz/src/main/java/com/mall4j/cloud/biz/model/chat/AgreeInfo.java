package com.mall4j.cloud.biz.model.chat;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * 会话同意情况
 *
 */
public class AgreeInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 同意时间
     */
    //private Long status_change_time;
    @SerializedName("status_change_time")
    private Long statusChangeTime;
    /**
     * 用户id
     */
    //private String userid;
    @SerializedName("userid")
    private String userId;
    /**
     * 外部客户id
     */
    //private String exteranalopenid;
    @SerializedName("exteranalopenid")
    private String exteranalOpenId;
    /**
     * 同意情况
     */
    //private String agree_status;
    @SerializedName("agree_status")
    private String agreeStatus;
    private Date changeTime;

    public Long getStatusChangeTime() {
        return statusChangeTime;
    }

    public void setStatusChangeTime(Long statusChangeTime) {
        this.statusChangeTime = statusChangeTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getExteranalOpenId() {
        return exteranalOpenId;
    }

    public void setExteranalOpenId(String exteranalOpenId) {
        this.exteranalOpenId = exteranalOpenId;
    }

    public String getAgreeStatus() {
        return agreeStatus;
    }

    public void setAgreeStatus(String agreeStatus) {
        this.agreeStatus = agreeStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Date changeTime) {
        this.changeTime = changeTime;
    }
}
