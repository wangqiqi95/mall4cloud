package com.mall4j.cloud.group.mapper;

import com.mall4j.cloud.group.model.GroupSku;
import com.mall4j.cloud.group.vo.GroupSkuVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 拼团活动商品规格
 *
 * @author YXF
 * @date 2021-03-20 10:39:32
 */
public interface GroupSkuMapper {

	/**
	 * 获取拼团活动商品规格列表
	 *
	 * @return 拼团活动商品规格列表
	 */
	List<GroupSku> list();

	/**
	 * 根据拼团活动商品规格id获取拼团活动商品规格
	 *
	 * @param groupSkuId 拼团活动商品规格id
	 * @return 拼团活动商品规格
	 */
	GroupSku getByGroupSkuId(@Param("groupSkuId") Long groupSkuId);

	/**
	 * 保存拼团活动商品规格
	 *
	 * @param groupSku 拼团活动商品规格
	 */
	void save(@Param("groupSku") GroupSku groupSku);

	/**
	 * 更新拼团活动商品规格
	 *
	 * @param groupSku 拼团活动商品规格
	 */
	void update(@Param("groupSku") GroupSku groupSku);

	/**
	 * 根据拼团活动商品规格id删除拼团活动商品规格
	 *
	 * @param groupSkuId
	 */
	void deleteById(@Param("groupSkuId") Long groupSkuId);

	/**
	 * 批量更新
	 *
	 * @param groupSkuList
	 */
    void batchSave(@Param("groupSkuList") List<GroupSku> groupSkuList);

	/**
	 * 根据活动id，删除团购sku信息
	 *
	 * @param groupActivityId
	 */
	void removeGroupSkuByGroupActivityId(@Param("groupActivityId") Long groupActivityId);

	/**
	 * 批量更新
	 *
	 * @param groupSkuList
	 */
	void batchUpdate(@Param("groupSkuList") List<GroupSku> groupSkuList);

	/**
	 * 根据活动id，获取团购sku列表信息
	 *
	 * @param groupActivityId
	 * @return
	 */
    List<GroupSkuVO> listByGroupActivityId(@Param("groupActivityId") Long groupActivityId);

}
