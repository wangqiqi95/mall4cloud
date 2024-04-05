package com.mall4j.cloud.platform.mapper;

import com.mall4j.cloud.platform.model.OfflineHandleEvent;
import com.mall4j.cloud.api.platform.vo.OfflineHandleEventVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 下线处理事件
 *
 * @author YXF
 * @date 2021-01-15 17:46:26
 */
public interface OfflineHandleEventMapper {

	/**
	 * 获取下线处理事件列表
	 *
	 * @return 下线处理事件列表
	 */
	List<OfflineHandleEventVO> list();

	/**
	 * 根据下线处理事件id获取下线处理事件
	 *
	 * @param eventId 下线处理事件id
	 * @return 下线处理事件
	 */
	OfflineHandleEvent getByEventId(@Param("eventId") Long eventId);

	/**
	 * 保存下线处理事件
	 *
	 * @param offlineHandleEvent 下线处理事件
	 */
	void save(@Param("offlineHandleEvent") OfflineHandleEvent offlineHandleEvent);

	/**
	 * 更新下线处理事件
	 *
	 * @param offlineHandleEvent 下线处理事件
	 */
	void update(@Param("offlineHandleEvent") OfflineHandleEvent offlineHandleEvent);

	/**
	 * 根据下线处理事件id删除下线处理事件
	 *
	 * @param eventId
	 */
	void deleteById(@Param("eventId") Long eventId);

	/**
	 * 更新事件为申请状态
	 *
	 * @param eventId
	 */
    void updateToApply(Long eventId);

	/**
	 * 通过事件类型和所属id获取正在处理的下线事件信息
	 *
	 * @param handleType
	 * @param handleId
	 * @return
	 */
	OfflineHandleEventVO getProcessingEventByHandleTypeAndHandleId(@Param("handleType") Integer handleType, @Param("handleId") Long handleId);

	/**
	 * 统计审核的数量
	 * @param handleType
	 * @param handleId
	 * @return
	 */
    int offlineCount(@Param("handleType") Integer handleType, @Param("handleId") Long handleId);

	/**
	 * 获取事件id列表
	 * @param handleType
	 * @param handleId
	 * @return
	 */
    List<Long> listIdByHandleTypeAndHandleId(@Param("handleType") Integer handleType, @Param("handleId") Long handleId);

	/**
	 * 删除事件
	 * @param handleType
	 * @param handleId
	 */
	void deleteByHandleTypeAndHandleId(@Param("handleType") Integer handleType, @Param("handleId") Long handleId);
}
