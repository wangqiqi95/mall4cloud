package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.dto.DistributionStoreActivityQueryDTO;
import com.mall4j.cloud.distribution.dto.DistributionStoreActivityUpdateDTO;
import com.mall4j.cloud.distribution.model.DistributionStoreActivity;
import com.mall4j.cloud.distribution.vo.DistributionStoreActivityProvinceCountVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 门店活动
 *
 * @author gww
 * @date 2021-12-26 21:17:59
 */
public interface DistributionStoreActivityMapper {

	/**
	 * 获取门店活动列表
	 * @param distributionStoreActivityQueryDTO 查询参数
	 * @return 门店活动列表
	 */
	List<DistributionStoreActivity> list(@Param("queryDTO") DistributionStoreActivityQueryDTO distributionStoreActivityQueryDTO);

	/**
	 * 获取门店生效中的活动列表
	 * @param distributionStoreActivityQueryDTO 查询参数
	 * @return 门店活动列表
	 */
	List<DistributionStoreActivity> listEffect(@Param("queryDTO") DistributionStoreActivityQueryDTO distributionStoreActivityQueryDTO);

	/**
	 * 获取门店生效中的活动列表
	 * @param distributionStoreActivityQueryDTO 查询参数
	 * @return 门店活动列表
	 */
	List<DistributionStoreActivity> listAppEffect(@Param("queryDTO") DistributionStoreActivityQueryDTO distributionStoreActivityQueryDTO);

	/**
	 * 根据省份进行分组统计
	 * @return
	 */
	List<DistributionStoreActivityProvinceCountVO> groupByProvince();

	/**
	 * 根据门店活动id获取门店活动
	 *
	 * @param id 门店活动id
	 * @return 门店活动
	 */
	DistributionStoreActivity getById(@Param("id") Long id);

	/**
	 * 保存门店活动
	 * @param distributionStoreActivity 门店活动
	 */
	void save(@Param("distributionStoreActivity") DistributionStoreActivity distributionStoreActivity);

	/**
	 * 更新门店活动
	 * @param distributionStoreActivity 门店活动
	 */
	void update(@Param("distributionStoreActivity") DistributionStoreActivity distributionStoreActivity);

	/**
	 * 根据门店活动id删除门店活动
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	/**
	 * 批量更新门店活动
	 * @param distributionStoreActivityUpdateDTO
	 */
	void updateStatusBatch(@Param("distributionStoreActivityUpdateDTO") DistributionStoreActivityUpdateDTO distributionStoreActivityUpdateDTO);

	/**
	 * 当前小时需要提醒的活动ID集合
	 *
	 * @param remindType 1-活动开始前 2-活动结束后
	 * @return
	 */
	List<DistributionStoreActivity> remindStoreActivityList(@Param("remindType") Integer remindType);

	int activityBeginUpdateWeight();

	int activityToBeginUpdateWeight();

	int activityEndUpdateWeight();
}
