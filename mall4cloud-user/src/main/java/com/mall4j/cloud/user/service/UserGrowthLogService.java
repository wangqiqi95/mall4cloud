package com.mall4j.cloud.user.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.user.model.UserGrowthLog;
import com.mall4j.cloud.user.vo.UserGrowthLogVO;

import java.util.List;

/**
 * 用户成长值记录
 *
 * @author YXF
 * @date 2020-12-08 10:16:53
 */
public interface UserGrowthLogService {

	/**
	 * 分页获取用户成长值记录列表
	 * @param pageDTO 分页参数
	 * @return 用户成长值记录列表分页数据
	 */
	PageVO<UserGrowthLogVO> page(PageDTO pageDTO);

	/**
	 * 根据用户成长值记录id获取用户成长值记录
	 *
	 * @param logId 用户成长值记录id
	 * @return 用户成长值记录
	 */
	UserGrowthLog getByLogId(Long logId);

	/**
	 * 保存用户成长值记录
	 * @param userGrowthLog 用户成长值记录
	 */
	void save(UserGrowthLog userGrowthLog);

	/**
	 * 更新用户成长值记录
	 * @param userGrowthLog 用户成长值记录
	 */
	void update(UserGrowthLog userGrowthLog);

	/**
	 * 根据用户成长值记录id删除用户成长值记录
	 * @param logId
	 */
	void deleteById(Long logId);

	/**
	 * 批量保存成长值记录
	 * @param userGrowthLogs
	 * @return
	 */
    int saveBatch(List<UserGrowthLog> userGrowthLogs);

	/**
	 * 分页获取用户成长值记录列表
	 * @param pageDTO 分页参数
	 * @param userId 用户id
	 * @return 成长值记录列表
	 */
    PageVO<UserGrowthLogVO> getPageByUserId(PageDTO pageDTO, Long userId);

	/**
	 * 根据业务id获取成长值日志
	 * @param bizId 业务id
	 * @param ioType 0减少 1收入
	 * @return 成长值变更日志
	 */
    UserGrowthLog getByBizId(Long bizId, Integer ioType);
}
