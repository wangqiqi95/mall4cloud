package com.mall4j.cloud.biz.model;

import lombok.Data;

import java.util.Date;

/**
 * The table wechat_logistics_mapping
 */
@Data
public class WechatLogisticsMappingDO{

    /**
     * id 主键ID.
     */
    private Long id;
    /**
     * deliveryId 快递公司ID.
     */
    private String deliveryId;
    /**
     * wxDeliveryId 微信快递公司ID.
     */
    private String wxDeliveryId;
    /**
     * wxDeliveryName 微信快递公司名称.
     */
    private String wxDeliveryName;
    /**
     * 手机号
     */
    private String phone;
    /**
     * createBy 创建人 USERID.
     */
    private String createBy;
    /**
     * createTime 创建时间.
     */
    private Date createTime;
    /**
     * updateBy 修改人.
     */
    private String updateBy;
    /**
     * updateTime 修改时间.
     */
    private Date updateTime;
    /**
     * 视频号4.0物流公司编码
     */
    private String wxDeliveryCompanyId;
    /**
     * 视频号4.0快递公司名称
     */
    private String wxDeliveryCompanyName;
    /**
     * isDeleted 是否删除 0 否 1是.
     */
    private Integer isDeleted;

}
