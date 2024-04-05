package com.mall4j.cloud.group.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel("套餐一口加列表实体")
public class MealActivityListVO implements Serializable {
    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("活动名称")
    private String activityName;
    @ApiModelProperty("活动开始时间")
    private Date activityBeginTime;
    @ApiModelProperty("活动截至时间")
    private Date activityEndTime;
    @ApiModelProperty("是否全部门店 0 否 1是")
    private Integer isAllShop;
    @ApiModelProperty("适用门店数")
    private Integer applyShopNum;
    @ApiModelProperty("活动状态 0 禁用 1启用 2进行中 3 未开始 4已结束")
    private Integer status;
    @ApiModelProperty("权重")
    private Integer weight;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "创建人名称")
    private String createUserName;
}
