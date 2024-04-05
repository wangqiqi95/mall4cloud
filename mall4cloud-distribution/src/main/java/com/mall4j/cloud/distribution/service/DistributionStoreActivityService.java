package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.dto.DistributionStoreActivityQueryDTO;
import com.mall4j.cloud.distribution.dto.DistributionStoreActivityUpdateDTO;
import com.mall4j.cloud.distribution.model.DistributionStoreActivity;
import com.mall4j.cloud.distribution.vo.DistributionStoreActivityCountVO;
import com.mall4j.cloud.distribution.vo.DistributionStoreActivityProvinceCountVO;
import com.mall4j.cloud.distribution.vo.StoreActivityProvinceVO;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 门店活动
 *
 * @author gww
 * @date 2021-12-26 21:17:59
 */
public interface DistributionStoreActivityService {

	/**
	 * 分页获取门店活动列表
	 * @param pageDTO 分页参数
	 * @param distributionStoreActivityQueryDTO 查询参数
	 * @return 门店活动列表分页数据
	 */
	PageVO<DistributionStoreActivity> page(PageDTO pageDTO, DistributionStoreActivityQueryDTO distributionStoreActivityQueryDTO);

	/**
	 * 分页获取生效中的门店活动列表
	 * @param pageDTO 分页参数
	 * @param distributionStoreActivityQueryDTO 查询参数
	 * @return 门店活动列表分页数据
	 */
	PageVO<DistributionStoreActivity> pageEffect(PageDTO pageDTO, DistributionStoreActivityQueryDTO distributionStoreActivityQueryDTO);


	DistributionStoreActivityCountVO count();

	/**
	 * 分页获取我报名的门店活动列表-商城端
	 * @param pageDTO 分页参数
	 * @param distributionStoreActivityQueryDTO 查询参数
	 * @return 门店活动列表分页数据
	 */
	PageVO<DistributionStoreActivity> appPageEffect(PageDTO pageDTO, DistributionStoreActivityQueryDTO distributionStoreActivityQueryDTO);

	List<StoreActivityProvinceVO> appProvinceEffect();

	/**
	 * 根据省份进行分组统计
	 *
	 * @return
	 */
	List<DistributionStoreActivityProvinceCountVO> groupByProvince();

	/**
	 * 根据门店活动id获取门店活动
	 *
	 * @param id 门店活动id
	 * @return 门店活动
	 */
	DistributionStoreActivity getById(Long id);

	/**
	 * 保存门店活动
	 * @param distributionStoreActivity 门店活动
	 */
	void save(DistributionStoreActivity distributionStoreActivity);

	/**
	 * 更新门店活动
	 * @param distributionStoreActivity 门店活动
	 */
	void update(DistributionStoreActivity distributionStoreActivity);

	/**
	 * 更新门店活动状态
	 * @param distributionStoreActivityUpdateDTO
	 */
	void updateStatus(DistributionStoreActivityUpdateDTO distributionStoreActivityUpdateDTO);

	List<String> importExcel(MultipartFile file);

}
