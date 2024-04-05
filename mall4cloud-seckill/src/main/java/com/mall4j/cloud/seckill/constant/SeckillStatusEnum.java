package com.mall4j.cloud.seckill.constant;

/**
 * 秒杀活动状态
 * @author lhd
 * @date 2021/04/01
 */
public enum SeckillStatusEnum {

	/**
	 * 删除 (逻辑删除)
	 */
	DELETE(-1),

	/**
	 * 禁用/过期/下架
	 */
	DISABLE(0),

	/**
	 * 启用/未过期/上架
	 */
	ENABLE(1),

	/**
	 * 违规下架
	 */
	OFFLINE(2),

	/**
	 * 等待审核
	 */
	WAIT_AUDIT(3)
	;

	private final Integer value;

	public Integer value() {
		return value;
	}

	SeckillStatusEnum(Integer value) {
		this.value = value;
	}

	public static Boolean offlineStatus (Integer value) {
		SeckillStatusEnum[] enums = values();
		for (SeckillStatusEnum statusEnum : enums) {
			if (statusEnum.value().equals(value)) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

}
