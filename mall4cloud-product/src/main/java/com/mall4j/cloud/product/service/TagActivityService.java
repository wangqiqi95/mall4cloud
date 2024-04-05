package com.mall4j.cloud.product.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
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
public interface TagActivityService {

	/**
	 * 根据标签名称分页获取营销标签活动表列表
	 * @param filter 筛选参数
	 * @return 营销标签活动表列表分页数据
	 */
	PageVO<TagActivityVO> page(PageDTO pageDTO, TagActivityPageDTO filter);


	/**
	 * 根据营销标签活动表id获取营销标签活动表
	 * @param id 营销标签活动表id
	 * @return 营销标签活动表
	 */
	TagActivity getById(@Param("id") Long id);

	void removeCache(Long id);

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
	 * 创建或者更新营销标签活动
	 * @param tagActivity 标签活动信息
	 * @param stores  关联商店
	 * @param spuList 关联商品
	 */
    void createOrUpdateTagActivity(TagActivity tagActivity, List<Long> stores, List<Long> spuList);

	/**
	 * 小程序前端根据商品id查询权重最高的活动标签
 	 * @param spuId 商品id
	 * @param storeId 商店
	 * @return
	 */
	TagActivity getTagBySpuId(Long spuId, Long storeId);

	/**
	 * 检测活动的开始时间和结束时间，将状态更新
	 */
	void updateTagActivityStatus();

    void addShops(Long id, List<Long> shops);

    void removeCategoryCache(List<Long> spuList,List<Long> stores);
}
