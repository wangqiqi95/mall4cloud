package com.mall4j.cloud.coupon.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel("coupon_pack_activity")
public class CouponPackActivity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * id
     */
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

    /**
     * 适用门店集合 -1为全部门店
     */
    @ApiModelProperty("0否 1是")
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

    @ApiModelProperty("每人累计限制次数")
    private Integer receiveTimes;
    @ApiModelProperty("每人每天限制次数")
    private Integer dayLimit;

    /**
     * 活动状态
     */
    @ApiModelProperty("活动状态")
    private Integer status;

    /**
     * 删除标记
     */
    @ApiModelProperty("删除标记")
    private Integer deleted;

    /**
     * create_time
     */
    @ApiModelProperty("create_time")
    private Date createTime;

    /**
     * create_user_name
     */
    @ApiModelProperty("create_user_name")
    private String createUserName;

    /**
     * update_time
     */
    @ApiModelProperty("update_time")
    private Date updateTime;

    /**
     * update_user_name
     */
    @ApiModelProperty("update_user_name")
    private String updateUserName;

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
