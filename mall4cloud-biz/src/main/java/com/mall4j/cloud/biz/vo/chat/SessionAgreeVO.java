package com.mall4j.cloud.biz.vo.chat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 会话同意VO
 *
 */
@Data
public class SessionAgreeVO {
    private Long id;
    /**
     * 同意状态
     */
    @ApiModelProperty("同意状态")
    private String agreeStatus;
    private String agreeStatusName;
    /**
     * 每月同意人数
     */
    private Integer monthCount;
    /**
     * 月份
     */
    private String mon;
    /**
     * 人数
     */
    private Integer agreeCount;

    private String userId;
    private String exteranalOpenId;
    private Long statusChangeTime;
    private Date changeTime;
    /**
     * 同意总量
     */
    private Integer agreeSum;

    @ApiModelProperty("员工名称")
    private String staffName;
    @ApiModelProperty("客户昵称")
    private String userName;
    @ApiModelProperty("客户名称")
    private String cpRemark;
    @ApiModelProperty("客户手机号")
    private String phone;
    @ApiModelProperty("企业")
    private String corpFullName;
}
