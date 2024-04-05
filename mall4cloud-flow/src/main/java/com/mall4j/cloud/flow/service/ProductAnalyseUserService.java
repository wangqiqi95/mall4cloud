package com.mall4j.cloud.flow.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.flow.model.ProductAnalyseUser;

/**
 * 流量分析—页面数据统计表
 *
 * @author YXF
 * @date 2021-05-21 15:25:19
 */
public interface ProductAnalyseUserService {

	/**
	 * 分页获取流量分析—页面数据统计表列表
	 * @param pageDTO 分页参数
	 * @return 流量分析—页面数据统计表列表分页数据
	 */
	PageVO<ProductAnalyseUser> page(PageDTO pageDTO);

	/**
	 * 根据流量分析—页面数据统计表id获取流量分析—页面数据统计表
	 *
	 * @param productAnalyseUserId 流量分析—页面数据统计表id
	 * @return 流量分析—页面数据统计表
	 */
	ProductAnalyseUser getByProductAnalyseUserId(Long productAnalyseUserId);

	/**
	 * 保存流量分析—页面数据统计表
	 * @param productAnalyseUser 流量分析—页面数据统计表
	 */
	void save(ProductAnalyseUser productAnalyseUser);

	/**
	 * 更新流量分析—页面数据统计表
	 * @param productAnalyseUser 流量分析—页面数据统计表
	 */
	void update(ProductAnalyseUser productAnalyseUser);

	/**
	 * 根据流量分析—页面数据统计表id删除流量分析—页面数据统计表
	 * @param productAnalyseUserId 流量分析—页面数据统计表id
	 */
	void deleteById(Long productAnalyseUserId);
}
