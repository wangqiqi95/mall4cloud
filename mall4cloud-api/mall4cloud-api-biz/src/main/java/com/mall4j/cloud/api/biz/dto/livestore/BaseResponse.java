package com.mall4j.cloud.api.biz.dto.livestore;

/**
 * 基础响应
 * @author lt
 */
public class BaseResponse {
    public Integer getErrcode() {
        return errcode;
    }

    public void setErrcode(Integer errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    /**
     * 错误码
     */
    private Integer errcode;
    /**
     * 错误消息
     */
    private String errmsg;
}
