package com.mall4j.cloud.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 客户标签DTO
 *
 * @author YXF
 * @date 2020-12-08 10:16:53
 */
public class UserTagDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	/**
	 * 新增标签校验
	 */
	public interface AddUserTag {
	}

	/**
	 * 修改标签校验
	 */
	public interface UpdateUserTag {
	}

    @ApiModelProperty("自增id")
	@NotNull(message = "自增id不能为空", groups = UpdateUserTag.class)
    private Long userTagId;

    @ApiModelProperty("标签名字")
	@NotBlank(message = "标签名字不能为空", groups = {AddUserTag.class, UpdateUserTag.class })
    private String tagName;

    @ApiModelProperty("标签类型0手动1条件")
	@Max(value = 1, message = "只能为0或1", groups = AddUserTag.class)
	@Min(value = 0, message = "只能为0或1", groups = AddUserTag.class)
	@NotNull(message = "标签类型不能为空。", groups = AddUserTag.class)
    private Integer tagType;

    @ApiModelProperty("系统标签是否开启")
	@Max(value = 1, message = "只能为0或1", groups = UpdateUserTag.class)
	@Min(value = 0, message = "只能为0或1", groups = UpdateUserTag.class)
    private Integer isSysTurnOn;

    @ApiModelProperty("成为客户开始时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date registerMinTime;

    @ApiModelProperty("成为客户结束时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date registerMaxTime;

	@ApiModelProperty(value = "清空成为客户时间标记")
	private Boolean clearRegisterTime;

    @ApiModelProperty("关注开始时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date subscribeMinTime;

    @ApiModelProperty("关注结束时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date subscribeMaxTime;

	@ApiModelProperty(value = "清空关注时间标记")
	private Boolean clearSubscribeTime;

    @ApiModelProperty("成为会员开始时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date toBeMemberMinTime;

    @ApiModelProperty("成为会员结束时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date toBeMemberMaxTime;

	@ApiModelProperty(value = "清空成为会员时间标记")
	private Boolean clearToBeMemberTime;

    @ApiModelProperty("最近消费时间范围0(不限)1(今天)2(最近7天)3(最近15天)4(最近30天)5(最近45天)6(最近60天)7(最近90天)8(最近180天)")
	@Max(value = 8, message = "最大值为8", groups = {AddUserTag.class, UpdateUserTag.class})
	@Min(value = 0, message = "最小值为0", groups = {AddUserTag.class, UpdateUserTag.class})
    private Integer recentPurchaseTime;

	@ApiModelProperty(value = "清空成为会员时间标记")
	private Boolean clearRecentPurchaseTime;

    @ApiModelProperty("消费次数时间范围0(不限)1(今天)2(最近7天)3(最近15天)4(最近30天)5(最近45天)6(最近60天)7(最近90天)8(最近180天)")
	@Max(value = 8, message = "最大值为8", groups = {AddUserTag.class, UpdateUserTag.class})
	@Min(value = 0, message = "最小值为0", groups = {AddUserTag.class, UpdateUserTag.class})
    private Integer purchaseNumTime;

    @ApiModelProperty("消费次数最小次数")
	@Max(value = 100000, message = "最大值为100000", groups = {AddUserTag.class, UpdateUserTag.class})
	@Min(value = 0, message = "最小值为0", groups = {AddUserTag.class, UpdateUserTag.class})
    private Long purchaseNumMinNum;

    @ApiModelProperty("消费次数最大次数")
	@Max(value = 100000, message = "最大值为100000", groups = {AddUserTag.class, UpdateUserTag.class})
	@Min(value = 0, message = "最小值为0", groups = {AddUserTag.class, UpdateUserTag.class})
    private Long purchaseNumMaxNum;

	@ApiModelProperty(value = "清空消费次数标记")
	private Boolean clearPurchaseNum;

    @ApiModelProperty("消费金额时间范围0(不限)1(今天)2(最近7天)3(最近15天)4(最近30天)5(最近45天)6(最近60天)7(最近90天)8(最近180天)")
	@Max(value = 8, message = "最大值为8", groups = {AddUserTag.class, UpdateUserTag.class})
	@Min(value = 0, message = "最小值为0", groups = {AddUserTag.class, UpdateUserTag.class})
    private Integer purchaseAmountTime;

    @ApiModelProperty("消费金额最小金额")
	@DecimalMax(value = "100000000", message = "最大值为100000000", groups = {AddUserTag.class, UpdateUserTag.class})
	@DecimalMin(value = "0", message = "最小值为0", groups = {AddUserTag.class, UpdateUserTag.class})
    private BigDecimal purchaseAmountMinAmount;

    @ApiModelProperty("消费金额最大金额")
	@DecimalMax(value = "100000000", message = "最大值为100000000", groups = {AddUserTag.class, UpdateUserTag.class})
	@DecimalMin(value = "0", message = "最小值为0", groups = {AddUserTag.class, UpdateUserTag.class})
    private BigDecimal purchaseAmountMaxAmount;

	@ApiModelProperty(value = "清空消费次数标记")
	private Boolean clearPurchaseAmount;

    @ApiModelProperty("订单均价时间范围0(不限)1(今天)2(最近7天)3(最近15天)4(最近30天)5(最近45天)6(最近60天)7(最近90天)8(最近180天)")
	@Max(value = 8, message = "最大值为8", groups = {AddUserTag.class, UpdateUserTag.class})
	@Min(value = 0, message = "最小值为0", groups = {AddUserTag.class, UpdateUserTag.class})
    private Integer orderAveragePriceTime;

    @ApiModelProperty("订单均价最小金额")
	@DecimalMax(value = "100000000", message = "最大值为100000000", groups = {AddUserTag.class, UpdateUserTag.class})
	@DecimalMin(value = "0", message = "最小值为0", groups = {AddUserTag.class, UpdateUserTag.class})
    private BigDecimal orderAveragePriceMinAmount;

    @ApiModelProperty("订单均价最大金额")
	@DecimalMax(value = "100000000", message = "最大值为100000000", groups = {AddUserTag.class, UpdateUserTag.class})
	@DecimalMin(value = "0", message = "最小值为0", groups = {AddUserTag.class, UpdateUserTag.class})
    private BigDecimal orderAveragePriceMaxAmount;

	@ApiModelProperty(value = "清空订单均价标记")
	private Boolean clearOrderAveragePrice;

    @ApiModelProperty("充值金额时间范围0(不限)1(今天)2(最近7天)3(最近15天)4(最近30天)5(最近45天)6(最近60天)7(最近90天)8(最近180天)")
	@Max(value = 8, message = "最大值为8", groups = {AddUserTag.class, UpdateUserTag.class})
	@Min(value = 0, message = "最小值为0", groups = {AddUserTag.class, UpdateUserTag.class})
    private Integer rechargeAmountTime;

    @ApiModelProperty("充值金额最小金额")
	@DecimalMax(value = "100000000", message = "最大值为100000000", groups = {AddUserTag.class, UpdateUserTag.class})
	@DecimalMin(value = "0", message = "最小值为0", groups = {AddUserTag.class, UpdateUserTag.class})
    private Long rechargeAmountMinAmount;

    @ApiModelProperty("充值金额最大金额")
	@DecimalMax(value = "100000000", message = "最大值为100000000", groups = {AddUserTag.class, UpdateUserTag.class})
	@DecimalMin(value = "0", message = "最小值为0", groups = {AddUserTag.class, UpdateUserTag.class})
    private Long rechargeAmountMaxAmount;

	@ApiModelProperty(value = "清空充值金额标记")
	private Boolean clearRechargeAmount;

    @ApiModelProperty("充值次数时间范围0(不限)1(今天)2(最近7天)3(最近15天)4(最近30天)5(最近45天)6(最近60天)7(最近90天)8(最近180天)")
	@Max(value = 8, message = "最大值为8", groups = {AddUserTag.class, UpdateUserTag.class})
	@Min(value = 0, message = "最小值为0", groups = {AddUserTag.class, UpdateUserTag.class})
    private Integer rechargeNumTime;

    @ApiModelProperty("充值次数最小次数")
	@Max(value = 100000, message = "最大值为100000", groups = {AddUserTag.class, UpdateUserTag.class})
	@Min(value = 0, message = "最小值为0", groups = {AddUserTag.class, UpdateUserTag.class})
    private Long rechargeNumMinNum;

    @ApiModelProperty("充值次数最大次数")
	@Max(value = 100000, message = "最大值为100000", groups = {AddUserTag.class, UpdateUserTag.class})
	@Min(value = 0, message = "最小值为0", groups = {AddUserTag.class, UpdateUserTag.class})
    private Long rechargeNumMaxNum;

	@ApiModelProperty(value = "清空充值次数标记")
	private Boolean clearRechargeNum;

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

	public BigDecimal getPurchaseAmountMinAmount() {
		return purchaseAmountMinAmount;
	}

	public void setPurchaseAmountMinAmount(BigDecimal purchaseAmountMinAmount) {
		this.purchaseAmountMinAmount = purchaseAmountMinAmount;
	}

	public BigDecimal getPurchaseAmountMaxAmount() {
		return purchaseAmountMaxAmount;
	}

	public void setPurchaseAmountMaxAmount(BigDecimal purchaseAmountMaxAmount) {
		this.purchaseAmountMaxAmount = purchaseAmountMaxAmount;
	}

	public Integer getOrderAveragePriceTime() {
		return orderAveragePriceTime;
	}

	public void setOrderAveragePriceTime(Integer orderAveragePriceTime) {
		this.orderAveragePriceTime = orderAveragePriceTime;
	}

	public BigDecimal getOrderAveragePriceMinAmount() {
		return orderAveragePriceMinAmount;
	}

	public void setOrderAveragePriceMinAmount(BigDecimal orderAveragePriceMinAmount) {
		this.orderAveragePriceMinAmount = orderAveragePriceMinAmount;
	}

	public BigDecimal getOrderAveragePriceMaxAmount() {
		return orderAveragePriceMaxAmount;
	}

	public void setOrderAveragePriceMaxAmount(BigDecimal orderAveragePriceMaxAmount) {
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

	public Boolean getClearRegisterTime() {
		return clearRegisterTime;
	}

	public void setClearRegisterTime(Boolean clearRegisterTime) {
		this.clearRegisterTime = clearRegisterTime;
	}

	public Boolean getClearSubscribeTime() {
		return clearSubscribeTime;
	}

	public void setClearSubscribeTime(Boolean clearSubscribeTime) {
		this.clearSubscribeTime = clearSubscribeTime;
	}

	public Boolean getClearToBeMemberTime() {
		return clearToBeMemberTime;
	}

	public void setClearToBeMemberTime(Boolean clearToBeMemberTime) {
		this.clearToBeMemberTime = clearToBeMemberTime;
	}

	public Boolean getClearRecentPurchaseTime() {
		return clearRecentPurchaseTime;
	}

	public void setClearRecentPurchaseTime(Boolean clearRecentPurchaseTime) {
		this.clearRecentPurchaseTime = clearRecentPurchaseTime;
	}

	public Boolean getClearPurchaseNum() {
		return clearPurchaseNum;
	}

	public void setClearPurchaseNum(Boolean clearPurchaseNum) {
		this.clearPurchaseNum = clearPurchaseNum;
	}

	public Boolean getClearPurchaseAmount() {
		return clearPurchaseAmount;
	}

	public void setClearPurchaseAmount(Boolean clearPurchaseAmount) {
		this.clearPurchaseAmount = clearPurchaseAmount;
	}

	public Boolean getClearOrderAveragePrice() {
		return clearOrderAveragePrice;
	}

	public void setClearOrderAveragePrice(Boolean clearOrderAveragePrice) {
		this.clearOrderAveragePrice = clearOrderAveragePrice;
	}

	public Boolean getClearRechargeAmount() {
		return clearRechargeAmount;
	}

	public void setClearRechargeAmount(Boolean clearRechargeAmount) {
		this.clearRechargeAmount = clearRechargeAmount;
	}

	public Boolean getClearRechargeNum() {
		return clearRechargeNum;
	}

	public void setClearRechargeNum(Boolean clearRechargeNum) {
		this.clearRechargeNum = clearRechargeNum;
	}

	@Override
	public String toString() {
		return "UserTagDTO{" +
				"userTagId=" + userTagId +
				", tagName='" + tagName + '\'' +
				", tagType=" + tagType +
				", isSysTurnOn=" + isSysTurnOn +
				", registerMinTime=" + registerMinTime +
				", registerMaxTime=" + registerMaxTime +
				", clearRegisterTime=" + clearRegisterTime +
				", subscribeMinTime=" + subscribeMinTime +
				", subscribeMaxTime=" + subscribeMaxTime +
				", clearSubscribeTime=" + clearSubscribeTime +
				", toBeMemberMinTime=" + toBeMemberMinTime +
				", toBeMemberMaxTime=" + toBeMemberMaxTime +
				", clearToBeMemberTime=" + clearToBeMemberTime +
				", recentPurchaseTime=" + recentPurchaseTime +
				", clearRecentPurchaseTime=" + clearRecentPurchaseTime +
				", purchaseNumTime=" + purchaseNumTime +
				", purchaseNumMinNum=" + purchaseNumMinNum +
				", purchaseNumMaxNum=" + purchaseNumMaxNum +
				", clearPurchaseNum=" + clearPurchaseNum +
				", purchaseAmountTime=" + purchaseAmountTime +
				", purchaseAmountMinAmount=" + purchaseAmountMinAmount +
				", purchaseAmountMaxAmount=" + purchaseAmountMaxAmount +
				", clearPurchaseAmount=" + clearPurchaseAmount +
				", orderAveragePriceTime=" + orderAveragePriceTime +
				", orderAveragePriceMinAmount=" + orderAveragePriceMinAmount +
				", orderAveragePriceMaxAmount=" + orderAveragePriceMaxAmount +
				", clearOrderAveragePrice=" + clearOrderAveragePrice +
				", rechargeAmountTime=" + rechargeAmountTime +
				", rechargeAmountMinAmount=" + rechargeAmountMinAmount +
				", rechargeAmountMaxAmount=" + rechargeAmountMaxAmount +
				", clearRechargeAmount=" + clearRechargeAmount +
				", rechargeNumTime=" + rechargeNumTime +
				", rechargeNumMinNum=" + rechargeNumMinNum +
				", rechargeNumMaxNum=" + rechargeNumMaxNum +
				", clearRechargeNum=" + clearRechargeNum +
				", userNum=" + userNum +
				", statisticUpdateTime=" + statisticUpdateTime +
				'}';
	}
}
