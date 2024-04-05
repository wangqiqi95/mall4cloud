package com.mall4j.cloud.platform.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.api.platform.dto.OfflineHandleEventDTO;
import com.mall4j.cloud.platform.model.OfflineHandleEvent;
import com.mall4j.cloud.api.platform.vo.OfflineHandleEventVO;

/**
 * 下线处理事件
 *
 * @author YXF
 * @date 2021-01-15 17:46:26
 */
public interface OfflineHandleEventService {

	/**
	 * 分页获取下线处理事件列表
	 * @param pageDTO 分页参数
	 * @return 下线处理事件列表分页数据
	 */
	PageVO<OfflineHandleEventVO> page(PageDTO pageDTO);

	/**
	 * 根据下线处理事件id获取下线处理事件
	 *
	 * @param eventId 下线处理事件id
	 * @return 下线处理事件
	 */
	OfflineHandleEventVO getByEventId(Long eventId);

	/**
	 * 保存下线处理事件
	 * @param offlineHandleEvent 下线处理事件
	 */
	void save(OfflineHandleEvent offlineHandleEvent);

	/**
	 * 更新事件为申请状态
	 * @param eventId
	 * @param reapplyReason
	 */
	void apply(Long eventId, String reapplyReason);

	/**
	 * 通过事件类型和所属id获取正在处理的下线事件信息
	 * @param handleType
	 * @param handleId
	 * @return
	 */
	OfflineHandleEventVO getProcessingEventByHandleTypeAndHandleId(Integer handleType, Long handleId);

	/**
	 * 更新下线时间为申请状态
	 * @param eventId
	 * @param reapplyReason
	 */
	void updateToApply(Long eventId, String reapplyReason);

	/**
	 * 审核下线事件
	 * @param offlineHandleEventDTO
	 */
	void auditOfflineEvent(OfflineHandleEventDTO offlineHandleEventDTO);

	/**
	 * 统计审核的数量
	 *
	 * @param handleType
	 * @param handleId
	 * @return
	 */
    int offlineCount(Integer handleType, Long handleId);

	/**
	 * 更新审核事件
	 * @param offlineHandleEventDto
	 */
	void update(OfflineHandleEventDTO offlineHandleEventDto);

	/**
	 * 根据事件类型与事件id删除事件
	 * @param handleType
	 * @param handleId
	 */
    void deleteByHandleTypeAndHandleId(Integer handleType, Long handleId);
}
