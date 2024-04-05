package com.mall4j.cloud.api.user.vo.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class AnalyzeUserStaffCpRelationListVO {

    @ApiModelProperty("客户会员id")
    private Long userId;
    @ApiModelProperty("企业微信昵称")
    private String qiWeiNickName;
    @ApiModelProperty("客户备注")
    private String userRemarks;
    @ApiModelProperty("客户企业微信id")
    private String qiWeiUserId;
    @ApiModelProperty("客户手机号")
    private String userPhone;
    @ApiModelProperty("导购staffId")
    private Long staffId;
    @ApiModelProperty("导购名称")
    private String staffName;
    @ApiModelProperty("导购企业微信id")
    private String qiWeiStaffId;
    @ApiModelProperty("状态")
    private Integer status;
    @ApiModelProperty("0自动通过/1手动通过")
    private Integer autoType;
    private String autoTypeName;
    @ApiModelProperty("添加时间")
    private Date cpCreateTime;
    @ApiModelProperty("好友关系")
    private Integer contactChangeType;
    @ApiModelProperty("好友关系")
    private String contactChangeTypeName;






}
