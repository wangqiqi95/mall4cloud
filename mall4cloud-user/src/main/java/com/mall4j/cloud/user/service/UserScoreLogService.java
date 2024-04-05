package com.mall4j.cloud.user.service;

import cn.hutool.core.date.DateTime;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.user.dto.CountFriendAssistanceRespDto;
import com.mall4j.cloud.user.dto.PageFriendAssistanceRespDto;
import com.mall4j.cloud.user.model.UserScoreLog;
import com.mall4j.cloud.user.vo.UserScoreLogVO;

import java.util.List;

/**
 * 用户积分记录
 *
 * @author YXF
 * @date 2020-12-08 10:16:53
 */
public interface UserScoreLogService {

	/**
	 * 分页获取用户积分记录列表
	 * @param pageDTO 分页参数
	 * @return 用户积分记录列表分页数据
	 */
	PageVO<UserScoreLogVO> page(PageDTO pageDTO);

	/**
	 * 根据出入类型和积分状态分页获取用户积分记录列表
	 * @param pageDTO 分页参数
	 * @param ioType 出入类型
	 * @param source 积分状态
	 * @return 用户积分记录列表分页数据
	 */
	PageVO<UserScoreLogVO> pageByIoTypeAndSource(PageDTO pageDTO,Integer ioType,Integer source);

	/**
	 * 通过用户id，分页获取用户积分日志
	 * @param pageDTO
	 * @param userId
	 * @return
	 */
	PageVO<UserScoreLogVO> pageByUserId(PageDTO pageDTO, Long userId);

	/**
	 * 根据用户积分记录id获取用户积分记录
	 *
	 * @param logId 用户积分记录id
	 * @return 用户积分记录
	 */
	UserScoreLog getByLogId(Long logId);

	/**
	 * 保存用户积分记录
	 * @param userScoreLog 用户积分记录
	 */
	void save(UserScoreLog userScoreLog);

	/**
	 * 更新用户积分记录
	 * @param userScoreLog 用户积分记录
	 */
	void update(UserScoreLog userScoreLog);

	/**
	 * 根据用户积分记录id删除用户积分记录
	 * @param logId
	 */
	void deleteById(Long logId);

	/**
	 * 批量保存保存用户积分记录
	 * @param userScoreLogs 用户积分记录
	 * @return 受影响行数
	 */
	int saveBatch(List<UserScoreLog> userScoreLogs);

	/**
	 * 根据日志类型，用户id，开始时间和结束时间获取日志数量
	 * @param type 日志类型
	 * @param userId 用户id
	 * @param beginOfDay 开始时间
	 * @param endOfDay 结束时间
	 * @return 数量
	 */
	Integer countByUserIdAndDateTimeAndType(Integer type, Long userId, DateTime beginOfDay, DateTime endOfDay);

	/**
	 *  获取用户签到天数
	 * @param userId 用户id
	 * @return 签到天数
	 */
	Integer getConsecutiveDays(Long userId);

	/**
	 * 获取业务有关积分记录
	 *
	 * @param userId 用户id
	 * @param bizId 业务id
	 * @param source 业务类型
	 * @param ioType 收入/支出
	 * @return
	 */
	UserScoreLog getOrderScoreLog(Long userId, Long bizId,Integer source, Integer ioType);

	/**
	 * 获取用户积分商品兑换记录列表
	 * @param pageDTO
	 * @return
	 */
	PageVO<UserScoreLogVO> scoreProdPage(PageDTO pageDTO);

	/**
	 * 查询用户是否已助力
	 * @param userId 邀请人
	 * @param bizId  被邀请人
	 * @return
	 */
	Boolean isFriendAssistance(Long userId,Long bizId);

	/**
	 * 获取好友助力明细列表
	 * @return
	 */
	PageVO<PageFriendAssistanceRespDto> pageFriendAssistance(PageDTO dto);

	/**
	 * 统计好友助力人数及总积分
	 * @return
	 */
    CountFriendAssistanceRespDto countFriendAssistance();


	/**
	 * 统计已助力次数
	 * @param bizId
	 * @return
	 */
	Integer countFriendAssistanceByBizId(Long bizId);
}
