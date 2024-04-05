package com.mall4j.cloud.coupon.vo;

import com.mall4j.cloud.coupon.model.CouponPackShop;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("券包详情实体")
public class CouponPackVO implements Serializable {
    @ApiModelProperty("id")
    private Integer id;

    /**
     * 活动名称
     */
    @ApiModelProperty("活动名称")
    private String activityName;

    /**
     * 活动开始时间
     */
    @ApiModelProperty("活动开始时间")
    private Date activityBeginTime;

    /**
     * 活动截止时间
     */
    @ApiModelProperty("活动截止时间")
    private Date activityEndTime;

    @ApiModelProperty("是否全部门店")
    private Integer isAllShop;
    /**
     * 活动banner地址
     */
    @ApiModelProperty("活动banner地址")
    private String activityBannerUrl;

    /**
     * 初始优惠券包库存
     */
    @ApiModelProperty("初始优惠券包库存")
    private Integer initialCouponStock;

    /**
     * 当前优惠券包库存
     */
    @ApiModelProperty("当前优惠券包库存")
    private Integer curCouponStock;

    /**
     * 优惠券集合
     */
    @ApiModelProperty("优惠券集合")
    private String couponIds;

    /**
     * 描述
     */
    @ApiModelProperty("描述")
    private String description;

    /**
     * 权重
     */
    @ApiModelProperty("权重")
    private Integer weight;

    /**
     * 活动状态
     */
    @ApiModelProperty("活动状态")
    private Integer status;

    @ApiModelProperty("每人限制领取次数")
    private Integer receiveTimes;
    @ApiModelProperty("每人每天限制领取次数")
    private Integer dayLimit;

    @ApiModelProperty("适用门店")
    private List<CouponPackShop> couponPackShops;

    // 消费限制
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

    @ApiModelProperty("粉丝等级集合")
    private String fanLevels;

    @ApiModelProperty("粉丝等级不足提示")
    private String fanTips;

    @ApiModelProperty("指定消费门店")
    private String appointShop;

}
