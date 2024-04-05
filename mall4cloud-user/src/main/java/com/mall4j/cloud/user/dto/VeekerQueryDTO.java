package com.mall4j.cloud.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class VeekerQueryDTO {

    @ApiModelProperty("微客名称")
    private String name;
    @ApiModelProperty("微客手机号")
    private String phone;
    @ApiModelProperty("微客会员卡号")
    private String cardNo;
    @ApiModelProperty("微客门店ID集合")
    private List<Long> storeIdList;
    @ApiModelProperty("微客申请开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date veekerApplyStartTime;
    @ApiModelProperty("微客申请结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date veekerApplyEndTime;
    @ApiModelProperty("是否添加微信 -1-全部 0-否 1-是")
    private Integer addWechat;
    @ApiModelProperty("微客状态 -1-全部 0-禁用 1-启用 2-拉黑")
    private Integer veekerStatus;
    @ApiModelProperty("微客审核状态 -1-全部 0-待审核 1-已同意")
    private Integer veekerAuditStatus;
    @ApiModelProperty("发展人名称")
    private String staffName;
    @ApiModelProperty("发展人状态 -1 全部 0-正常 1-注销")
    private Integer staffStatus;
    @ApiModelProperty("发展人手机号")
    private String staffMobile;
    @ApiModelProperty("发展人工号")
    private String staffNo;
    @ApiModelProperty("发展人门店ID集合")
    private List<Long> staffStoreIdList;
    @ApiModelProperty("发展人ID集合")
    private List<Long> staffIdList;

}
