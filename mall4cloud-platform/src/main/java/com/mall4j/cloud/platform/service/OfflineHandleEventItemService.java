package com.mall4j.cloud.platform.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.platform.model.OfflineHandleEventItem;
import com.mall4j.cloud.api.platform.vo.OfflineHandleEventItemVO;

import java.util.List;

/**
 * 下线处理事件记录项
 *
 * @author YXF
 * @date 2021-01-15 17:46:26
 */
public interface OfflineHandleEventItemService {

	/**
	 * 分页获取下线处理事件记录项列表
	 * @param pageDTO 分页参数
	 * @return 下线处理事件记录项列表分页数据
	 */
	PageVO<OfflineHandleEventItemVO> page(PageDTO pageDTO);

	/**
	 * 根据下线处理事件记录项id获取下线处理事件记录项
	 *
	 * @param eventItemId 下线处理事件记录项id
	 * @return 下线处理事件记录项
	 */
	OfflineHandleEventItemVO getByEventItemId(Long eventItemId);

	/**
	 * 保存下线处理事件记录项
	 * @param offlineHandleEventItem 下线处理事件记录项
	 */
	void save(OfflineHandleEventItem offlineHandleEventItem);

	/**
	 * 更新下线处理事件记录项
	 * @param offlineHandleEventItem 下线处理事件记录项
	 */
	void update(OfflineHandleEventItem offlineHandleEventItem);


	/**
	 * 获取最新的下线处理事件记录
	 * @param eventId
	 * @return
	 */
    OfflineHandleEventItem getNewOfflineHandleEventItem(Long eventId);

	/**
	 * 根据事件id删除事件项
	 * @param eventIds
	 */
	void deleteByEventIds(List<Long> eventIds);
}
