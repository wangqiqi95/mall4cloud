/*
 * work_wx
 * wuhen 2020/1/16.
 * Copyright (c) 2020  jianfengwuhen@126.com All Rights Reserved.
 */

package com.mall4j.cloud.biz.wx.wx.exception;



public class ErrorMsg extends SessionRequestMsg {
    public ErrorMsg(int state, String message) {
        super();
        this.state = state;
        this.message = message;
    }


    public ErrorMsg(int state) {
        super();
        this.state = state;
        this.message = "";
    }

}
