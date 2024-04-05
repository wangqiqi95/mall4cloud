package com.mall4j.cloud.group.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "抽奖活动统计实体")
public class LotteryDrawActivityCensusVO implements Serializable {
    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("活动名称")
    private String activityName;
    @ApiModelProperty("游戏类型 1 砸金蛋 2摇一摇 3 0元抽奖 4 大转盘 5刮刮乐")
    private Integer gameType;
    @ApiModelProperty("活动开始时间")
    private Date activityBeginTime;
    @ApiModelProperty("活动截止时间")
    private Date activityEndTime;
    @ApiModelProperty("奖品发放总量 1 按日限制发送总量 2 按累计总量限制发送总量")
    private Integer prizeGrantTotal;
    @ApiModelProperty("抽奖人数")
    private int drawNum;
    @ApiModelProperty("中奖人数")
    private int awardNum;
    @ApiModelProperty("抽奖次数")
    private int drawTimes;
    private List<PrizeCensusVO> prizes;
}
