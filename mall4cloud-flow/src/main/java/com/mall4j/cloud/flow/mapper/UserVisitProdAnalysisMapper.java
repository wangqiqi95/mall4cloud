package com.mall4j.cloud.flow.mapper;

import com.mall4j.cloud.flow.model.UserVisitProdAnalysis;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户访问商品数据
 *
 * @author YXF
 * @date 2021-05-21 15:25:19
 */
public interface UserVisitProdAnalysisMapper {

	/**
	 * 获取用户访问商品数据列表
	 *
	 * @return 用户访问商品数据列表
	 */
	List<UserVisitProdAnalysis> list();

	/**
	 * 根据用户访问商品数据id获取用户访问商品数据
	 *
	 * @param userAnalysisId 用户访问商品数据id
	 * @return 用户访问商品数据
	 */
	UserVisitProdAnalysis getByUserAnalysisId(@Param("userAnalysisId") Long userAnalysisId);

	/**
	 * 保存用户访问商品数据
	 *
	 * @param userVisitProdAnalysis 用户访问商品数据
	 */
	void save(@Param("userVisitProdAnalysis") UserVisitProdAnalysis userVisitProdAnalysis);

	/**
	 * 更新用户访问商品数据
	 *
	 * @param userVisitProdAnalysis 用户访问商品数据
	 */
	void update(@Param("userVisitProdAnalysis") UserVisitProdAnalysis userVisitProdAnalysis);

	/**
	 * 根据用户访问商品数据id删除用户访问商品数据
	 *
	 * @param userVisitProdAnalysisId
	 */
	void deleteById(@Param("userVisitProdAnalysisId") Long userVisitProdAnalysisId);

	/**
	 * 批量保存用户访问商品数据
	 *
	 * @param list
	 */
    void saveBatch(@Param("list") List<UserVisitProdAnalysis> list);

	/**
	 * 批量更新用户访问商品数据
	 *
	 * @param list
	 */
	void updateBatch(@Param("list") List<UserVisitProdAnalysis> list);

	/**
	 * 批量删除用户访问商品数据
	 *
	 * @param userAnalysisIds
	 */
	void deleteBatch(@Param("userAnalysisIds") List<Long> userAnalysisIds);

	/**
	 * 根据userAnalysisId列表，获取用户访问商品记录
	 *
	 * @param userAnalysisIds
	 * @return
	 */
	List<UserVisitProdAnalysis> listByUserAnalysisId(@Param("userAnalysisIds") List<Long> userAnalysisIds);
}
