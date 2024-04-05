package com.mall4j.cloud.group.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel("开屏广告添加实体")
public class OpenScreenAdDTO implements Serializable {
    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("活动名称")
    private String activityName;
    @ApiModelProperty("活动开始时间")
    private Date activityBeginTime;
    @ApiModelProperty("活动截至时间")
    private Date activityEndTime;
    @ApiModelProperty("适用门店")
    private String applyShopIds;
    @ApiModelProperty("是否全部门店 0否 1是")
    private Integer isAllShop;
    @ApiModelProperty("活动图片链接")
    private String activityPicUrl;
    @ApiModelProperty("适用粉丝等级集合")
    private String fansLevel;
    @ApiModelProperty("跳转类型0 不跳转 1跳转h5 2跳转小程序")
    private Integer redirectType;
    @ApiModelProperty("跳转链接")
    private String redirectUrl;
    @ApiModelProperty("广告频率类型 0每天一次 1每次打开出现弹窗 2累计仅一次")
    private Integer adFrequency;
    @ApiModelProperty("权重")
    private Integer weight;
    @ApiModelProperty("状态 0未启用 1已启用")
    private Integer status;
    @ApiModelProperty(value = "创建人id",hidden = true)
    private Long createUserId;
    @ApiModelProperty(value = "创建人名称",hidden = true)
    private String createUserName;
    @ApiModelProperty(value = "更新人id",hidden = true)
    private Long updateUserId;
    @ApiModelProperty(value = "更新人姓名",hidden = true)
    private String updateUserName;
}
