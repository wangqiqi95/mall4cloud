package com.mall4j.cloud.user.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 客户标签VO
 *
 * @author YXF
 * @date 2020-12-08 10:16:53
 */
public class UserTagVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("自增id")
    private Long userTagId;

    @ApiModelProperty("标签名字")
    private String tagName;

    @ApiModelProperty("标签类型0手动1条件2系统")
    private Integer tagType;

    @ApiModelProperty("系统标签是否开启")
    private Integer isSysTurnOn;

    @ApiModelProperty("成为客户开始时间")
    private Date registerMinTime;

    @ApiModelProperty("成为客户结束时间")
    private Date registerMaxTime;

    @ApiModelProperty("关注开始时间")
    private Date subscribeMinTime;

    @ApiModelProperty("关注结束时间")
    private Date subscribeMaxTime;

    @ApiModelProperty("成为会员开始时间")
    private Date toBeMemberMinTime;

    @ApiModelProperty("成为会员结束时间")
    private Date toBeMemberMaxTime;

    @ApiModelProperty("最近消费时间范围0(不限)1(今天)2(最近7天)3(最近15天)4(最近30天)5(最近45天)6(最近60天)7(最近90天)8(最近180天)")
    private Integer recentPurchaseTime;

    @ApiModelProperty("消费次数时间范围0(不限)1(今天)2(最近7天)3(最近15天)4(最近30天)5(最近45天)6(最近60天)7(最近90天)8(最近180天)")
    private Integer purchaseNumTime;

    @ApiModelProperty("消费次数最小次数")
    private Long purchaseNumMinNum;

    @ApiModelProperty("消费次数最大次数")
    private Long purchaseNumMaxNum;

    @ApiModelProperty("消费金额时间范围0(不限)1(今天)2(最近7天)3(最近15天)4(最近30天)5(最近45天)6(最近60天)7(最近90天)8(最近180天)")
    private Integer purchaseAmountTime;

    @ApiModelProperty("消费金额最小金额")
    private Long purchaseAmountMinAmount;

    @ApiModelProperty("消费金额最大金额")
    private Long purchaseAmountMaxAmount;

    @ApiModelProperty("订单均价时间范围0(不限)1(今天)2(最近7天)3(最近15天)4(最近30天)5(最近45天)6(最近60天)7(最近90天)8(最近180天)")
    private Integer orderAveragePriceTime;

    @ApiModelProperty("订单均价最小金额")
    private Long orderAveragePriceMinAmount;

    @ApiModelProperty("订单均价最大金额")
    private Long orderAveragePriceMaxAmount;

    @ApiModelProperty("充值金额时间范围0(不限)1(今天)2(最近7天)3(最近15天)4(最近30天)5(最近45天)6(最近60天)7(最近90天)8(最近180天)")
    private Integer rechargeAmountTime;

    @ApiModelProperty("充值金额最小金额")
    private Long rechargeAmountMinAmount;

    @ApiModelProperty("充值金额最大金额")
    private Long rechargeAmountMaxAmount;

    @ApiModelProperty("充值次数时间范围0(不限)1(今天)2(最近7天)3(最近15天)4(最近30天)5(最近45天)6(最近60天)7(最近90天)8(最近180天)")
    private Integer rechargeNumTime;

    @ApiModelProperty("充值次数最小次数")
    private Long rechargeNumMinNum;

    @ApiModelProperty("充值次数最大次数")
    private Long rechargeNumMaxNum;

    @ApiModelProperty("符合标签的人数")
    private Long userNum;

    @ApiModelProperty("统计更新时间")
    private Date statisticUpdateTime;

	public Long getUserTagId() {
		return userTagId;
	}

	public void setUserTagId(Long userTagId) {
		this.userTagId = userTagId;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public Integer getTagType() {
		return tagType;
	}

	public void setTagType(Integer tagType) {
		this.tagType = tagType;
	}

	public Integer getIsSysTurnOn() {
		return isSysTurnOn;
	}

	public void setIsSysTurnOn(Integer isSysTurnOn) {
		this.isSysTurnOn = isSysTurnOn;
	}

	public Date getRegisterMinTime() {
		return registerMinTime;
	}

	public void setRegisterMinTime(Date registerMinTime) {
		this.registerMinTime = registerMinTime;
	}

	public Date getRegisterMaxTime() {
		return registerMaxTime;
	}

	public void setRegisterMaxTime(Date registerMaxTime) {
		this.registerMaxTime = registerMaxTime;
	}

	public Date getSubscribeMinTime() {
		return subscribeMinTime;
	}

	public void setSubscribeMinTime(Date subscribeMinTime) {
		this.subscribeMinTime = subscribeMinTime;
	}

	public Date getSubscribeMaxTime() {
		return subscribeMaxTime;
	}

	public void setSubscribeMaxTime(Date subscribeMaxTime) {
		this.subscribeMaxTime = subscribeMaxTime;
	}

	public Date getToBeMemberMinTime() {
		return toBeMemberMinTime;
	}

	public void setToBeMemberMinTime(Date toBeMemberMinTime) {
		this.toBeMemberMinTime = toBeMemberMinTime;
	}

	public Date getToBeMemberMaxTime() {
		return toBeMemberMaxTime;
	}

	public void setToBeMemberMaxTime(Date toBeMemberMaxTime) {
		this.toBeMemberMaxTime = toBeMemberMaxTime;
	}

	public Integer getRecentPurchaseTime() {
		return recentPurchaseTime;
	}

	public void setRecentPurchaseTime(Integer recentPurchaseTime) {
		this.recentPurchaseTime = recentPurchaseTime;
	}

	public Integer getPurchaseNumTime() {
		return purchaseNumTime;
	}

	public void setPurchaseNumTime(Integer purchaseNumTime) {
		this.purchaseNumTime = purchaseNumTime;
	}

	public Long getPurchaseNumMinNum() {
		return purchaseNumMinNum;
	}

	public void setPurchaseNumMinNum(Long purchaseNumMinNum) {
		this.purchaseNumMinNum = purchaseNumMinNum;
	}

	public Long getPurchaseNumMaxNum() {
		return purchaseNumMaxNum;
	}

	public void setPurchaseNumMaxNum(Long purchaseNumMaxNum) {
		this.purchaseNumMaxNum = purchaseNumMaxNum;
	}

	public Integer getPurchaseAmountTime() {
		return purchaseAmountTime;
	}

	public void setPurchaseAmountTime(Integer purchaseAmountTime) {
		this.purchaseAmountTime = purchaseAmountTime;
	}

	public Long getPurchaseAmountMinAmount() {
		return purchaseAmountMinAmount;
	}

	public void setPurchaseAmountMinAmount(Long purchaseAmountMinAmount) {
		this.purchaseAmountMinAmount = purchaseAmountMinAmount;
	}

	public Long getPurchaseAmountMaxAmount() {
		return purchaseAmountMaxAmount;
	}

	public void setPurchaseAmountMaxAmount(Long purchaseAmountMaxAmount) {
		this.purchaseAmountMaxAmount = purchaseAmountMaxAmount;
	}

	public Integer getOrderAveragePriceTime() {
		return orderAveragePriceTime;
	}

	public void setOrderAveragePriceTime(Integer orderAveragePriceTime) {
		this.orderAveragePriceTime = orderAveragePriceTime;
	}

	public Long getOrderAveragePriceMinAmount() {
		return orderAveragePriceMinAmount;
	}

	public void setOrderAveragePriceMinAmount(Long orderAveragePriceMinAmount) {
		this.orderAveragePriceMinAmount = orderAveragePriceMinAmount;
	}

	public Long getOrderAveragePriceMaxAmount() {
		return orderAveragePriceMaxAmount;
	}

	public void setOrderAveragePriceMaxAmount(Long orderAveragePriceMaxAmount) {
		this.orderAveragePriceMaxAmount = orderAveragePriceMaxAmount;
	}

	public Integer getRechargeAmountTime() {
		return rechargeAmountTime;
	}

	public void setRechargeAmountTime(Integer rechargeAmountTime) {
		this.rechargeAmountTime = rechargeAmountTime;
	}

	public Long getRechargeAmountMinAmount() {
		return rechargeAmountMinAmount;
	}

	public void setRechargeAmountMinAmount(Long rechargeAmountMinAmount) {
		this.rechargeAmountMinAmount = rechargeAmountMinAmount;
	}

	public Long getRechargeAmountMaxAmount() {
		return rechargeAmountMaxAmount;
	}

	public void setRechargeAmountMaxAmount(Long rechargeAmountMaxAmount) {
		this.rechargeAmountMaxAmount = rechargeAmountMaxAmount;
	}

	public Integer getRechargeNumTime() {
		return rechargeNumTime;
	}

	public void setRechargeNumTime(Integer rechargeNumTime) {
		this.rechargeNumTime = rechargeNumTime;
	}

	public Long getRechargeNumMinNum() {
		return rechargeNumMinNum;
	}

	public void setRechargeNumMinNum(Long rechargeNumMinNum) {
		this.rechargeNumMinNum = rechargeNumMinNum;
	}

	public Long getRechargeNumMaxNum() {
		return rechargeNumMaxNum;
	}

	public void setRechargeNumMaxNum(Long rechargeNumMaxNum) {
		this.rechargeNumMaxNum = rechargeNumMaxNum;
	}

	public Long getUserNum() {
		return userNum;
	}

	public void setUserNum(Long userNum) {
		this.userNum = userNum;
	}

	public Date getStatisticUpdateTime() {
		return statisticUpdateTime;
	}

	public void setStatisticUpdateTime(Date statisticUpdateTime) {
		this.statisticUpdateTime = statisticUpdateTime;
	}

	@Override
	public String toString() {
		return "UserTagVO{" +
				"userTagId=" + userTagId +
				", tagName='" + tagName + '\'' +
				", tagType=" + tagType +
				", isSysTurnOn=" + isSysTurnOn +
				", registerMinTime=" + registerMinTime +
				", registerMaxTime=" + registerMaxTime +
				", subscribeMinTime=" + subscribeMinTime +
				", subscribeMaxTime=" + subscribeMaxTime +
				", toBeMemberMinTime=" + toBeMemberMinTime +
				", toBeMemberMaxTime=" + toBeMemberMaxTime +
				", recentPurchaseTime=" + recentPurchaseTime +
				", purchaseNumTime=" + purchaseNumTime +
				", purchaseNumMinNum=" + purchaseNumMinNum +
				", purchaseNumMaxNum=" + purchaseNumMaxNum +
				", purchaseAmountTime=" + purchaseAmountTime +
				", purchaseAmountMinAmount=" + purchaseAmountMinAmount +
				", purchaseAmountMaxAmount=" + purchaseAmountMaxAmount +
				", orderAveragePriceTime=" + orderAveragePriceTime +
				", orderAveragePriceMinAmount=" + orderAveragePriceMinAmount +
				", orderAveragePriceMaxAmount=" + orderAveragePriceMaxAmount +
				", rechargeAmountTime=" + rechargeAmountTime +
				", rechargeAmountMinAmount=" + rechargeAmountMinAmount +
				", rechargeAmountMaxAmount=" + rechargeAmountMaxAmount +
				", rechargeNumTime=" + rechargeNumTime +
				", rechargeNumMinNum=" + rechargeNumMinNum +
				", rechargeNumMaxNum=" + rechargeNumMaxNum +
				", userNum=" + userNum +
				", statisticUpdateTime=" + statisticUpdateTime +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				'}';
	}
}
