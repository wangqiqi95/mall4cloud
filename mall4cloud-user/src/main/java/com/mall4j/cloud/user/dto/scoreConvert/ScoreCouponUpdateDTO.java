package com.mall4j.cloud.user.dto.scoreConvert;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 积分兑换
 *
 * @author shijing
 * @date 2021-12-10 18:07:21
 */

@Data
@ApiModel(description = "新增积分换券参数")
public class ScoreCouponUpdateDTO {

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    private String convertId;

    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String convertTitle;

    /**
     * 积分兑换数
     */
    @ApiModelProperty(value = "积分兑换数")
    private Long convertScore;

    @ApiModelProperty(value = "封面")
    private String convertUrl;

    /**
     * 限制兑换总数
     */
    @ApiModelProperty(value = "限制兑换总数")
    private Long maxAmount;

    /**
     * 每人限制兑换数
     */
    @ApiModelProperty(value = "每人限制兑换数")
    private Long personMaxAmount;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String description;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Long sort;

    /**
     * 优惠券id
     */
    @ApiModelProperty(value = "优惠券id集合")
    private List<Long> couponIds;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    /**
     * 是否全部门店
     */
    @ApiModelProperty(value = "是否全部门店（适用门店）")
    private Boolean isAllShop;

    /**
     * 适用的门店
     */
    @ApiModelProperty(value = "适用门店")
    private List<Long> shops;

    /**
     * 是否全部门店（优惠券）
     */
    @ApiModelProperty(value = "是否全部门店（优惠券）")
    private Boolean isAllCouponShop;

    /**
     * 适用的门店
     */
    @ApiModelProperty(value = "优惠券门店")
    private List<Long> couponShops;

    /**
     * 禁用人员
     */
    @ApiModelProperty(value = "禁用人员")
    private List<String> phoneNums;

    /**
     * 积分换券活动类型（0：积分兑礼/1：积分换券）
     */
    @ApiModelProperty(value = "积分换券活动类型（0：积分兑礼/1：积分兑券）")
    private Short type;

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

    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    private String commodityName;

    /**
     * 商品图片
     */
    @ApiModelProperty("商品图片")
    private String commodityImgUrl;
    /**
     * 礼品描述
     */
    @ApiModelProperty("礼品描述")
    private String commodityDes;

    @ApiModelProperty("指定消费门店")
    private String appointShop;

    /**
     * 状态 0：启用/1：停用
     */
    @ApiModelProperty(value = "状态 0：启用/1：停用")
    private Integer convertStatus;

    @ApiModelProperty("c端是否展示 0-展示 ，1 -不展示")
    private Integer isShow;

    @ApiModelProperty("活动开始前x小时提醒")
    private Integer newRemind;
    //    @ApiModelProperty("活动开始前提醒时间")
//    private Date newRemindTime;
    @ApiModelProperty("是否发送积分上新订阅消息 0 否 1是")
    private Integer newNotice;
    @ApiModelProperty("积分上新温馨提示")
    private String newReminder;
}
