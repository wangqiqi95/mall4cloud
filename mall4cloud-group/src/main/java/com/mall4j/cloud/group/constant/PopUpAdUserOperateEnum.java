package com.mall4j.cloud.group.constant;


import lombok.Getter;

@Getter
public enum PopUpAdUserOperateEnum {

    BROWSE(1,"浏览"),
    CLICK(2,"点击");

    private Integer operate;
    private String operateStr;


    PopUpAdUserOperateEnum(Integer operate, String operateStr){
        this.operate = operate;
        this.operateStr = operateStr;
    }
}
