package com.mall4j.cloud.biz.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 积分礼品到货提醒记录关联
 * @author Grady_Lu
 */
@Data
public class WeixinMaSubscriptUserBussinessRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 用户订阅记录id
     */
    private Long userRecordId;

    /**
     * 业务记录编号
     */
    private Long bussinessRecordNo;

}
