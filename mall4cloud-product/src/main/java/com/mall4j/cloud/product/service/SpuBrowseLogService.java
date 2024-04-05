package com.mall4j.cloud.product.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.product.dto.SpuBrowseLogDTO;
import com.mall4j.cloud.product.model.SpuBrowseLog;
import com.mall4j.cloud.product.vo.SpuBrowseLogVO;

/**
 * 商品浏览记录表
 *
 * @author YXF
 * @date 2021-03-19 14:28:14
 */
public interface SpuBrowseLogService {

	/**
	 * 分页获取商品浏览记录表列表
	 * @param pageDTO 分页参数
	 * @return 商品浏览记录表列表分页数据
	 */
	PageVO<SpuBrowseLogVO> page(PageDTO pageDTO);

	/**
	 * 保存商品浏览记录表
	 * @param spuBrowseLog 商品浏览记录表
	 */
	void save(SpuBrowseLog spuBrowseLog);

	/**
	 * 更新商品浏览记录表
	 * @param spuBrowseLog 商品浏览记录表
	 */
	void update(SpuBrowseLog spuBrowseLog);

	/**
	 * 根据商品id获取浏览记录
	 * @param spuId
	 * @param userId
	 * @return
	 */
	SpuBrowseLog getBySpuIdAndUserId(Long spuId, Long userId);

	/**
	 * 删除商品浏览记录
	 * @param spuBrowseLogId
	 */
	void deleteById(Long spuBrowseLogId);

	/**
	 * 单间、批量删除商品浏览记录
	 * @param spuBrowseLogDTO
	 */
	void delete(SpuBrowseLogDTO spuBrowseLogDTO);

	/**
	 * 获取用户浏览的分类
	 * @param spuType
	 * @return
	 */
	Long recommendCategoryId(Integer spuType);

	/**
	 * 获取用户该商品当天的浏览记录
	 * @param spuBrowseLogDTO
	 * @return
	 */
	SpuBrowseLog getCurrentLogBySpuIdAndUserId(SpuBrowseLogDTO spuBrowseLogDTO);
}
