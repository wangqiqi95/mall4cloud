package com.mall4j.cloud.biz.model;

import lombok.Data;

import java.util.Date;

/**
 * The table wechat_notify_task
 */
@Data
public class WechatNotifyTaskDO{

    /**
     * id 主键ID.
     */
    private Long id;
    /**
     * tenantId 租户ID.
     */
    private String tenantId;
    /**
     * type 类型 0-商品 1-订单 2-售后.
     */
    private String type;
    /**
     * itemId ITEM_ID.
     */
    private String itemId;
    /**
     * itemStatus 0-初始化 1-完成.
     */
    private String itemStatus;
    /**
     * createTime 创建时间.
     */
    private Date createTime;
    /**
     * updateTime 修改时间.
     */
    private Date updateTime;
    /**
     * isDeleted 是否删除 0 否 1是.
     */
    private Integer isDeleted;

}
