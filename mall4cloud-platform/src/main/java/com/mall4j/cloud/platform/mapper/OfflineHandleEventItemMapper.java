package com.mall4j.cloud.platform.mapper;

import com.mall4j.cloud.platform.model.OfflineHandleEventItem;
import com.mall4j.cloud.api.platform.vo.OfflineHandleEventItemVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 下线处理事件记录项
 *
 * @author YXF
 * @date 2021-01-15 17:46:26
 */
public interface OfflineHandleEventItemMapper {

	/**
	 * 获取下线处理事件记录项列表
	 *
	 * @return 下线处理事件记录项列表
	 */
	List<OfflineHandleEventItemVO> list();

	/**
	 * 根据下线处理事件记录项id获取下线处理事件记录项
	 *
	 * @param eventItemId 下线处理事件记录项id
	 * @return 下线处理事件记录项
	 */
	OfflineHandleEventItemVO getByEventItemId(@Param("eventItemId") Long eventItemId);

	/**
	 * 保存下线处理事件记录项
	 *
	 * @param offlineHandleEventItem 下线处理事件记录项
	 */
	void save(@Param("offlineHandleEventItem") OfflineHandleEventItem offlineHandleEventItem);

	/**
	 * 更新下线处理事件记录项
	 *
	 * @param offlineHandleEventItem 下线处理事件记录项
	 */
	void update(@Param("offlineHandleEventItem") OfflineHandleEventItem offlineHandleEventItem);

	/**
	 * 获取最新的下线处理事件记录
	 * @param eventId
	 * @return
	 */
    OfflineHandleEventItem getNewOfflineHandleEventItem(@Param("eventId") Long eventId);

	/**
	 * 根据事件id列表删除事件项
	 * @param eventIds
	 */
	void deleteByEventIds(@Param("eventIds") List<Long> eventIds);
}
