package com.mall4j.cloud.api.platform.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import java.util.List;

@Data
public class StaffQueryDTO {

    @ApiModelProperty("关键字查询(支持员工名称、工号、手机号)")
    private String keyword;
    @ApiModelProperty("员工名称")
    private String staffName;
    @ApiModelProperty("员工手机号")
    private String mobile;
    @ApiModelProperty("员工工号")
    private String staffNo;
    @ApiModelProperty("机构id")
    private Long orgId;
    @ApiModelProperty("部门ID集合")
    private List<Long> orgIds;
    @ApiModelProperty("员工门店ID集合")
    private List<Long> storeIdList;
    @ApiModelProperty("状态0 正常 1注销")
    private Integer status;
    @ApiModelProperty("员工ID集合")
    private List<Long> staffIdList;
    @ApiModelProperty("排除员工ID集合")
    private List<Long> staffIdListNot;
    @ApiModelProperty("员工门店ID")
    private Long storeId;
    @ApiModelProperty("企微用户状态:0-待加入 1-已激活 2-已禁用 4-未激活 5-退出企业")
    private Integer qiweiUserStatus;
    @ApiModelProperty("企微用户状态:0-待加入 1-已激活 2-已禁用 4-未激活 5-退出企业")
    private List<Integer> qiweiUserStatusList;
    @ApiModelProperty("角色类型 1-导购 2-店长 3-店务")
    private Integer roleType;

    @ApiModelProperty(value = "0:默认排序(时间倒序) 1:按工号倒序")
    private Integer sort;

    @ApiModelProperty("所属门店")
    private String store;

    private String path;

    @ApiModelProperty("手机号/工号集合")
    private List<String> mobileStaffNoList;

    @ApiModelProperty("是否开启会话存档: 0否/1是")
    private Integer cpMsgAudit;

    @ApiModelProperty("是否返回部门信息: false否/true是")
    private Boolean backOrg=false;

    private List<String> qiWeiUserIds;

    private Integer isDelete;

}
