package com.mall4j.cloud.group.mapper;

import com.mall4j.cloud.group.model.GroupOrder;
import com.mall4j.cloud.group.vo.GroupOrderVO;
import com.mall4j.cloud.group.vo.app.AppGroupUserVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 拼团订单表
 *
 * @author YXF
 * @date 2021-03-20 10:39:32
 */
public interface GroupOrderMapper {

	/**
	 * 获取拼团订单表列表
	 *
	 * @return 拼团订单表列表
	 */
	List<GroupOrder> list();

	/**
	 * 根据拼团订单表id获取拼团订单表
	 *
	 * @param groupOrderId 拼团订单表id
	 * @return 拼团订单表
	 */
	GroupOrder getByGroupOrderId(@Param("groupOrderId") Long groupOrderId);

	/**
	 * 保存拼团订单表
	 *
	 * @param groupOrder 拼团订单表
	 */
	void save(@Param("groupOrder") GroupOrder groupOrder);

	/**
	 * 更新拼团订单表
	 *
	 * @param groupOrder 拼团订单表
	 */
	void update(@Param("groupOrder") GroupOrder groupOrder);

	/**
	 * 根据拼团订单表id删除拼团订单表
	 *
	 * @param groupOrderId
	 */
	void deleteById(@Param("groupOrderId") Long groupOrderId);

	/**
	 * 根据订单号，获取团购订单信息
	 *
	 * @param orderId
	 * @return
	 */
    GroupOrderVO getByOrderId(@Param("orderId") Long orderId);

	/**
	 * 获取参团的用户列表
	 *
	 * @param groupTeamId
	 * @return
	 */
	List<AppGroupUserVO> listApiGroupUserDto(@Param("groupTeamId") Long groupTeamId);

	/**
	 * 获取用户团购订单信息
	 *
	 * @param groupTeamId
	 * @param userId
	 * @return
	 */
    GroupOrderVO getUserGroupOrderByGroupTeamId(@Param("groupTeamId") Long groupTeamId, @Param("userId") Long userId);

	/**
	 * 用户在该活动中有的商品数量
	 *
	 * @param userId
	 * @param groupActivityId
	 * @return
	 */
    List<Long> getUserHadSpuCountByGroupActivityId(@Param("userId") Long userId, @Param("groupActivityId") Long groupActivityId);

	/**
	 * 取消团购订单
	 *
	 * @param orderIds 团购订单ids
	 */
    void cancelGroupOrder(@Param("orderIds") List<Long> orderIds);

	/**
	 * 更新订单状态为待成团
	 *
	 * @param orderId      订单id
	 * @param identityType 1代表开团
	 * @param groupTeamId 拼团团队id
	 * @return 更新状态
	 */
	int updateToPaySuccess(@Param("orderId") Long orderId, @Param("identityType") Integer identityType, @Param("groupTeamId") Long groupTeamId);

	/**
	 * 根据待成团的团队id，获取需要发货的团id
	 *
	 * @param groupTeamId 团队id
	 * @return
	 */
    List<Long> listSuccessOrderIdByTeamId(@Param("groupTeamId") Long groupTeamId);

	/**
	 * 批量保存拼团订单
	 *
	 * @param groupOrders 拼团订单信息
	 */
	void saveBatch(@Param("groupOrders") List<GroupOrder> groupOrders);

	/**
	 * 根据商品及用户id，获取用户订单数量
	 *
	 * @param groupActivityId  团购id
	 * @param spuId  商品id
	 * @param userId 用户id
	 * @return
	 */
    Integer getOrderBySpuIdAndUserId(@Param("groupActivityId") Long groupActivityId, @Param("spuId") Long spuId, @Param("userId") Long userId);
}
