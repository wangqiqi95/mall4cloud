package com.mall4j.cloud.group.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(value = "中奖记录实体")
public class LotteryAwardRecordVO implements Serializable {
    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("奖品名称")
    private String prizeName;
    @ApiModelProperty("抽奖活动id")
    private Integer lotteryDrawId;
    @ApiModelProperty("优惠券Id")
    private Long couponId;
    @ApiModelProperty("用户id")
    private Long userId;
    @ApiModelProperty("优惠券码")
    private String couponCode;
    @ApiModelProperty("优惠券状态")
    private Integer couponStatus;
    @ApiModelProperty("用户昵称")
    private String nickname;
    @ApiModelProperty("用户手机号")
    private String mobile;
    @ApiModelProperty("获取奖励时间")
    private Date awardTime;
    @ApiModelProperty("门店名称")
    private String shopName;
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
    private String addr;
    private Integer awardType;
}
