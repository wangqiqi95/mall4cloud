package com.mall4j.cloud.group.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "lottery_draw_activity_game")
public class LotteryDrawActivityGame implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)

    @ApiModelProperty("id")
    private Integer id;


    @ApiModelProperty("抽奖活动id")
    private Integer lotteryDrawId;


    @ApiModelProperty("游戏类型 1 砸金蛋 2摇一摇 3 0元抽奖 4 大转盘 5刮刮乐")
    private Integer gameType;

    @ApiModelProperty("游戏背景图")
    private String gameBackgroundPic;


    @ApiModelProperty("活动规则按钮图")
    private String activityRulePic;


    @ApiModelProperty("查看我的奖品按钮图")
    private String showMyPrizePic;


    @ApiModelProperty("跑马灯展示中奖记录开关")
    private String showAwardRecord;


    @ApiModelProperty("抽奖按钮图片")
    private String drawBtnPic;


    @ApiModelProperty("抽奖背景图（如：刮奖背景图 转盘背景图）")
    private String drawBackgroundPic;


    @ApiModelProperty("弹框自定义图片")
    private String popUpPic;

    @ApiModelProperty("中奖弹框自定义图片")
    private String winPopUpPic;

    @ApiModelProperty("未中奖弹框自定义图片")
    private String failPopUpPic;

    @ApiModelProperty("领取按钮背景自定义图片")
    private String getButtonBackgroundPic;

    @ApiModelProperty("刮奖区（渡灰区域）自定义图片")
    private String scratchOffPic;
}
