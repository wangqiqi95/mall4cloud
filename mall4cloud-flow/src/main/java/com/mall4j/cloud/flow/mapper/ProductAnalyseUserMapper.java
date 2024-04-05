package com.mall4j.cloud.flow.mapper;

import com.mall4j.cloud.flow.model.ProductAnalyseUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 流量分析—页面数据统计表
 *
 * @author YXF
 * @date 2021-05-21 15:25:19
 */
public interface ProductAnalyseUserMapper {

	/**
	 * 获取流量分析—页面数据统计表列表
	 *
	 * @return 流量分析—页面数据统计表列表
	 */
	List<ProductAnalyseUser> list();

	/**
	 * 根据流量分析—页面数据统计表id获取流量分析—页面数据统计表
	 *
	 * @param productAnalyseUserId 流量分析—页面数据统计表id
	 * @return 流量分析—页面数据统计表
	 */
	ProductAnalyseUser getByProductAnalyseUserId(@Param("productAnalyseUserId") Long productAnalyseUserId);

	/**
	 * 保存流量分析—页面数据统计表
	 *
	 * @param productAnalyseUser 流量分析—页面数据统计表
	 */
	void save(@Param("productAnalyseUser") ProductAnalyseUser productAnalyseUser);

	/**
	 * 更新流量分析—页面数据统计表
	 *
	 * @param productAnalyseUser 流量分析—页面数据统计表
	 */
	void update(@Param("productAnalyseUser") ProductAnalyseUser productAnalyseUser);

	/**
	 * 根据流量分析—页面数据统计表id删除流量分析—页面数据统计表
	 *
	 * @param productAnalyseUserId
	 */
	void deleteById(@Param("productAnalyseUserId") Long productAnalyseUserId);

	/**
	 * 批量保存
	 *
	 * @param productAnalyseUserList
	 */
    void saveBatch(@Param("productAnalyseUserList") List<ProductAnalyseUser> productAnalyseUserList);

	/**
	 * 批量更新
	 *
	 * @param productAnalyseUserList
	 */
	void updateBatch(@Param("productAnalyseUserList") List<ProductAnalyseUser> productAnalyseUserList);

	/**
	 * 删除商品统计数据
	 *
	 * @param spuId 商品id
	 */
    void deleteSpuDataBySpuId(@Param("spuId") Long spuId);
}
