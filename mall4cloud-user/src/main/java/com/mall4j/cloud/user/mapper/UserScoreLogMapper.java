package com.mall4j.cloud.user.mapper;

import cn.hutool.core.date.DateTime;
import com.mall4j.cloud.user.dto.CountFriendAssistanceRespDto;
import com.mall4j.cloud.user.dto.PageFriendAssistanceRespDto;
import com.mall4j.cloud.user.model.UserScoreLog;
import com.mall4j.cloud.user.vo.UserScoreLogVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户积分记录
 *
 * @author YXF
 * @date 2020-12-08 10:16:53
 */
public interface UserScoreLogMapper {

	/**
	 * 获取用户积分记录列表
	 * @param userId userId
	 * @return 用户积分记录列表
	 */
	List<UserScoreLogVO> list(@Param("userId") Long userId);

	/**
	 *根据出入类型和积分状态分页获取用户积分记录列表
	 * @param userId 用户id
	 * @param ioType 出入类型
	 * @param source 积分状态
	 * @return 用户积分记录列表
	 */
	List<UserScoreLogVO> listByIoTypeAndSource(@Param("userId") Long userId,@Param("ioType") Integer ioType,@Param("source") Integer source);
	/**
	 * 根据用户积分记录id获取用户积分记录
	 *
	 * @param logId 用户积分记录id
	 * @return 用户积分记录
	 */
	UserScoreLog getByLogId(@Param("logId") Long logId);

	/**
	 * 保存用户积分记录
	 * @param userScoreLog 用户积分记录
	 */
	void save(@Param("userScoreLog") UserScoreLog userScoreLog);

	/**
	 * 更新用户积分记录
	 * @param userScoreLog 用户积分记录
	 */
	void update(@Param("userScoreLog") UserScoreLog userScoreLog);

	/**
	 * 根据用户积分记录id删除用户积分记录
	 * @param logId
	 */
	void deleteById(@Param("logId") Long logId);

	/**
	 * 根据日志类型，用户id，开始时间和结束时间获取日志数量
	 * @param type 日志类型
	 * @param userId 用户id
	 * @param beginOfDay 开始时间
	 * @param endOfDay 结束时间
	 * @return 数量
	 */
    Integer countByUserIdAndDateTimeAndType(@Param("type") Integer type, @Param("userId") Long userId, @Param("beginOfDay") DateTime beginOfDay, @Param("endOfDay") DateTime endOfDay);

	/**
	 *  获取用户签到天数
	 * @param userId 用户id
	 * @return 签到天数
	 */
	Integer getConsecutiveDays(@Param("userId") Long userId);

	/**
	 * 批量保存用户日志
	 * @param userScoreLogs
	 * @return 保存的条数
	 */
	int saveBatch(@Param("userScoreLogs") List<UserScoreLog> userScoreLogs);

	/**
	 * 获取业务有关积分记录
	 * @param userId 用户id
	 * @param bizId 业务id
	 * @param source 业务类型
	 * @param ioType 收入/支出
	 * @return
	 */
    UserScoreLog getOrderScoreLog(@Param("userId") Long userId, @Param("bizId") Long bizId, @Param("source") Integer source, @Param("ioType") Integer ioType);

	/**
	 * 获取用户积分记录列表
	 * @param userId userId
	 * @return 用户积分记录列表
	 */
	List<UserScoreLogVO> listScoreLog(@Param("userId") Long userId);

	/**
	 * 统计是否助力
	 * @param userId  用户id
	 * @param bizId  助力用户id
	 * @return
	 */
	Integer isFriendAssistance(@Param("userId") Long userId, @Param("bizId") Long bizId);

	/**
	 * 助力明细列表
	 * @param userId 用户id
	 * @return
	 */
	List<PageFriendAssistanceRespDto> pageFriendAssistance(@Param("userId") Long userId);

	/**
	 * 统计助力用户数及积分
	 * @param userId
	 * @return
	 */
    CountFriendAssistanceRespDto countFriendAssistanceByUserId(@Param("userId") Long userId);

	/**
	 * 统计已助力次数
	 * @param bizId
	 * @return
	 */
	Integer countFriendAssistanceByBizId(@Param("bizId") Long bizId);
}
