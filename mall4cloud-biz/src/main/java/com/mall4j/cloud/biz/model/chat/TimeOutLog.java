package com.mall4j.cloud.biz.model.chat;

import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

import java.util.Date;

/**
 * 超时记录表
 *
 */
@Data
public class TimeOutLog extends BaseModel {

    private Long id;
    /**
     * 员工姓名
     */
    private String staffName;
    /**
     * 员工id
     */
    private String staffId;
    /**
     *客户姓名
     */
    private String userName;
    /**
     * 客户id
     */
    private String userId;
    /**
     * 记录状态，1表示超时，0表示最新记录
     */
    private String status;
    /**
     * 超时触发时间
     */
    private Date triggerTime;
    /**
     * 客户发送消息时间
     */
    private Date sendTime;
    /**
     * 群id
     */
    private String roomId;
    /**
     * 命中规则
     */
    private String ruleName;
    /**
     * 超时规则id
     */
    private Long timeOutId;

}
