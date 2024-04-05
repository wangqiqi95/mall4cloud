package com.mall4j.cloud.group.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 限时调价活动VO
 *
 * @author FrozenWatermelon
 * @date 2022-03-10 01:55:13
 */
@Data
public class TimeLimitedDiscountActivityVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Integer id;

    @ApiModelProperty("活动名称")
    private String activityName;

    @ApiModelProperty("活动开始时间")
    private Date activityBeginTime;

    @ApiModelProperty("活动结束时间")
    private Date activityEndTime;

    @ApiModelProperty("是否全部门店 0 否 1是")
    private Integer isAllShop;

    @ApiModelProperty("适用门店数")
    private Integer applyShopNum;

    @ApiModelProperty("权重")
    private Integer weight;

    @ApiModelProperty("状态 0 未启用 1已启用")
    private Integer status;

    @ApiModelProperty("")
    private Integer deleted;

    @ApiModelProperty("创建人id")
    private Long createUserId;

    @ApiModelProperty("创建人名称")
    private String createUserName;

    @ApiModelProperty("更新人id")
    private Long updateUserId;

    @ApiModelProperty("更新人名称")
    private String updateUserName;

    @ApiModelProperty("审核状态：0待审核 1审核通过 2驳回")
    private Integer checkStatus;

    @ApiModelProperty("spu优惠列表")
    private List<TimeLimitedDiscountSpuVO> spus;

    @ApiModelProperty("spu优惠列表")
    private List<TimeLimitedDiscountShopVO> shops;

    @ApiModelProperty("类型1，限时调价。2，会员日活动调价 3，虚拟门店价")
    private Integer type;

    @ApiModelProperty("是否允许同时使用优惠券0否1是")
    private Integer canUseCoupon;

    @ApiModelProperty("是否同时参加满减则活动0否1是")
    private Integer canUseDiscount;

    //在会员日活动中是否可以使用优惠券标识 0否1是
    private Integer friendlyCouponUseFlag = 1;

    //在会员日活动中是否可以参加满减活动标识 0否1是
    private Integer friendlyDiscountFlag = 1;

}
