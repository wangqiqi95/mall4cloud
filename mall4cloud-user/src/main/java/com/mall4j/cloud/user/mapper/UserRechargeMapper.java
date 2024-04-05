package com.mall4j.cloud.user.mapper;

import com.mall4j.cloud.user.model.UserRecharge;
import com.mall4j.cloud.user.vo.UserRechargeVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 余额充值级别表
 *
 * @author FrozenWatermelon
 * @date 2021-04-27 15:51:36
 */
public interface UserRechargeMapper {

	/**
	 * 获取余额充值级别表列表
	 * @return 余额充值级别表列表
	 */
	List<UserRechargeVO> list();

	/**
	 * 根据余额充值级别表id获取余额充值级别表
	 *
	 * @param rechargeId 余额充值级别表id
	 * @return 余额充值级别表
	 */
	UserRecharge getByRechargeId(@Param("rechargeId") Long rechargeId);

	/**
	 * 保存余额充值级别表
	 * @param userRecharge 余额充值级别表
	 */
	void save(@Param("userRecharge") UserRecharge userRecharge);

	/**
	 * 更新余额充值级别表
	 * @param userRecharge 余额充值级别表
	 */
	void update(@Param("userRecharge") UserRecharge userRecharge);

	/**
	 * 根据余额充值级别表id删除余额充值级别表
	 * @param rechargeId
	 */
	void deleteById(@Param("rechargeId") Long rechargeId);

	/**
	 * 获取充值信息
	 * @param rechargeId
	 * @return
	 */
    UserRechargeVO getRechargeInfo(@Param("rechargeId") Long rechargeId);
}
