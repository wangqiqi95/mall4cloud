package com.mall4j.cloud.group.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel(value = "开屏广告活动列表实体")
public class OpenScreenAdListVO implements Serializable {
    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("活动名称")
    private String activityName;
    @ApiModelProperty("活动开始时间")
    private Date activityBeginTime;
    @ApiModelProperty("活动截至时间")
    private Date activityEndTime;
    @ApiModelProperty("是否全部门店 0否 1是")
    private Integer isAllShop;
    @ApiModelProperty("适用门店数")
    private Integer applyShopNum;
    @ApiModelProperty("适用粉丝等级")
    private String fansLevel;
    @ApiModelProperty("跳转类型0 不跳转 1跳转h5 2跳转小程序")
    private Integer redirectType;
    @ApiModelProperty("跳转链接")
    private String redirectUrl;
    @ApiModelProperty("广告频率类型 0每天一次 1每次打开出现弹窗 2累计仅一次")
    private Integer adFrequency;
    @ApiModelProperty("权重")
    private Integer weight;
    @ApiModelProperty("活动状态 0 禁用 1启用 2进行中 3 未开始 4已结束")
    private Integer status;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "创建人名称")
    private String createUserName;
}
