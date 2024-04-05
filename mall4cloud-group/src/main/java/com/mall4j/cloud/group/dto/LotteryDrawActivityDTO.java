package com.mall4j.cloud.group.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "添加抽奖活动主要信息实体")
public class LotteryDrawActivityDTO implements Serializable {
    @ApiModelProperty("id")
    private Integer id;


    @ApiModelProperty("活动名称")
    private String activityName;


    @ApiModelProperty("活动开始时间")
    private Date activityBeginTime;


    @ApiModelProperty("活动截止时间")
    private Date activityEndTime;


    @ApiModelProperty("适用门店")
    private String applyShopIds;
    @ApiModelProperty("是否全部门店")
    private Integer isAllShop;


    @ApiModelProperty("是否展示积分商城")
    private Integer showPointMall;


    @ApiModelProperty("抽奖类型 1每人每天 2每人总共")
    private Integer drawType;


    @ApiModelProperty("抽奖次数")
    private Integer drawTimes;


    @ApiModelProperty("分享获得抽奖次数开关")
    private Integer shareSwitch;


    @ApiModelProperty("分享可获得的抽奖次数")
    private Integer shareTimes;


    @ApiModelProperty("使用积分方式 1.每次消耗 2.抽奖次数使用完之后消耗")
    private Integer usePointType;


    @ApiModelProperty("使用积分数")
    private Integer usePoint;


    @ApiModelProperty("活动规则")
    private String activityRule;


    @ApiModelProperty("分享标题")
    private String shareTitle;


    @ApiModelProperty("分享封面")
    private String sharePicUrl;

    @ApiModelProperty(hidden = true)
    private String createUserName;
    @ApiModelProperty(hidden = true)
    private String updateUserName;

    @ApiModelProperty("订单消费限制开关")
    private Integer orderSwitch;

    @ApiModelProperty("订单消费开始时间")
    private Date orderStartTime;

    @ApiModelProperty("订单消费结束时间")
    private Date orderEndTime;

    @ApiModelProperty("订单消费类型【0:表示累计消费;1:表示单笔消费】")
    private Integer orderExpendType;

    @ApiModelProperty("订单消费金额限制")
    private Long orderNum;

    @ApiModelProperty("订单类型限制")
    private String orderType;

    @ApiModelProperty("订单金额不足提示")
    private String orderTips;

    @ApiModelProperty("会员等级集合")
    private String fanLevels;

    @ApiModelProperty("会员等级不足提示")
    private String fanTips;

    @ApiModelProperty("指定消费门店集合")
    private String appointShop;

}
