package com.mall4j.cloud.api.platform.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class StaffVO {

    @ApiModelProperty("员工ID")
    private Long id;
    @ApiModelProperty("员工手机号")
    private String mobile;
    @ApiModelProperty("员工工号")
    private String staffNo;
    @ApiModelProperty("员工名称")
    private String staffName;
    @ApiModelProperty("门店ID")
    private Long storeId;
    @ApiModelProperty("门店编码")
    private String storeCode;
    @ApiModelProperty("门店名称")
    private String storeName;
    @ApiModelProperty("状态 0 正常 1注销")
    private Integer status;
    @ApiModelProperty("角色类型 1-导购 2-店长 3-店务")
    private Integer roleType;
    @ApiModelProperty("头像")
    private String avatar;
    @ApiModelProperty("注册时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @ApiModelProperty("企微用户id")
    private String qiWeiUserId;
    @ApiModelProperty("企微用户状态 1-已激活 2-已禁用 4-未激活 5-退出企业")
    private Integer qiWeiUserStatus;

    @ApiModelProperty("员工邮箱")
    private String email;

    @ApiModelProperty("员工职位")
    private String position;

    @ApiModelProperty("会员数量")
    private Integer memberNum;
    @ApiModelProperty("微客数量")
    private Integer veekerNum;


    @ApiModelProperty("组织节点ID")
    private String orgId;
    @ApiModelProperty("组织节点编码")
    private String orgCode;
    @ApiModelProperty("组织节点名称")
    private String orgName;

    private List<StaffOrgVO> orgs;


    @ApiModelProperty("是否虚拟门店：0否 1是")
    private Integer storeInviteType;

    @ApiModelProperty("官店门店ID")
    private Long mainStoreId;
    @ApiModelProperty("官店门店编码")
    private String mainStoreCode;
    @ApiModelProperty("官店门店名称")
    private String mainStoreName;
    @ApiModelProperty("员工微信号")
    private String weChatNo;

    @ApiModelProperty("是否开启会话存档: 0否/1是")
    private Integer cpMsgAudit;

    /**
     * 系统账户id，对应sys_user表id
     */
    private String sysUserId;

    private Integer isDelete;

}
