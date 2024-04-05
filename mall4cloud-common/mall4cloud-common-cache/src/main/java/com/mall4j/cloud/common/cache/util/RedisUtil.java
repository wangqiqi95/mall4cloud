package com.mall4j.cloud.common.cache.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.common.cache.constant.CacheNames;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.util.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author FrozenWatermelon
 * @date 2020/7/11
 */
public class RedisUtil {

	private static final Logger logger = LoggerFactory.getLogger(RedisUtil.class);

	@SuppressWarnings("unchecked")
	private static final RedisTemplate<String, Object> REDIS_TEMPLATE = SpringContextUtils.getBean("redisTemplate",
			RedisTemplate.class);

	public static final StringRedisTemplate STRING_REDIS_TEMPLATE = SpringContextUtils.getBean("stringRedisTemplate",
			StringRedisTemplate.class);

	// =============================common============================
	/**
	 * 指定缓存失效时间
	 * @param key 键
	 * @param time 时间(秒)
	 * @return 是否成功
	 */
	public static Boolean expire(String key, long time) {
		if (key.contains(StrUtil.SPACE)) {
			throw new LuckException(ResponseEnum.EXCEPTION);
		}
		try {
			if (time > 0) {
				REDIS_TEMPLATE.expire(key, time, TimeUnit.SECONDS);
			}
			return Boolean.TRUE;
		}
		catch (Exception e) {
			logger.error("Set expire error: {}", e.getMessage());
			return Boolean.FALSE;
		}
	}

	/**
	 * 根据key 获取过期时间
	 * @param key 键 不能为null
	 * @return 时间(秒) 返回-1代表为永久有效 失效时间为0，说明该主键未设置失效时间（失效时间默认为-1）
	 */
	public static Long getExpire(String key) {
		if (key.contains(StrUtil.SPACE)) {
			throw new LuckException(ResponseEnum.EXCEPTION);
		}
		return REDIS_TEMPLATE.getExpire(key, TimeUnit.SECONDS);
	}

	/**
	 * 判断key是否存在
	 * @param key 键
	 * @return true 存在 false 不存在
	 */
	public static Boolean hasKey(String key) {
		if (key.contains(StrUtil.SPACE)) {
			throw new LuckException(ResponseEnum.EXCEPTION);
		}
		try {
			return REDIS_TEMPLATE.hasKey(key);
		}
		catch (Exception e) {
			logger.error("Error getting hasKey: {}", e.getMessage());
			return Boolean.FALSE;
		}
	}

	/**
	 * 删除缓存
	 * @param key 可以传一个值 或多个
	 */
	@SuppressWarnings("unchecked")
	public static void del(String... key) {
		if (key != null && key.length > 0) {
			for (String s : key) {
				if (s.contains(StrUtil.SPACE)) {
					throw new LuckException(ResponseEnum.EXCEPTION);
				}
			}

			if (key.length == 1) {
				REDIS_TEMPLATE.delete(key[0]);
			}
			else {
				REDIS_TEMPLATE.delete(Arrays.asList(key));
			}
		}
	}

	// ============================String=============================
	/**
	 * 普通缓存获取
	 * @param key 键
	 * @return 值
	 */
	@SuppressWarnings("unchecked")
	public static <T> T get(String key) {
		if (key.contains(StrUtil.SPACE)) {
			throw new LuckException(ResponseEnum.EXCEPTION);
		}
		return (T) REDIS_TEMPLATE.opsForValue().get(key);
	}

	/**
	 * 普通缓存放入并设置时间
	 * @param key 键
	 * @param value 值
	 * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
	 * @return true成功 false 失败
	 */
	public static boolean set(String key, Object value, long time) {
		if (key.contains(StrUtil.SPACE)) {
			throw new LuckException(ResponseEnum.EXCEPTION);
		}
		try {
			if (time > 0) {
				REDIS_TEMPLATE.opsForValue().set(key, value, time, TimeUnit.SECONDS);
			}
			else {
				REDIS_TEMPLATE.opsForValue().set(key, value);
			}
			return true;
		}
		catch (Exception e) {
			logger.error("Redis opsForValue error: {}", e.getMessage());
			return false;
		}
	}

	/**
	 * 递增 此时value值必须为int类型 否则报错
	 * @param key 键
	 * @param delta 要增加几(大于0)
	 * @return 自增后的值
	 */
	public static Long incr(String key, long delta) {
		if (key.contains(StrUtil.SPACE)) {
			throw new LuckException(ResponseEnum.EXCEPTION);
		}
		if (delta < 0) {
			throw new RuntimeException("递增因子必须大于0");
		}
		return STRING_REDIS_TEMPLATE.opsForValue().increment(key, delta);
	}

