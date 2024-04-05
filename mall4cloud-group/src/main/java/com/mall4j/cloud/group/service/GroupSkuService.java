package com.mall4j.cloud.group.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.group.model.GroupActivity;
import com.mall4j.cloud.group.model.GroupSku;
import com.mall4j.cloud.group.vo.GroupSkuVO;

import java.util.List;

/**
 * 拼团活动商品规格
 *
 * @author YXF
 * @date 2021-03-20 10:39:32
 */
public interface GroupSkuService {

	/**
	 * 分页获取拼团活动商品规格列表
	 * @param pageDTO 分页参数
	 * @return 拼团活动商品规格列表分页数据
	 */
	PageVO<GroupSku> page(PageDTO pageDTO);

	/**
	 * 根据拼团活动商品规格id获取拼团活动商品规格
	 *
	 * @param groupSkuId 拼团活动商品规格id
	 * @return 拼团活动商品规格
	 */
	GroupSku getByGroupSkuId(Long groupSkuId);

	/**
	 * 保存拼团活动商品规格
	 * @param groupSku 拼团活动商品规格
	 */
	void save(GroupSku groupSku);

	/**
	 * 更新拼团活动商品规格
	 * @param groupSku 拼团活动商品规格
	 */
	void update(GroupSku groupSku);

	/**
	 * 根据拼团活动商品规格id删除拼团活动商品规格
	 * @param groupSkuId 拼团活动商品规格id
	 */
	void deleteById(Long groupSkuId);

	/**
	 * 批量保存
	 * @param groupSkuList
	 * @param groupActivityId
	 */
	void batchSave(List<GroupSku> groupSkuList, Long groupActivityId);

	/**
	 * 根据活动id，删除团购sku信息
	 * @param groupActivityId
	 */
    void removeGroupSkuByGroupActivityId(Long groupActivityId);

	/**
	 * 批量更新
	 * @param groupSkuList
	 * @param groupActivityId
	 */
	void batchUpdate(List<GroupSku> groupSkuList, Long groupActivityId);

	/**
	 * 根据活动id，获取团购sku列表信息
	 * @param groupActivityId
	 * @return
	 */
    List<GroupSkuVO> listByGroupActivityId(Long groupActivityId);
}
