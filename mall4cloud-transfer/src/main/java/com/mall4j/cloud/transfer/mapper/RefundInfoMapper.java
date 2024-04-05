package com.mall4j.cloud.transfer.mapper;

import com.mall4j.cloud.transfer.model.RefundInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 退款信息
 *
 * @author FrozenWatermelon
 * @date 2022-04-04 23:32:10
 */
public interface RefundInfoMapper {

	/**
	 * 获取退款信息列表
	 * @return 退款信息列表
	 */
	List<RefundInfo> list();

	/**
	 * 根据退款信息id获取退款信息
	 *
	 * @param refundId 退款信息id
	 * @return 退款信息
	 */
	RefundInfo getByRefundId(@Param("refundId") Long refundId);

	/**
	 * 保存退款信息
	 * @param refundInfo 退款信息
	 */
	void save(@Param("refundInfo") RefundInfo refundInfo);
    void save2(@Param("refundInfo") RefundInfo refundInfo);

	/**
	 * 更新退款信息
	 * @param refundInfo 退款信息
	 */
	void update(@Param("refundInfo") RefundInfo refundInfo);

	/**
	 * 根据退款信息id删除退款信息
	 * @param refundId
	 */
	void deleteById(@Param("refundId") Long refundId);
}
