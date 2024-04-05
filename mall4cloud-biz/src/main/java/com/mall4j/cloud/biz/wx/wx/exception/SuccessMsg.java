/*
 * work_wx
 * wuhen 2020/1/16.
 * Copyright (c) 2020  jianfengwuhen@126.com All Rights Reserved.
 */

package com.mall4j.cloud.biz.wx.wx.exception;

/**
 *
 */
public class SuccessMsg extends SessionRequestMsg {

    public SuccessMsg(){
        super.state = 1;
        super.message = "操作成功";
    }

    public SuccessMsg(String result){
        super.state = 1;
        super.message = result;
    }


    public SuccessMsg(Object data){
        super.state = 1;
        super.message = "操作成功";
        super.data = data;
    }


}
