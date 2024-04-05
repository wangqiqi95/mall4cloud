package com.mall4j.cloud.flow.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.flow.model.UserVisitProdAnalysis;

/**
 * 流量分析—用户流量商品数据
 *
 * @author YXF
 * @date 2021-05-21 15:25:19
 */
public interface UserVisitProdAnalysisService {

	/**
	 * 分页获取流量分析—用户流量商品数据列表
	 * @param pageDTO 分页参数
	 * @return 流量分析—用户流量商品数据列表分页数据
	 */
	PageVO<UserVisitProdAnalysis> page(PageDTO pageDTO);

	/**
	 * 根据流量分析—用户流量商品数据id获取流量分析—用户流量商品数据
	 *
	 * @param userAnalysisId 流量分析—用户流量商品数据id
	 * @return 流量分析—用户流量商品数据
	 */
	UserVisitProdAnalysis getByUserAnalysisId(Long userAnalysisId);

	/**
	 * 保存流量分析—用户流量商品数据
	 * @param userVisitProdAnalysis 流量分析—用户流量商品数据
	 */
	void save(UserVisitProdAnalysis userVisitProdAnalysis);

	/**
	 * 更新流量分析—用户流量商品数据
	 * @param userVisitProdAnalysis 流量分析—用户流量商品数据
	 */
	void update(UserVisitProdAnalysis userVisitProdAnalysis);

	/**
	 * 根据流量分析—用户流量商品数据id删除流量分析—用户流量商品数据
	 * @param userAnalysisId 流量分析—用户流量商品数据id
	 */
	void deleteById(Long userAnalysisId);
}
