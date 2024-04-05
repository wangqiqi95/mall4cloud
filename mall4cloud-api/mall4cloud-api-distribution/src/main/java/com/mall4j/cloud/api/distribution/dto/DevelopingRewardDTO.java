package com.mall4j.cloud.api.distribution.dto;

import lombok.Data;

import java.util.Date;

/**
 * @Author ZengFanChang
 * @Date 2021/12/26
 */
@Data
public class DevelopingRewardDTO {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 门店ID
     */
    private Long storeId;

    /**
     * 导购ID
     */
    private Long staffId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 申请时间
     */
    private Date applyTime;

    /**
     * 是否申请操作
     */
    private boolean apply;

}
