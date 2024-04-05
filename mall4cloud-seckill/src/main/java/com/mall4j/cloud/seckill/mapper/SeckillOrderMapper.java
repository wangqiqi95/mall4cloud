package com.mall4j.cloud.seckill.mapper;

import com.mall4j.cloud.seckill.model.SeckillOrder;
import com.mall4j.cloud.seckill.vo.SeckillOrderVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 秒杀订单
 *
 * @author lhd
 * @date 2021-03-30 14:59:28
 */
public interface SeckillOrderMapper {

	/**
	 * 获取秒杀订单列表
	 *
	 * @return 秒杀订单列表
	 */
	List<SeckillOrderVO> list();

	/**
	 * 根据秒杀订单id获取秒杀订单
	 *
	 * @param seckillOrderId 秒杀订单id
	 * @return 秒杀订单
	 */
	SeckillOrderVO getBySeckillOrderId(@Param("seckillOrderId") Long seckillOrderId);

	/**
	 * 保存秒杀订单
	 *
	 * @param seckillOrder 秒杀订单
	 */
	void save(@Param("seckillOrder") SeckillOrder seckillOrder);

	/**
	 * 更新秒杀订单
	 *
	 * @param seckillOrder 秒杀订单
	 */
	void update(@Param("seckillOrder") SeckillOrder seckillOrder);

	/**
	 * 根据秒杀订单id删除秒杀订单
	 *
	 * @param seckillOrderId
	 */
	void deleteById(@Param("seckillOrderId") Long seckillOrderId);

	/**
	 * 查询用户在秒杀活动购买的商品数量
	 *
	 * @param seckillId
	 * @param spuId
	 * @param userId
	 * @return
	 */
    int selectNumByUser(@Param("seckillId") Long seckillId, @Param("spuId") Long spuId, @Param("userId") Long userId);

	/**
	 * 计算秒杀订单数量，如果有说明秒杀成功
	 *
	 * @param orderId
	 * @return
	 */
    int countByOrderId(@Param("orderId") Long orderId);

	/**
	 * 取消未支付的秒杀订单
	 *
	 * @param orderId 订单id
	 * @return
	 */
	int cancelUnpayOrderByOrderId(@Param("orderId") Long orderId);

	/**
	 * 计算未支付的订单数量
	 *
	 * @param orderId 订单id
	 * @return 未支付的订单数量
	 */
	int countUnpayOrderByOrderId(@Param("orderId") Long orderId);

	/**
	 * 更新成为支付成功的状态
	 * @param orderId
	 */
    void updateToPaySuccess(@Param("orderId") Long orderId);
}
