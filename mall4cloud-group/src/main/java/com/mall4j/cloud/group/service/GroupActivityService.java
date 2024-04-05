package com.mall4j.cloud.group.service;

import com.mall4j.cloud.common.product.vo.GroupActivitySpuVO;
import com.mall4j.cloud.api.platform.dto.OfflineHandleEventDTO;
import com.mall4j.cloud.api.platform.vo.OfflineHandleEventVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.group.constant.GroupActivityStatusEnum;
import com.mall4j.cloud.group.dto.AppGroupActivityDTO;
import com.mall4j.cloud.group.dto.GroupActivityDTO;
import com.mall4j.cloud.group.model.GroupActivity;
import com.mall4j.cloud.group.vo.app.AppGroupActivityVO;
import com.mall4j.cloud.group.vo.GroupActivityVO;
import com.mall4j.cloud.group.vo.app.AppGroupTeamVO;

import java.util.List;

/**
 * 拼团活动表
 *
 * @author YXF
 * @date 2021-03-20 10:39:31
 */
public interface GroupActivityService {

	/**
	 * 分页获取拼团活动表列表
	 * @param pageDTO 分页参数
	 * @param groupActivityDTO 搜索参数
	 * @return 拼团活动表列表分页数据
	 */
	PageVO<GroupActivityVO> page(PageDTO pageDTO, GroupActivityDTO groupActivityDTO);

	/**
	 * 平台管理分页查询
	 * @param pageDTO 分页参数
	 * @param groupActivityDTO 搜索参数
	 * @return 拼团活动表列表分页数据
	 */
	PageVO<GroupActivityVO> platformPage(PageDTO pageDTO, GroupActivityDTO groupActivityDTO);

	/**
	 * 根据拼团活动表id获取拼团活动信息
	 *
	 * @param groupActivityId 拼团活动表id
	 * @return 拼团活动表
	 */
	GroupActivityVO getByGroupActivityId(Long groupActivityId);

	/**
	 * 根据拼团活动表id获取拼团活动及商品信息
	 *
	 * @param groupActivityId 拼团活动表id
	 * @return 拼团活动表
	 */
	GroupActivityVO getGroupActivityInfo(Long groupActivityId);

	/**
	 * 保存拼团活动表
	 * @param groupActivityDTO 拼团活动信息
	 */
	void save(GroupActivityDTO groupActivityDTO);

	/**
	 * 更新拼团活动表
	 * @param groupActivityDTO 拼团活动信息
	 */
	void update(GroupActivityDTO groupActivityDTO);

	/**
	 * 更改活动的状态
	 * @param groupActivityId
	 * @param status
	 */
	void updateStatus(Long groupActivityId, GroupActivityStatusEnum status);

	/**
	 * 失效活动
	 * @param groupActivityId
	 * @param spuId
	 */
	void invalidGroupActivity(Long groupActivityId, Long spuId);

	/**
	 * 删除活动
	 * @param groupActivityId
	 * @param spuId
	 */
	void deleteGroupActivity(Long groupActivityId, Long spuId);

	/**
	 * 启动活动
	 * @param groupActivityId
	 * @param spuId
	 */
	void activeGroupActivity(Long groupActivityId, Long spuId);

	/**
	 * 平台下线活动
	 * @param groupActivityId
	 */
	void offlineGroupActivity(Long groupActivityId);

	/**
	 * 平台下架优惠券
	 * @param offlineHandleEventDto
	 */
	void offline(OfflineHandleEventDTO offlineHandleEventDto);

	/**
	 * 获取下线的事件记录
	 * @param groupActivityId
	 * @return
	 */
	OfflineHandleEventVO getOfflineHandleEvent(Long groupActivityId);

	/**
	 * 平台审核商家提交的申请
	 * @param offlineHandleEventDto
	 */
	void audit(OfflineHandleEventDTO offlineHandleEventDto);

	/**
	 * 违规活动提交审核
	 * @param offlineHandleEventDto
	 */
	void auditApply(OfflineHandleEventDTO offlineHandleEventDto);

	/**
	 * 拼团活动详情
	 * @param spuId
	 * @param storeId
	 * @return
	 */
    AppGroupActivityVO getAppGroupActivityInfo(Long spuId, Long storeId);

	/**
	 * 根据spuId获取团购信息
	 * @param spuId
	 * @return
	 */
	AppGroupActivityVO getBySpuId(Long spuId);

	AppGroupActivityVO getBySpuIdAndActivityid(Long spuId,Long actvityId);

	/**
	 * 根据商品id获取团购商品信息
	 * @param spuIds
	 * @return
	 */
	List<GroupActivitySpuVO> groupSpuListBySpuIds(List<Long> spuIds);

	/**
	 * 获取应该结束但是没有结束的拼团列表
	 * @return 应该结束但是没有结束的拼团列表
	 */
	List<GroupActivity> listUnEndButNeedEndActivity();

	/**
	 * 改变商品类型，结束正在进行的拼团
	 * @param groupActivityList
	 */
	void changeProdTypeByGroupActivityIdList(List<GroupActivity> groupActivityList);

	/**
	 * 根据商品ids下线所有的团购活动
	 * @param spuIds 商品ids
	 * @return 返回结果
	 */
    void offlineGroupBySpuIds(List<Long> spuIds);
	/**
	 * 清除团购缓存
	 * @param spuId
	 */
	void removeCache(Long spuId);

	/**
	 * 拼团活动详情（可以获取已失效的）
	 * @param groupActivityId
	 * @param storeId
	 * @return
	 */
	AppGroupActivityVO getAppGroupActivityByGroupActivityId(Long groupActivityId, Long storeId);

	List<AppGroupActivityVO> groupListByStoreIdAndActivityId(Long storeId, AppGroupActivityDTO appGroupActivityDTO);

    AppGroupActivityVO getBySpuIdAndStoreId(Long spuId, Long storeId);
}
