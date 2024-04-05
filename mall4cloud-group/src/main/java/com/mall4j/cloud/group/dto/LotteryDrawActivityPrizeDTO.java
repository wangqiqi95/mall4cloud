package com.mall4j.cloud.group.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "抽奖活动添加奖品配置信息实体详情")
public class LotteryDrawActivityPrizeDTO implements Serializable {
    @ApiModelProperty(value = "id")
    private Integer id;


    @ApiModelProperty("抽奖活动id")
    private Integer lotteryDrawId;


    @ApiModelProperty("奖品类型 1优惠券 2积分")
    private Integer prizeType;

    @ApiModelProperty("奖品名称")
    private String prizeName;

    @ApiModelProperty("奖品图片")
    private String prizeImgs;

    @ApiModelProperty("积分数")
    private Integer pointNum;


    @ApiModelProperty("优惠券id")
    private Long couponId;


    @ApiModelProperty("奖品库存")
    private Integer prizeStock;


    @ApiModelProperty("奖品初始库存")
    private Integer prizeInitStock;


    @ApiModelProperty("每人仅可中一次奖开关")
    private Integer onlyOneSwitch;


    @ApiModelProperty("阳光普照奖开关")
    private Integer sunshineAwardSwitch;


    @ApiModelProperty("大奖开关")
    private Integer bigAwardSwitch;


    @ApiModelProperty("大奖类型 1按每日发放 2按指定日期发放")
    private Integer bigAwardType;


    @ApiModelProperty("大奖指定日期集合")
    private String bigAwardTime;


    @ApiModelProperty("粉丝等级集合")
    private String fanLevels;

    @ApiModelProperty("奖品规则")
    private String prizeRule;
}
