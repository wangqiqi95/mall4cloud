package com.mall4j.cloud.user.constant;


import lombok.Getter;

@Getter
public enum ImportTagUserStatusEnum {

    //进行中
    ING(0),
    //完成
    FINISH(1),
    //异常
    ERROR(2),
    //警告
    WARN(3);

    private Integer importStatus;

    ImportTagUserStatusEnum(Integer importStatus){
        this.importStatus = importStatus;
    }
}
