package com.mall4j.cloud.product.mapper;

import com.mall4j.cloud.product.dto.SpuBrowseLogDTO;
import com.mall4j.cloud.product.model.SpuBrowseLog;
import com.mall4j.cloud.product.vo.SpuBrowseLogVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 商品浏览记录表
 *
 * @author YXF
 * @date 2021-03-19 14:28:14
 */
public interface SpuBrowseLogMapper {

	/**
	 * 获取商品浏览记录表列表
	 *
	 * @param userId
	 * @return 商品浏览记录表列表
	 */
	List<SpuBrowseLogVO> spuBrowseList(@Param("userId") Long userId);

	/**
	 * 保存商品浏览记录表
	 *
	 * @param spuBrowseLog 商品浏览记录表
	 */
	void save(@Param("spuBrowseLog") SpuBrowseLog spuBrowseLog);

	/**
	 * 更新商品浏览记录表
	 *
	 * @param spuBrowseLog 商品浏览记录表
	 */
	void update(@Param("spuBrowseLog") SpuBrowseLog spuBrowseLog);

	/**
	 * 获取用户的商品浏览记录信息
	 *
	 * @param spuId
	 * @param userId
	 * @return
	 */
	SpuBrowseLog getBySpuIdAndUserId(@Param("spuId") Long spuId, @Param("userId") Long userId);

	/**
	 * 批量更新记录状态
	 *
	 * @param spuBrowseLogIds
	 * @param userId
	 * @param status
	 */
	void batchUpdateStatus(@Param("spuBrowseLogIds") List<Long> spuBrowseLogIds, @Param("userId") Long userId, @Param("status") Integer status);

	/**
	 * 获取推荐的分类id
	 *
	 * @param userId  用户id
	 * @param limit   指定数量
	 * @param spuType 商品类型
	 * @return 分类id
	 */
	Long recommendCategoryId(@Param("userId") Long userId, @Param("limit") Integer limit, @Param("spuType") Integer spuType);

	/**
	 * 获取用户该商品当天的浏览记录
	 *
	 * @param spuBrowseLogDTO
	 * @param startTime 开始时间
	 * @return 商品浏览信息
	 */
    SpuBrowseLog getCurrentLogBySpuIdAndUserId(@Param("spuBrowseLog") SpuBrowseLogDTO spuBrowseLogDTO, @Param("startTime") Date startTime);
}
