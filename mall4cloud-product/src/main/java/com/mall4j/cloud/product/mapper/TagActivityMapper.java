package com.mall4j.cloud.product.mapper;

import com.mall4j.cloud.product.dto.SpuTagDTO;
import com.mall4j.cloud.product.dto.TagActivityPageDTO;
import com.mall4j.cloud.product.model.SpuTag;
import com.mall4j.cloud.product.model.TagActivity;
import com.mall4j.cloud.product.vo.SpuTagVO;
import com.mall4j.cloud.product.vo.TagActivityVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 营销标签活动表
 *
 * @author hwy
 * @date 2022-03-12 14:28:10
 */
public interface TagActivityMapper {

	/**
	 * 根据标签名称分页获取营销标签活动表列表
	 * @param filter 筛选参数
	 * @return 营销标签活动表列表分页数据
	 */
	List<TagActivityVO> list(@Param("et") TagActivityPageDTO filter);

	/**
	 * 根据营销标签活动表id获取营销标签活动表
	 * @param id 营销标签活动表id
	 * @return 营销标签活动表
	 */
	TagActivity getById(@Param("id") Long id);

	/**
	 * 保存营销标签活动表
	 * @param tagActivity 营销标签活动表
	 */
	void save(@Param("et") TagActivity tagActivity);

	/**
	 * 更新营销标签活动表
	 * @param tagActivity 营销标签活动表
	 */
	void update(@Param("et") TagActivity tagActivity);

	/**
	 * 根据营销标签活动表id删除营销标签活动表
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
	/**
	 * 小程序前端根据商品id查询权重最高的活动标签
	 * @param storeId 商品id
	 * @param storeId 商店
	 * @return
	 */
    TagActivity getTagBySpuId(@Param("spuId") Long spuId,@Param("storeId")  Long storeId);
	/**
	 * 检测活动的开始时间和结束时间，将状态更新
	 */
	void updateTagActivityStatus();
}
