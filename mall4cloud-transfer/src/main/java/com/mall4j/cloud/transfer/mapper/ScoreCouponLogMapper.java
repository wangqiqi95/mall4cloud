package com.mall4j.cloud.transfer.mapper;

import com.mall4j.cloud.transfer.model.ScoreCouponLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 积分换券兑换记录
 *
 * @author FrozenWatermelon
 * @date 2022-04-14 15:16:46
 */
public interface ScoreCouponLogMapper {

	/**
	 * 获取积分换券兑换记录列表
	 * @return 积分换券兑换记录列表
	 */
	List<ScoreCouponLog> list();

	/**
	 * 根据积分换券兑换记录id获取积分换券兑换记录
	 *
	 * @param logId 积分换券兑换记录id
	 * @return 积分换券兑换记录
	 */
	ScoreCouponLog getByLogId(@Param("logId") Long logId);

	/**
	 * 保存积分换券兑换记录
	 * @param scoreCouponLog 积分换券兑换记录
	 */
	void save(@Param("scoreCouponLog") ScoreCouponLog scoreCouponLog);

	/**
	 * 更新积分换券兑换记录
	 * @param scoreCouponLog 积分换券兑换记录
	 */
	void update(@Param("scoreCouponLog") ScoreCouponLog scoreCouponLog);

	/**
	 * 根据积分换券兑换记录id删除积分换券兑换记录
	 * @param logId
	 */
	void deleteById(@Param("logId") Long logId);
}
