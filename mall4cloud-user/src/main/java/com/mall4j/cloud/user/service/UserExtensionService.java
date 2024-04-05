package com.mall4j.cloud.user.service;

import cn.hutool.core.date.DateTime;
import com.mall4j.cloud.api.user.bo.UserOrderScoreBo;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.user.model.UserExtension;
import com.mall4j.cloud.user.vo.UserExtensionVO;

import java.util.List;

/**
 * 用户扩展信息
 *
 * @author YXF
 * @date 2020-12-08 10:16:53
 */
public interface UserExtensionService {

	/**
	 * 分页获取用户扩展信息列表
	 *
	 * @param pageDTO 分页参数
	 * @return 用户扩展信息列表分页数据
	 */
	PageVO<UserExtensionVO> page(PageDTO pageDTO);

	/**
	 * 根据用户扩展信息id获取用户扩展信息
	 *
	 * @param userExtensionId 用户扩展信息id
	 * @return 用户扩展信息
	 */
	UserExtension getByUserExtensionId(Long userExtensionId);

	/**
	 * 保存用户扩展信息
	 *
	 * @param userExtension 用户扩展信息
	 */
	void save(UserExtension userExtension);

	/**
	 * 更新用户扩展信息
	 *
	 * @param userExtension 用户扩展信息
	 */
	void update(UserExtension userExtension);

	/**
	 * 根据用户扩展信息id删除用户扩展信息
	 *
	 * @param userExtensionId
	 */
	void deleteById(Long userExtensionId);

	/**
	 * 根据成长值范围，更新普通用户的等级
	 *
	 * @param level 更新至该等级
	 * @param minGrowth 最小成长值
	 * @param maxGrowth 最大成长值
	 */
	void updateUserLevel(Integer level, int minGrowth, Integer maxGrowth);

	/**
	 * 更新用户的积分
	 *
	 * @param userIds
	 * @param score
	 * @param growth
	 */
	void updateUserScoreOrGrowth(List<Long> userIds, Long score, Integer growth);

	/**
	 * 扣减用户的积分
	 *
	 * @param userId
	 * @param score
	 */
	void reduceScore(Long userId, Long score);

	/**
	 * 根据用户Id列表， 获取用户列表信息
	 *
	 * @param userIds 用户id列表
	 * @return
	 */
	List<UserExtension> getByUserIdsAndLevelType(List<Long> userIds);

	/**
	 * 获取用户扩展信息
	 *
	 * @param userId
	 * @return
	 */
	UserExtension getByUserId(Long userId);

	/**
	 * 批量修改用户等级
	 * @param userIds
	 * @param levelType 会员类型必填
	 * @return
	 */
	void batchUpdateLevelByUserIds(List<Long> userIds, Integer levelType);

	/**
	 * 后台批量修改用户余额
	 * @param userIds
	 * @param balance 改变余额
	 * @param now
	 * @return
	 */
    int updateBatchUserBalanceByUserIds(List<Long> userIds, Long balance, DateTime now);

	/**
	 * 批量保存用户信息扩展表
	 * @param userExtensionList 扩展信息
	 * @return 影响行数
	 */
    int saveBatch(List<UserExtension> userExtensionList);

	/**
	 * 批量修改用户扩展表积分
	 * @param userExtensions 每个用户修改不通的积分
	 * @return 影响行数
	 */
    int batchUpdateScore(List<UserExtension> userExtensions);

	/**
	 * 更新积分及成长值
	 * @param orderId 订单id
	 */
	void updateScoreAndGrowth(Long orderId);

	/**
	 * 退还用户下单抵扣使用的积分
	 * @param userOrderScoreBo 用户订单退还积分
	 */
	void refundScore(UserOrderScoreBo userOrderScoreBo);

	/**
	 * 积分、成长值、余额日志
	 * @param userExtensionList 注册的用户集合
	 */
	void registerUserScoreGrowthBalanceLog(List<UserExtension> userExtensionList);
}
