package com.mall4j.cloud.biz.service;

import com.mall4j.cloud.biz.dto.NotifyLogDTO;
import com.mall4j.cloud.biz.model.NotifyLog;
import com.mall4j.cloud.biz.vo.NotifyLogVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

import java.util.List;

/**
 *
 *
 * @author FrozenWatermelon
 * @date 2021-01-16 15:01:14
 */
public interface NotifyLogService {

	/**
	 * 分页获取列表
	 *
	 * @param page
	 * @param userId 用户id
	 * @param msgType 消息类型
	 * @param status 消息已读状态 1.已读 0.未读
	 * @return 消息数量
	 */
	PageVO<NotifyLogVO> pageBySendTypeAndRemindType(PageDTO page, Long userId, Integer msgType, Integer status);

	/**
	 * 批量更新
	 * @param notifyLogList
	 */
	void updateBatchById(List<NotifyLogVO> notifyLogList);

	/**
	 * 查询用户未读消息数量
	 * @param userId 用户id
	 * @param msgType 消息类型
	 * @return 未读消息数量
	 */
	int countUnreadBySendTypeAndRemindType(Long userId, Integer msgType);

	/**
	 * 批量保存
	 * @param notifyLogs 日志信息
	 */
    void saveBatch(List<NotifyLog> notifyLogs);

	/**
	 * 分页获取列表
	 * @param pageDTO 分页信息
	 * @param notifyLogDTO 搜索参数
	 * @return 分页列表
	 */
	PageVO<NotifyLogVO> page(PageDTO pageDTO, NotifyLogDTO notifyLogDTO);
}
