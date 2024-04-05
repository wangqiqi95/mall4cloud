package com.mall4j.cloud.biz.mapper;

import com.mall4j.cloud.biz.dto.NotifyLogDTO;
import com.mall4j.cloud.biz.model.NotifyLog;
import com.mall4j.cloud.biz.vo.NotifyLogVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 *
 * @author FrozenWatermelon
 * @date 2021-01-16 15:01:14
 */
public interface NotifyLogMapper {

	/**
	 * 更新
	 * @param notifyLogList
	 */
	void updateBatchById(@Param("notifyLogList") List<NotifyLogVO> notifyLogList);

	/**
	 * 查询用户消息列表
	 * @param userId 用户id
	 * @param msgType 消息类型
	 * @param status 消息已读状态 1.已读 0.未读
	 * @return 消息列表
	 */
	List<NotifyLogVO> listBySendTypeAndRemindType(@Param("userId") Long userId, @Param("msgType") Integer msgType, @Param("status") Integer status);

	/**
	 * 批量保存
	 * @param notifyLogs 日志信息
	 */
    void saveBatch(@Param("notifyLogs") List<NotifyLog> notifyLogs);

	/**
	 * 根据条件获取已发送消息数量
	 * @param orderId 关联订单id
	 * @param level 等级
	 * @param userId 用户id
	 * @param sendType 发送类型
	 * @return 已发送消息数量
	 */
	int countNotifyByConditions(@Param("orderId") Long orderId, @Param("level") Integer level, @Param("userId") Long userId, @Param("sendType") Integer sendType);

	/**
	 * 根据条件获取日志列表
	 * @param notifyLogDTO 搜索参数
	 * @return 日志列表
	 */
    List<NotifyLogVO> list(@Param("notifyLog") NotifyLogDTO notifyLogDTO);
}
