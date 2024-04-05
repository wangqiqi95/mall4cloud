package com.mall4j.cloud.biz.model.chat;

/**
 * 会话存档token
 */
public class SessionTokenModel {

    private String corpId;
    private Integer expires_in;
    private String access_token;
    private Long loseTime;
    private String errmsg;
    private Integer errcode;


    public SessionTokenModel(String corpId) {
        this.corpId = corpId;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public Integer getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Integer expires_in) {
        this.expires_in = expires_in;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public Long getLoseTime() {
        return loseTime;
    }

    public void setLoseTime(Long loseTime) {
        this.loseTime = loseTime;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public Integer getErrcode() {
        return errcode;
    }

    public void setErrcode(Integer errcode) {
        this.errcode = errcode;
    }
}
