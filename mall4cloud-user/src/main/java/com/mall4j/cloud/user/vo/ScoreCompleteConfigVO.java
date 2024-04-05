

package com.mall4j.cloud.user.vo;


import java.util.List;

/**
 * 积分配置信息
 * @author lhd
 */
public class ScoreCompleteConfigVO {

	/**
	 * 签到获取积分
	 */
	private String signInScoreString;

	/**
	 * 签到获取积分
	 */
	private List<Integer> signInScore;

	/**
	 * 注册获取积分
	 */
	private Long registerScore;

	/**
	 * 购物开关
	 */
	private Boolean shoppingScoreSwitch;

	/**
	 * 购物获取积分(x元获取1积分)
	 */
	private Long shoppingGetScore;

	/**
	 * 购物使用积分抵现比例(x积分抵扣1元）
	 */
	private Long shoppingUseScoreRatio;

	/**
	 * 购物积分分类获取比例上限
	 */
	private Double getRatioLimit;

	/**
	 * 平台使用积分分类比例上限
	 */
	private Double useRatioLimit;

	public Double getUseRatioLimit() {
		return useRatioLimit;
	}

	public void setUseRatioLimit(Double useRatioLimit) {
		this.useRatioLimit = useRatioLimit;
	}

	public Double getGetRatioLimit() {
		return getRatioLimit;
	}

	public void setGetRatioLimit(Double getRatioLimit) {
		this.getRatioLimit = getRatioLimit;
	}

	public String getSignInScoreString() {
		return signInScoreString;
	}

	public void setSignInScoreString(String signInScoreString) {
		this.signInScoreString = signInScoreString;
	}

	public List<Integer> getSignInScore() {
		return signInScore;
	}

	public void setSignInScore(List<Integer> signInScore) {
		this.signInScore = signInScore;
	}

	public Long getRegisterScore() {
		return registerScore;
	}

	public void setRegisterScore(Long registerScore) {
		this.registerScore = registerScore;
	}

	public Boolean getShoppingScoreSwitch() {
		return shoppingScoreSwitch;
	}

	public void setShoppingScoreSwitch(Boolean shoppingScoreSwitch) {
		this.shoppingScoreSwitch = shoppingScoreSwitch;
	}

	public Long getShoppingGetScore() {
		return shoppingGetScore;
	}

	public void setShoppingGetScore(Long shoppingGetScore) {
		this.shoppingGetScore = shoppingGetScore;
	}

	public Long getShoppingUseScoreRatio() {
		return shoppingUseScoreRatio;
	}

	public void setShoppingUseScoreRatio(Long shoppingUseScoreRatio) {
		this.shoppingUseScoreRatio = shoppingUseScoreRatio;
	}

	@Override
	public String toString() {
		return "ScoreConfigVO{" +
				", signInScoreString='" + signInScoreString + '\'' +
				", signInScore=" + signInScore +
				", registerScore=" + registerScore +
				", shoppingScoreSwitch=" + shoppingScoreSwitch +
				", shoppingGetScore=" + shoppingGetScore +
				", shoppingUseScoreRatio=" + shoppingUseScoreRatio +
				", useRatioLimit=" + useRatioLimit +
				", getRatioLimit=" + getRatioLimit +
				'}';
	}
}

