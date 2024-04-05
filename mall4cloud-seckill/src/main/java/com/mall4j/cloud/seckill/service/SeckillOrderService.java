package com.mall4j.cloud.seckill.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.order.vo.ShopCartOrderMergerVO;
import com.mall4j.cloud.seckill.model.SeckillOrder;
import com.mall4j.cloud.seckill.vo.SeckillOrderVO;
import com.mall4j.cloud.seckill.vo.SeckillVO;

/**
 * 秒杀订单
 *
 * @author lhd
 * @date 2021-03-30 14:59:28
 */
public interface SeckillOrderService {

	/**
	 * 分页获取秒杀订单列表
	 * @param pageDTO 分页参数
	 * @return 秒杀订单列表分页数据
	 */
	PageVO<SeckillOrderVO> page(PageDTO pageDTO);

	/**
	 * 根据秒杀订单id获取秒杀订单
	 *
	 * @param seckillOrderId 秒杀订单id
	 * @return 秒杀订单
	 */
	SeckillOrderVO getBySeckillOrderId(Long seckillOrderId);

	/**
	 * 保存秒杀订单
	 * @param seckillOrder 秒杀订单
	 */
	void save(SeckillOrder seckillOrder);

	/**
	 * 更新秒杀订单
	 * @param seckillOrder 秒杀订单
	 */
	void update(SeckillOrder seckillOrder);

	/**
	 * 根据秒杀订单id删除秒杀订单
	 * @param seckillOrderId
	 */
	void deleteById(Long seckillOrderId);

	/**
	 * 计算订单中的商品数量
	 * @param seckill
	 * @param userId
	 * @param count
	 * @return 是否超过限制数量
	 */
	boolean checkOrderSpuNum(SeckillVO seckill, Long userId, Integer count);

	/**
	 * 提交订单
	 * @param mergerOrder
	 */
	void submit(ShopCartOrderMergerVO mergerOrder);

	/**
	 * 计算秒杀订单数量，如果有说明秒杀成功
	 * @param orderId
	 * @return
	 */
	int countByOrderId(Long orderId);

	/**
	 * 取消未支付的订单，还原秒杀库存
	 * @param orderId 订单id
	 */
    void cancelUnpayOrderByOrderId(Long orderId);

	/**
	 * 秒杀订单支付成功
	 * @param orderId 订单id
	 */
	void paySuccessOrderByOrderId(Long orderId);
}