	/**
	 * 递减
	 * @param key 键
	 * @param delta 要减少几(小于0)
	 * @return 自减后的值
	 */
	public static Long decr(String key, long delta) {
		if (key.contains(StrUtil.SPACE)) {
			throw new LuckException(ResponseEnum.EXCEPTION);
		}
		if (delta < 0) {
			throw new RuntimeException("递减因子必须小于0");
		}
		return STRING_REDIS_TEMPLATE.opsForValue().increment(key, -delta);
	}

	public static boolean setLongValue(String key, Long value, long time) {
		if (key.contains(StrUtil.SPACE)) {
			throw new LuckException(ResponseEnum.EXCEPTION);
		}
		try {
			if (time > 0) {
				STRING_REDIS_TEMPLATE.opsForValue().set(key, String.valueOf(value), time, TimeUnit.SECONDS);
			}
			else {
				STRING_REDIS_TEMPLATE.opsForValue().set(key, String.valueOf(value));
			}
			return true;
		}
		catch (Exception e) {
			logger.error("setLongValue() error: {}", e.getMessage());
			return false;
		}
	}

	/**
	 * 普通缓存获取
	 * @param key 键
	 * @return 值
	 */
	public static Long getLongValue(String key) {
		if (key == null) {
			return null;
		}
		if (key.contains(StrUtil.SPACE)) {
			throw new LuckException(ResponseEnum.EXCEPTION);
		}
		String result = STRING_REDIS_TEMPLATE.opsForValue().get(key);
		if (result == null) {
			return null;
		}
		return Long.valueOf(result);
	}

	/**
	 * 批量删除缓存
	 * @param keys
	 */
	public static void deleteBatch(List<String> keys) {
		if (CollUtil.isEmpty(keys)) {
			return;
		}
		for (String key : keys) {
			if (key.contains(StrUtil.SPACE)) {
				throw new LuckException(ResponseEnum.EXCEPTION);
			}
		}
		REDIS_TEMPLATE.delete(keys);
	}

	public static void deleteBatchKeys(String keys) {
		Set key=REDIS_TEMPLATE.keys(keys);
		REDIS_TEMPLATE.delete(key);
	}

	/**
	 * 批量删除缓存
	 * @param cacheName 缓存名
	 * @param cacheKeys 缓存key
	 */
	public static void deleteBatch(String cacheName, List<?> cacheKeys) {
		if (StrUtil.isBlank(cacheName) || CollUtil.isEmpty(cacheKeys)) {
			return;
		}
		List<String> strCacheKeys = cacheKeys.stream().map(Object::toString).collect(Collectors.toList());
		List<String> keys = new ArrayList<>();
		for (String cacheKey : strCacheKeys) {
			String key = cacheName + CacheNames.UNION + cacheKey;
			keys.add(key);
			if (key.contains(StrUtil.SPACE)) {
				throw new LuckException(ResponseEnum.EXCEPTION);
			}
		}
		REDIS_TEMPLATE.delete(keys);
	}

	/**
	 * 比较和删除标记，原子性
	 * @return 是否成功
	 */
	public static boolean cad(String key, String value) {

		if (key.contains(StrUtil.SPACE) || value.contains(StrUtil.SPACE)) {
			throw new LuckException(ResponseEnum.EXCEPTION);
		}

		String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

		//通过lure脚本原子验证令牌和删除令牌
		Long result = STRING_REDIS_TEMPLATE.execute(new DefaultRedisScript<>(script, Long.class),
				Collections.singletonList(key),
				value);

		return !Objects.equals(result, 0L);
	}

	/**
	 * 普通缓存获取（带插入方式）
	 *
	 * @param time 失效时间(单位:秒), 该方法time必须>60, 小于60请使用普通get
	 * @return
	 */
	public static <T> T get(String key, long time, Supplier<T> o) {
		T t = get(key);

		if (t == null) {
			t = directGetT(key, time, o);
		} else {
			if ("sys-empty".equals(t)) {
				return null;
			}

			long expire = getExpire(key);
			// 30秒内提前更新
			if (expire < 30) {
				directGetT(key, time, o);
			}
		}

		return t;
	}

	private static <T> T directGetT(String key, long time, Supplier<T> o) {
		T t = o.get();
		if (t == null) {
			set(key, "sys-empty", 10);
		} else {
			set(key, t, time);
		}
		return t;
	}
}
