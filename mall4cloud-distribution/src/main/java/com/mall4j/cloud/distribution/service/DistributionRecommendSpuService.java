package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.dto.DistributionRecommendSpuDTO;
import com.mall4j.cloud.distribution.dto.DistributionRecommendSpuQueryDTO;
import com.mall4j.cloud.distribution.dto.DistributionRecommendSpuUpdateDTO;
import com.mall4j.cloud.distribution.vo.DistributionRecommendSpuVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 分销推广-推荐商品
 *
 * @author gww
 * @date 2021-12-24 16:01:22
 */
public interface DistributionRecommendSpuService {

	/**
	 * 分页获取分销推广-推荐商品列表
	 * @param pageDTO 分页参数
	 * @param distributionRecommendSpuQueryDTO 查询参数
	 * @return 分销推广-推荐商品列表分页数据
	 */
	PageVO<DistributionRecommendSpuVO> page(PageDTO pageDTO, DistributionRecommendSpuQueryDTO distributionRecommendSpuQueryDTO);

	/**
	 * 根据分销推广-推荐商品id获取分销推广-推荐商品
	 *
	 * @param id 分销推广-推荐商品id
	 * @return 分销推广-推荐商品
	 */
	DistributionRecommendSpuVO getById(Long id);

	/**
	 * 保存分销推广-推荐商品
	 * @param distributionRecommendSpuDTO 分销推广-推荐商品
	 */
	void save(DistributionRecommendSpuDTO distributionRecommendSpuDTO);

	/**
	 * 更新分销推广-推荐商品
	 * @param distributionRecommendSpuDTO 分销推广-推荐商品
	 */
	void update(DistributionRecommendSpuDTO distributionRecommendSpuDTO);

	/**
	 * 批量更新分销推广-推荐商品
	 * @param distributionRecommendSpuUpdateDTO
	 */
	void updateStatus(DistributionRecommendSpuUpdateDTO distributionRecommendSpuUpdateDTO);

	/**
	 * 导出
	 * @param response
	 * @param distributionRecommendSpuQueryDTO
	 */
	void export(HttpServletResponse response, DistributionRecommendSpuQueryDTO distributionRecommendSpuQueryDTO);

	/**
	 * 通过查询条件返回推荐商品id集合
	 * @param distributionRecommendSpuQueryDTO
	 * @return
	 */
	List<Long> listSpuIdListByParam(DistributionRecommendSpuQueryDTO distributionRecommendSpuQueryDTO);

}
