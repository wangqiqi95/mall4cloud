package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.dto.DistributionStoreActivityUserDTO;
import com.mall4j.cloud.distribution.dto.DistributionStoreActivityUserQueryDTO;
import com.mall4j.cloud.distribution.model.DistributionStoreActivityUser;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpServletResponse;

/**
 * 门店活动-报名用户
 *
 * @author gww
 * @date 2021-12-27 13:42:16
 */
public interface DistributionStoreActivityUserService {

	/**
	 * 分页获取门店活动-报名用户列表
	 * @param pageDTO 分页参数
	 * @param distributionStoreActivityUserQueryDTO 查询参数
	 * @return 门店活动-报名用户列表分页数据
	 */
	PageVO<DistributionStoreActivityUser> page(PageDTO pageDTO, DistributionStoreActivityUserQueryDTO distributionStoreActivityUserQueryDTO);

	/**
	 * 门店活动-报名用户列表导出
	 * @param distributionStoreActivityUserQueryDTO
	 */
	void export(HttpServletResponse response, DistributionStoreActivityUserQueryDTO distributionStoreActivityUserQueryDTO);

	/**
	 * 保存门店活动-报名用户
	 * @param distributionStoreActivityUserDTO 门店活动-报名用户
	 */
	void save(DistributionStoreActivityUserDTO distributionStoreActivityUserDTO);

	void applyCheck(Long activityId);

	/**
	 * 门店活动-报名取消
	 * @param userId 会员id
	 * @param activityId 活动id
	 */
	void cancel(Long userId, Long activityId);

	/**
	 * 门店活动-签到
	 * @param userId 会员id
	 * @param activityId 活动id
	 */
	void sign(Long userId, Long activityId);

	/**
	 * 通过用户id和活动id获取报名详情
	 * @param userId
	 * @param activityId
	 * @return
	 */
	DistributionStoreActivityUser findByUserIdAndActivityId(Long userId, Long activityId);

	Integer countByActivityIdAndStatus(Long activityId, Integer status);

}
