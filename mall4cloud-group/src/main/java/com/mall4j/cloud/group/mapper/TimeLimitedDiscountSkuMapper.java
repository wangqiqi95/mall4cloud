package com.mall4j.cloud.group.mapper;

import com.mall4j.cloud.api.group.feign.vo.TimeDiscountActivityVO;
import com.mall4j.cloud.group.dto.TimeLimitedDiscountSkuDTO;
import com.mall4j.cloud.group.model.TimeLimitedDiscountSku;
import com.mall4j.cloud.group.vo.TimeLimitedDiscountSkuVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 限时调价活动 sku价格
 *
 * @author FrozenWatermelon
 * @date 2022-03-10 13:29:18
 */
public interface TimeLimitedDiscountSkuMapper {

	/**
	 * 获取限时调价活动 sku价格列表
	 * @return 限时调价活动 sku价格列表
	 */
	List<TimeLimitedDiscountSku> list();

	/**
	 * 根据限时调价活动 sku价格id获取限时调价活动 sku价格
	 *
	 * @param id 限时调价活动 sku价格id
	 * @return 限时调价活动 sku价格
	 */
	TimeLimitedDiscountSku getById(@Param("id") Long id);

	/**
	 * 保存限时调价活动 sku价格
	 * @param timeLimitedDiscountSku 限时调价活动 sku价格
	 */
	void save(@Param("timeLimitedDiscountSku") TimeLimitedDiscountSku timeLimitedDiscountSku);

	/**
	 * 更新限时调价活动 sku价格
	 * @param timeLimitedDiscountSku 限时调价活动 sku价格
	 */
	void update(@Param("timeLimitedDiscountSku") TimeLimitedDiscountSku timeLimitedDiscountSku);

	/**
	 * 根据限时调价活动 sku价格id删除限时调价活动 sku价格
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	void insertBatch(@Param("list") List<TimeLimitedDiscountSkuDTO> list);

	void removeActivityId(@Param("activityId")Integer activityId);

    List<TimeLimitedDiscountSkuVO> selectByActivityId(@Param("activityId")Integer activityId,@Param("spuId")Integer spuId);

    List<TimeLimitedDiscountSkuVO> selectByActivityAndSpuIds(@Param("activityId")Integer activityId,@Param("spuIds")List<Long> spuIds);

    List<TimeDiscountActivityVO> selectBySkuIds(@Param("activityids")List activityids, @Param("spuIds")List spuIds);
}
