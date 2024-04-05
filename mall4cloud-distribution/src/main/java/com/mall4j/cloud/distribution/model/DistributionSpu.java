package com.mall4j.cloud.distribution.model;

import java.io.Serializable;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 分销商品关联信息
 *
 * @author cl
 * @date 2021-08-09 14:14:07
 */
public class DistributionSpu extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 分销商品表
     */
    private Long distributionSpuId;

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 商品id
     */
    private Long spuId;

    /**
     * 状态(0:商家下架 1:商家上架 2:违规下架 3:平台审核)
     */
    private Integer state;

    /**
     * 奖励方式(0 按比例 1 按固定数值)
     */
    private Integer awardMode;

    /**
     * 上级奖励设置(0 关闭 1开启)
     */
    private Integer parentAwardSet;

	/**
	 * 奖励数额(奖励比例为0时，代表百分比*100，为1时代表实际奖励金额*100）
	 * 例如： 奖励比例为1%，awardNumbers = 100 ; awardNumbers = 1 则实际的奖励比例为 0.01%
	 */
	private Long awardNumbers;

	/**
	 * 上级奖励数额(奖励比例为0时，表示百分比*100，为1时代表实际奖励金额*100）
	 * 例如： 奖励比例为1%，parentAwardNumbers = 100 ; parentAwardNumbers = 1 则实际的奖励比例为 0.01%
	 */
	private Long parentAwardNumbers;

    /**
     * 操作人id
     */
    private Long modifier;

	public Long getDistributionSpuId() {
		return distributionSpuId;
	}

	public void setDistributionSpuId(Long distributionSpuId) {
		this.distributionSpuId = distributionSpuId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Long getSpuId() {
		return spuId;
	}

	public void setSpuId(Long spuId) {
		this.spuId = spuId;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getAwardMode() {
		return awardMode;
	}

	public void setAwardMode(Integer awardMode) {
		this.awardMode = awardMode;
	}

	public Integer getParentAwardSet() {
		return parentAwardSet;
	}

	public void setParentAwardSet(Integer parentAwardSet) {
		this.parentAwardSet = parentAwardSet;
	}

	public Long getModifier() {
		return modifier;
	}

	public void setModifier(Long modifier) {
		this.modifier = modifier;
	}

	public Long getAwardNumbers() {
		return awardNumbers;
	}

	public void setAwardNumbers(Long awardNumbers) {
		this.awardNumbers = awardNumbers;
	}

	public Long getParentAwardNumbers() {
		return parentAwardNumbers;
	}

	public void setParentAwardNumbers(Long parentAwardNumbers) {
		this.parentAwardNumbers = parentAwardNumbers;
	}

	@Override
	public String toString() {
		return "DistributionSpu{" +
				"createTime=" + createTime +
				", updateTime=" + updateTime +
				", distributionSpuId=" + distributionSpuId +
				", shopId=" + shopId +
				", spuId=" + spuId +
				", state=" + state +
				", awardMode=" + awardMode +
				", parentAwardSet=" + parentAwardSet +
				", awardNumbers=" + awardNumbers +
				", parentAwardNumbers=" + parentAwardNumbers +
				", modifier=" + modifier +
				'}';
	}
}
