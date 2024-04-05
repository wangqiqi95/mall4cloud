package com.mall4j.cloud.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.common.product.vo.SpuDetailVO;
import com.mall4j.cloud.product.model.SpuDetail;

import java.util.List;

/**
 * 商品详情信息
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
public interface SpuDetailService extends IService<SpuDetail> {

	/**
	 * 批量保存商品详情信息
	 * @param spuDetailList 商品详情信息
	 */
	void batchSave(List<SpuDetail> spuDetailList);

	/**
	 * 批量更新商品详情信息
	 * @param spuDetailList 商品详情信息
	 */
	void batchUpdate(List<SpuDetail> spuDetailList);

	/**
	 * 根据商品详情信息id删除商品详情信息
	 * @param spuId
	 */
	void deleteById(Long spuId);

	/**
	 * 更新商品详情
	 * @param spuDetailList
	 * @param detailList
	 * @param spuId
	 */
    void update(List<SpuDetail> spuDetailList, List<SpuDetailVO> detailList, Long spuId);
}
