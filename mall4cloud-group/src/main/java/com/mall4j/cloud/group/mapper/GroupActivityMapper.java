package com.mall4j.cloud.group.mapper;

import com.mall4j.cloud.common.product.vo.GroupActivitySpuVO;
import com.mall4j.cloud.group.dto.GroupActivityDTO;
import com.mall4j.cloud.group.model.GroupActivity;
import com.mall4j.cloud.group.vo.app.AppGroupActivityVO;
import com.mall4j.cloud.group.vo.GroupActivityVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 拼团活动表
 *
 * @author YXF
 * @date 2021-03-20 10:39:31
 */
public interface GroupActivityMapper {

	/**
	 * 获取拼团活动表列表
	 *
	 * @param groupActivityDTO
	 * @return 拼团活动表列表
	 */
	List<GroupActivityVO> list(@Param("groupActivity") GroupActivityDTO groupActivityDTO);

	/**
	 * 根据拼团活动表id获取拼团活动表
	 *
	 * @param groupActivityId 拼团活动表id
	 * @return 拼团活动表
	 */
	GroupActivityVO getByGroupActivityId(@Param("groupActivityId") Long groupActivityId);

	/**
	 * 保存拼团活动表
	 *
	 * @param groupActivity 拼团活动表
	 */
	void save(@Param("groupActivity") GroupActivity groupActivity);

	/**
	 * 更新拼团活动表
	 *
	 * @param groupActivity 拼团活动表
	 */
	void update(@Param("groupActivity") GroupActivity groupActivity);

	/**
	 * 根据拼团活动表id删除拼团活动表
	 *
	 * @param groupActivityId
	 */
	void deleteById(@Param("groupActivityId") Long groupActivityId);


	/**
	 * 根据商品id获取活动信息
	 *
	 * @param spuId
	 * @return
	 */
	AppGroupActivityVO getBySpuId(@Param("spuId") Long spuId);

	AppGroupActivityVO getBySpuIdAndActivityId(@Param("spuId") Long spuId, @Param("groupActivityId") Long groupActivityId);

	/**
	 * 根据商品id获取团购商品信息
	 *
	 * @param spuIds
	 * @return
	 */
    List<GroupActivitySpuVO> groupSpuListBySpuIds(@Param("spuIds") List<Long> spuIds);

	/**
	 * 获取应该结束但是没有结束的拼团列表
	 *
	 * @return 应该结束但是没有结束的拼团列表
	 */
	List<GroupActivity> listUnEndButNeedEndActivity();

	/**
	 * 失效活动状态
	 *
	 * @param groupActivityList 活动列表
	 */
	void expiredGroupActivityByGroupActivityIdList(@Param("groupActivityList") List<GroupActivity> groupActivityList);

	/**
	 * 拼团活动详情（可以获取已失效的）
	 *
	 * @param groupActivityId
	 * @return
	 */
	AppGroupActivityVO getAppGroupActivityByGroupActivityId(@Param("groupActivityId") Long groupActivityId);

	/**
	 * 根据商品ids下线所有的团购活动
	 *
	 * @param spuIds 商品ids
	 * @return 返回结果
	 */
	void expiredGroupActivityBySpuIds(@Param("spuIds") List<Long> spuIds);

	/**
	 * 更新团购活动的成团订单数和人数
	 *
	 * @param groupActivityId
	 */
    void updateGroupOrderInfo(@Param("groupActivityId") Long groupActivityId);

    AppGroupActivityVO getBySpuIdAndStoreId(@Param("spuId") Long spuId, @Param("storeId") Long storeId);
}
