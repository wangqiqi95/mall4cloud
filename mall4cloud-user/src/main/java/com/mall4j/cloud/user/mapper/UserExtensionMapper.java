package com.mall4j.cloud.user.mapper;

import cn.hutool.core.date.DateTime;
import com.mall4j.cloud.api.user.dto.CustomerReqDTO;
import com.mall4j.cloud.api.user.dto.MemberReqDTO;
import com.mall4j.cloud.api.user.vo.MemberOverviewVO;
import com.mall4j.cloud.user.model.UserBalanceLog;
import com.mall4j.cloud.user.model.UserExtension;
import com.mall4j.cloud.user.vo.UserExtensionVO;
import com.mall4j.cloud.user.vo.UserRechargeVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 用户扩展信息
 *
 * @author YXF
 * @date 2020-12-08 10:16:53
 */
public interface UserExtensionMapper {

	/**
	 * 获取用户扩展信息列表
	 *
	 * @return 用户扩展信息列表
	 */
	List<UserExtensionVO> list();

	/**
	 * 根据用户扩展信息id获取用户扩展信息
	 *
	 * @param userExtensionId 用户扩展信息id
	 * @return 用户扩展信息
	 */
	UserExtension getByUserExtensionId(@Param("userExtensionId") Long userExtensionId);

	/**
	 * 保存用户扩展信息
	 *
	 * @param userExtension 用户扩展信息
	 */
	void save(@Param("userExtension") UserExtension userExtension);

	/**
	 * 更新用户扩展信息
	 *
	 * @param userExtension 用户扩展信息
	 */
	void update(@Param("userExtension") UserExtension userExtension);

	/**
	 * 根据用户扩展信息id删除用户扩展信息
	 *
	 * @param userExtensionId
	 */
	void deleteById(@Param("userExtensionId") Long userExtensionId);

	/**
	 * 根据成长值范围，更新普通用户的等级
	 *
	 * @param level     更新至该等级
	 * @param minGrowth 最小成长值
	 * @param maxGrowth 最大成长值
	 */
    void updateUserLevel(@Param("level") Integer level, @Param("minGrowth") int minGrowth, @Param("maxGrowth") Integer maxGrowth);

	/**
	 * 更新用户的积分
	 *
	 * @param userIds
	 * @param score
	 * @param growth
	 */
	void updateUserScoreOrGrowth(@Param("userIds") List<Long> userIds, @Param("score") Long score, @Param("growth") Integer growth);

	/**
	 * 扣减用户的积分
	 *
	 * @param userId
	 * @param score
	 */
	void reduceScore(@Param("userId") Long userId, @Param("score") Long score);


	/**
	 * 根据用户Id列表， 获取用户列表信息
	 *
	 * @param userIds
	 */
	List<UserExtension> getByUserIdsAndLevelType(@Param("userIds") List<Long> userIds);

	/**
	 * 获取用户扩展信息
	 *
	 * @param userId
	 * @return
	 */
    UserExtension getByUserId(@Param("userId") Long userId);

	/**
	 * 批量修改用户等级
	 *
	 * @param userIds
	 * @param levelType 会员类型 必填
	 * @return
	 */
	int batchUpdateLevelByUserIds(@Param("userIds") List<Long> userIds, @Param("levelType") Integer levelType);

	/**
	 * 后台批量修改用户余额
	 *
	 * @param userIds
	 * @param balance 改变余额
	 * @param now
	 * @return
	 */
	int updateBatchUserBalanceByUserIds(@Param("userIds") List<Long> userIds, @Param("balance") Long balance, @Param("now") DateTime now);

	/**
	 * 通过充值列表进行充值，会送积分成长值之类的
	 *
	 * @param userRechargeVO 充值内容
	 * @param userBalanceLog 充值记录
	 */
	void updateByUserRecharge(@Param("userRechargeVO") UserRechargeVO userRechargeVO, @Param("userBalanceLog") UserBalanceLog userBalanceLog);

	/**
	 * 通过直接输入金额进行充值
	 *
	 * @param userBalanceLog 充值记录
	 */
	void addByUserBalanceLog(@Param("userBalanceLog") UserBalanceLog userBalanceLog);

	/**
	 * 扣减用户余额
	 *
	 * @param changeBalance 余额
	 * @param userId        用户id
	 * @return 更新状态
	 */
    int deductionUserBalance(@Param("changeBalance") Long changeBalance, @Param("userId") Long userId);

