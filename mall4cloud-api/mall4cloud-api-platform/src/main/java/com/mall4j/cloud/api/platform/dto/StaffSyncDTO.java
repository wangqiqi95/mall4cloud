package com.mall4j.cloud.api.platform.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class StaffSyncDTO {

    @ApiModelProperty("员工姓名")
    private String name;
    @ApiModelProperty(value = "门店编码")
    private String storeCode;
    @ApiModelProperty("员工手机号")
    private String mobile;
    @ApiModelProperty("员工编码")
    private String staffCode;
    @ApiModelProperty("员工工号")
    private String staffNo;
    @ApiModelProperty("是否离职 Y or N")
    private String isLeave;
    @ApiModelProperty("企微userId")
    private String qiweiUserId;
    @ApiModelProperty("企微用户状态 1-已激活 2-已禁用 4-未激活 5-退出企业")
    private Integer qiweiUserStatus;
    @ApiModelProperty(value = "角色类型 1-导购 2-店长 3-店务")
    private Integer roleType;
    private String createTime;
    private String avater;

    private List<String> orgIds;

}
