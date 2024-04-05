package com.mall4j.cloud.user.mapper;

import com.mall4j.cloud.user.model.UserGrowthLog;
import com.mall4j.cloud.user.vo.UserGrowthLogVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户成长值记录
 *
 * @author YXF
 * @date 2020-12-08 10:16:53
 */
public interface UserGrowthLogMapper {

	/**
	 * 获取用户成长值记录列表
	 * @return 用户成长值记录列表
	 */
	List<UserGrowthLogVO> list();

	/**
	 * 根据用户成长值记录id获取用户成长值记录
	 *
	 * @param logId 用户成长值记录id
	 * @return 用户成长值记录
	 */
	UserGrowthLog getByLogId(@Param("logId") Long logId);

	/**
	 * 保存用户成长值记录
	 * @param userGrowthLog 用户成长值记录
	 */
	void save(@Param("userGrowthLog") UserGrowthLog userGrowthLog);

	/**
	 * 更新用户成长值记录
	 * @param userGrowthLog 用户成长值记录
	 */
	void update(@Param("userGrowthLog") UserGrowthLog userGrowthLog);

	/**
	 * 根据用户成长值记录id删除用户成长值记录
	 * @param logId
	 */
	void deleteById(@Param("logId") Long logId);

	/**
	 * 批量保存成长值记录
	 * @param userGrowthLogs
	 * @return
	 */
    int saveBatch(@Param("userGrowthLogs") List<UserGrowthLog> userGrowthLogs);

	/**
	 * 获取用户成长值记录列表
	 * @param userId 用户id
	 * @return 用户成长值记录列表
	 */
    List<UserGrowthLogVO> getPageByUserId(@Param("userId") Long userId);

	/**
	 * 根据业务id获取成长值日志
	 * @param bizId 业务id
	 * @param ioType 1收入 0减少
	 * @return 成长值变更日志
	 */
    UserGrowthLog getByBizId(@Param("bizId") Long bizId, @Param("ioType") Integer ioType);
}