	/**
	 * 更新用户等级，通过购买会员
	 *
	 * @param userId     用户id
	 * @param afterLevel 购买后的等级
	 */
    void updateUserLevelByBuyVip(@Param("userId") Long userId, @Param("afterLevel") Integer afterLevel);

	/**
	 * 批量保存用户扩展表信息
	 *
	 * @param userExtensions 扩展表参数
	 * @return 影响行数
	 */
    int saveBatch(@Param("userExtensions") List<UserExtension> userExtensions);

	/**
	 * 批量修改用户扩展表积分
	 *
	 * @param userExtensions 每个用户修改不通的积分
	 * @return 影响行数
	 */
    int batchUpdateScore(@Param("userExtensions") List<UserExtension> userExtensions);

	/**
	 * 通过订单锁定用户积分
	 *
	 * @param userId   用户id
	 * @param useScore 积分
	 * @return 是否更新成功
	 */
	int lockScoreBySubmitOrder(@Param("userId") Long userId, @Param("useScore") Long useScore);

	/**
	 * 更新积分信息
	 * @param userId 用户id
	 * @param useScore 用户积分
	 * @return 是否更新成功
	 */
	int updateScoreByUserId(@Param("userId") Long userId, @Param("useScore") Long useScore);

	/**
	 * --------------数据分析相关sql--------------
	 * 根据条件参数，统计会员数量
	 * @param param 参数
	 * @return 数量
	 */
	Integer countMemberByParam(@Param("param") MemberReqDTO param);

	/**
	 * 根据参数条件，统计付费会员的数量
	 * @param param 参数
	 * @return 数量
	 */
	Integer countPayMemberByParam(@Param("param") CustomerReqDTO param);

	/**
	 * 统计某个时间段内，会员的人数
	 * @param date 时间参数
	 * @param startTime  开始时间
	 * @param endTime 结束时间
	 * @param isMember 会员类型
	 * @return 数量
	 */
	Integer countAllPersonNum(@Param("date") Date date, @Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("isMember") Integer isMember);

	/**
	 * 统计访客的会员信息
	 * @param dateTime 时间参数
	 * @param startTime  开始时间
	 * @param endTime 结束时间
	 * @return 数量
	 */
	Integer countVisitMemberNum(@Param("dateTime") Date dateTime, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

	/**
	 * 统计时间区间内，加购会员数
	 * @param dateTime 时间参数
	 * @param startTime  开始时间
	 * @param endTime 结束时间
	 * @return 数量
	 */
	Integer countAddCartMemberNum(@Param("dateTime") Date dateTime, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

	/**
	 * 先获取当前符合条件的会员ids
	 * @param param 筛选条件
	 * @return 会员信息集合
	 */
	List<UserExtensionVO> countByMemberTypeConditions(@Param("param") MemberReqDTO param);

	/**
	 * 获取累积会员数和新增会员数
	 * @param param 筛选条件
	 * @return 累积会员数和新增会员数
	 */
	MemberOverviewVO countMemberDataByParam(@Param("param") MemberReqDTO param);

	/**
	 * 修改用户余额
	 * @param userExtension 用户扩展表信息
	 */
	void updateBalanceByVersion(@Param("userExtension") UserExtension userExtension);

	/**
	 * 获取等级与普通会员成长值不匹配的用户
	 * @param levelType 会员类型
	 * @return userIds集合
	 */
	List<Long> getGrowthLevelMismatchUserByLevelType(@Param("levelType") Integer levelType);

	/**
	 * 获取对应等级，会员类型的会员
	 * @param level 会员等级
	 * @param levelType 会员类型
	 * @return
	 */
    List<UserExtension> getLevelAndLevelType(@Param("level") Integer level, @Param("levelType") Integer levelType);

	/**
	 * 批量增加/减少用户level个等级
	 * @param userIds 更新用户的userid集合
	 * @param level 增加/减少的等级数值,level为正数：增加等级，负数：减少等级
	 * @return 修改行数
	 */
    int batchChangeLevelByUserIdsAndLevel(@Param("userIds") List<Long> userIds, @Param("level") Integer level);

	/**
	 * 将付费会员修改成普通会员，并且将会员等级改为普通会员等级的成长值相匹配的等级
	 * @param userIds 用户id集合
	 * @param currentLevelType 当前会员类型
	 * @param afterLevelType 改变后的会员类型
	 */
	void batchUpdateTypeAndLevelByUserIds(@Param("userIds") List<Long> userIds, @Param("currentLevelType") Integer currentLevelType, @Param("afterLevelType") Integer afterLevelType);

}
