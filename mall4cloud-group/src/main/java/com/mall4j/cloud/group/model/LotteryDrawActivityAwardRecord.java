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
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "lottery_draw_activity_award_record")
public class LotteryDrawActivityAwardRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)

    @ApiModelProperty("id")
    private Integer id;


    @ApiModelProperty("抽奖活动id")
    private Integer lotteryDrawId;

    @ApiModelProperty("抽奖活动奖品id")
    private Integer lotteryDrawPrizeId;

    @ApiModelProperty("奖品名称")
    private String prizeName;

    @ApiModelProperty("用户id")
    private Long userId;


    @ApiModelProperty("用户昵称")
    private String nickname;


    @ApiModelProperty("用户手机号")
    private String mobile;


    @ApiModelProperty("门店id")
    private Long shopId;


    @ApiModelProperty("门店名称")
    private String shopName;


    @ApiModelProperty("中奖类型 0 未中奖 1优惠券 2积分 3 实物")
    private Integer awardType;


    @ApiModelProperty("积分数")
    private Integer pointNum;


    @ApiModelProperty("优惠券id")
    private Long couponId;


    @ApiModelProperty("中奖时间")
    private Date awardTime;
    @ApiModelProperty("中奖状态，0：待领取，1：待发放，2：已发放")
    private Integer status;
    @ApiModelProperty("用户收货地址")
    private String userAddr;
    @ApiModelProperty("用户手机号码")
    private String phone;
    @ApiModelProperty("发货物流单号")
    private String logisticsNo;
    @ApiModelProperty("发货物流公司")
    private String company;
    @ApiModelProperty("省ID")
    private Long provinceId;;
    @ApiModelProperty("城市ID")
    private Long cityId;
    @ApiModelProperty("区ID")
    private Long areaId;
    @ApiModelProperty("用户手机号码")
    private String userName;
    @ApiModelProperty("用户所在地区")
    private String addr;
}
