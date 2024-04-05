package com.mall4j.cloud.distribution.constant;

import java.util.Date;
import java.util.Objects;

/**
 * 活动状态枚举
 *
 * @author EricJeppesen
 * @date 2022/10/19 15:34
 */
public enum ActivityStatusEnum {
    // （0未开启,1已开启,2进行中,3已结束,）
    /**
     * 未开启
     */
    DISABLE(0, "未开启"),
    /**
     * 已开启
     */
    ENABLE(1, "已开启"),
    /**
     * 进行中
     */
    PROCESSING(2, "进行中"),
    /**
     * 已结束
     */
    ENDED(3, "已结束");
    /**
     * 状态码
     */
    private Integer code;
    /**
     * 描述
     */
    private String description;


    ActivityStatusEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
