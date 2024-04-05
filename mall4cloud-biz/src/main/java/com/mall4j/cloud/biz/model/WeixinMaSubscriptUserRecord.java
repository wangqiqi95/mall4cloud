package com.mall4j.cloud.biz.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 微信小程序用户订阅记录
 *
 * @author FrozenWatermelon
 * @date 2022-03-23 11:10:56
 */
@Data
public class WeixinMaSubscriptUserRecord implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 模版id
     */
    private String templateId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 小程序openid
     */
    private String openId;

    /**
     * 业务单据id
     */
    private String bussinessId;

    /**
     * 发送状态 0未发送，1已经发送
     */
    private Integer sendStatus;
    /**
     * 发送状态 0未发送，1已经发送
     */
    private Integer sendType;

    /**
     * 创建时间
     */
    private Date createTime;

}
