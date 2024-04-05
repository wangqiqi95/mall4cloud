package com.mall4j.cloud.group.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
@ApiModel("用户签到奖励实体")
@TableName(value = "user_sign_reward")
public class UserSignReward implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * id
     */
    @ApiModelProperty("id")
    private Long id;

    private Integer activityId;
    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 签到时间
     */
    @ApiModelProperty("签到时间")
    private Date signTime;

    /**
     * 用户手机号
     */
    @ApiModelProperty("用户手机号")
    private String mobile;

    /**
     * 门店id
     */
    @ApiModelProperty("门店id")
    private Long shopId;

    /**
     * 门店名称
     */
    @ApiModelProperty("门店名称")
    private String shopName;

    /**
     * 门店编码
     */
    @ApiModelProperty("门店编码")
    private String shopCode;

    /**
     * 赠送积分数
     */
    @ApiModelProperty("赠送积分数")
    private Integer pointNum;

    /**
     * 赠送优惠券id
     */
    @ApiModelProperty("赠送优惠券id")
    private Long couponId;

    /**
     * 赠送优惠券名称
     */
    @ApiModelProperty("赠送优惠券名称")
    private String couponName;

    /**
     * 1 常规签到奖励 2连续签到奖励
     */
    @ApiModelProperty("1 常规签到奖励 2连续签到奖励")
    private Integer signType;

    @ApiModelProperty("连续签到天数")
    private Integer seriesSignDay;
    /**
     * create_time
     */
    @ApiModelProperty("create_time")
    private Date createTime;

    /**
     * update_time
     */
    @ApiModelProperty("update_time")
    private Date updateTime;

}
