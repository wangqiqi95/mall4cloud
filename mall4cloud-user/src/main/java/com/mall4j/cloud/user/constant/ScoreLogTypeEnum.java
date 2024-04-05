package com.mall4j.cloud.user.constant;


/**
 * @author lhd
 * @date 2020/12/30
 */
public enum ScoreLogTypeEnum {

	/**
	 * 0.注册送积分
	 */
	REGISTER(0),
	/**
	 * 1. 购物  2.会员等级提升加积分 3.签到加积分 4购物抵扣使用积分
	 */
	 SHOP(1),

	/**
	 * 等级提升
	 */
	LEVEL_UP(2),

	/**
	 * 签到
	 */
	SIGN_IN(3),
	/**
	 * 购物抵扣积分
	 */
	SCORE_CASH(4),
	/**
	 * 积分过期
	 */
	EXPIRE(5),
	/**
	 * 余额充值
	 */
	BALANCE(6),
	/**
	 * 系统更改积分
	 */
	SYSTEM(7),
	/**
	 * 购物抵扣积分退还
	 */
	SCORE_CASH_BACK(8),
	/**
	 * 订单退款积分退还
	 */
	COMPLETE_ORDER_BACK(9),
	/**
	 * 积分商品兑换
	 */
	SCORE_PRODUCT_EXCHANGE(10),
	/**
	 * 兑换积分退还
	 */
	SCORE_PRODUCT_EXCHANGE_BACK(11),

	/**
	 * 积分清零活动用户拉新
	 */
	USER_PULL_NEW(12),

	/**
	 * 积分清零活动用户唤醒
	 */
	USER_AWAKEN(13)
	;


	private final Integer num;

	public Integer value() {
		return num;
	}

	ScoreLogTypeEnum(Integer num){
		this.num = num;
	}

	public static ScoreLogTypeEnum instance(Integer value) {
		ScoreLogTypeEnum[] enums = values();
		for (ScoreLogTypeEnum statusEnum : enums) {
			if (statusEnum.value().equals(value)) {
				return statusEnum;
			}
		}
		return null;
	}
}
