package com.mall4j.cloud.api.openapi.constant;

/**
 * 类描述：Redis 中用到的一些key 和 key的前缀常量值
 *
 * @date 2022/1/14 14:26：06
 */
public class RedisConstant {
	private RedisConstant() {
	}

	/**
	 * Redis 的分隔符
	 */
	public static final String REDIS_SEPARATOR = ":";


	/**
	 * 库存同步信息的Redis key
	 * === 数据类型 list 数据，用作队列
	 */
	public static final String STD_SYNC_STOCK_KEY = "STD:SYNC_STOCK";

	/**
	 * 商品价格信息同步的Redis key
	 * === 数据类型 list 数据，用作队列
	 */
	public static final String STD_SYNC_PRICE_KEY = "STD:SYNC_PRICE";

	/**
	 * 商品基础信息同步的Redis key
	 * === 数据类型 list 数据，用作队列
	 */
	public static final String STD_SYNC_PRODUCT_KEY = "STD:SYNC_PRODUCT";
}
