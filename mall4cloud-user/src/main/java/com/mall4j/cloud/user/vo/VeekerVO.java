package com.mall4j.cloud.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class VeekerVO {

    @ApiModelProperty("微客ID")
    private Long userId;
    @ApiModelProperty("微客名称")
    private String name;
    @ApiModelProperty("头像图片路径")
    private String pic;
    @ApiModelProperty("微客手机号")
    private String phone;
    @ApiModelProperty("微客会员卡号")
    private String cardNo;
    @ApiModelProperty("微客门店ID")
    private Long storeId;
    @ApiModelProperty("微客门店编码")
    private String storeCode;
    @ApiModelProperty("微客门店名称")
    private String storeName;
    @ApiModelProperty("微客申请时间")
    private Date veekerApplyTime;
    @ApiModelProperty("是否添加微信 0-否 1-是")
    private Integer addWechat;
    @ApiModelProperty("微客状态 0-禁用 1-启用 2-拉黑")
    private Integer veekerStatus;
    @ApiModelProperty("微客审核状态 0-待审核 1-已同意 2-已拒绝")
    private Integer veekerAuditStatus;
    @ApiModelProperty("发展人ID")
    private Long staffId;
    @ApiModelProperty("发展人名称")
    private String staffName;
    @ApiModelProperty("发展人状态 0-正常 1-注销")
    private Integer staffStatus;
    @ApiModelProperty("发展人手机号")
    private String staffMobile;
    @ApiModelProperty("发展人工号")
    private String staffNo;
    @ApiModelProperty("发展人门店ID")
    private Long staffStoreId;
    @ApiModelProperty("发展人门店编码")
    private String staffStoreCode;
    @ApiModelProperty("发展人门店名称")
    private String staffStoreName;

}
