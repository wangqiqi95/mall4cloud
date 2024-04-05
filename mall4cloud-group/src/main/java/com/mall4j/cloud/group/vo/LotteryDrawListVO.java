package com.mall4j.cloud.group.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel("抽奖活动列表实体")
public class LotteryDrawListVO implements Serializable {
    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("活动名称")
    private String activityName;
    @ApiModelProperty("游戏类型 1 砸金蛋 2摇一摇 3 0元抽奖 4 大转盘 5刮刮乐")
    private Integer gameType;
    @ApiModelProperty("活动开始时间")
    private Date activityBeginTime;
    @ApiModelProperty("活动截至时间")
    private Date activityEndTime;
    @ApiModelProperty("是否全部门店 0否 1是")
    private Integer isAllShop;
    @ApiModelProperty("适用门店数")
    private Integer applyShopNum;
    @ApiModelProperty("活动状态 0 禁用 1启用 2进行中 3 未开始 4已结束")
    private Integer status;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "创建人名称")
    private String createUserName;
}
