package com.mall4j.cloud.group.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("支付活动实体")
public class PayActivity implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("id")
    private Integer id;

    /**
     * 活动名称
     */
    @ApiModelProperty("活动名称")
    private String activityName;

    /**
     * 活动图片url
     */
    @ApiModelProperty("活动图片url")
    private String activityPicUrl;

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
     * 适用门店id集合 -1为全部门店
     */
    @ApiModelProperty("是否全部门店 0否 1是")
    private Integer isAllShop;

    /**
     * 消费金额
     */
    @ApiModelProperty("消费金额")
    private BigDecimal consumptionAmount;

    /**
     * 适用商品范围
     */
    @ApiModelProperty("适用商品范围")
    private String applyCommodityIds;

    /**
     * 每人每天最大参与订单数
     */
    @ApiModelProperty("每人每天最大参与订单数")
    private Integer dayMaxOrderNum;

    /**
     * 订单最低累计数
     */
    @ApiModelProperty("订单最低累计数")
    private Integer minOrderTotalNum;

    /**
     * 赠送积分开关
     */
    @ApiModelProperty("赠送积分开关")
    private Integer activityPointSwitch;

    /**
     * 赠送积分数
     */
    @ApiModelProperty("赠送积分数")
    private Integer activityPointNumber;

    /**
     * 赠送优惠券开关
     */
    @ApiModelProperty("赠送优惠券开关")
    private Integer activityCouponSwitch;

    /**
     * 赠送优惠券id
     */
    @ApiModelProperty("赠送优惠券id")
    private String activityCouponId;

    private Integer deleted;
    /**
     * 权重
     */
    @ApiModelProperty("权重")
    private Integer weight;

    /**
     * 活动状态 0 禁用 1启用
     */
    @ApiModelProperty("活动状态 0 禁用 1启用")
    private Integer status;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 创建人id
     */
    @ApiModelProperty("创建人id")
    private Long createUserId;

    /**
     * 创建人名称
     */
    @ApiModelProperty("创建人名称")
    private String createUserName;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;

    /**
     * 更新人id
     */
    @ApiModelProperty("更新人id")
    private Long updateUserId;

    /**
     * 更新人名称
     */
    @ApiModelProperty("更新人名称")
    private String updateUserName;
}
