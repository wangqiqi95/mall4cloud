package com.mall4j.cloud.product.service;

import com.mall4j.cloud.product.dto.ElPriceProdDTO;
import com.mall4j.cloud.product.model.ElPriceProd;
import com.mall4j.cloud.product.vo.ElPriceProdVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 电子价签商品
 *
 * @author FrozenWatermelon
 * @date 2022-03-27 22:24:29
 */
public interface ElPriceProdService {

	/**
	 * 获取电子价签商品列表
	 */
	List<ElPriceProdVO> getList(String elId,String prodName);

	/**
	 * 根据电子价签商品id获取电子价签商品
	 *
	 * @param id 电子价签商品id
	 * @return 电子价签商品
	 */
	ElPriceProd getById(Long id);

	/**
	 * 保存电子价签商品
	 * @param elPriceProd 电子价签商品
	 */
	void save(ElPriceProd elPriceProd);
	void saveBatch(Long elId,List<ElPriceProdDTO> elPriceProdDTOS);

	/**
	 * 更新电子价签商品
	 * @param elPriceProd 电子价签商品
	 */
	void update(ElPriceProd elPriceProd);

	/**
	 * 根据电子价签商品id删除电子价签商品
	 * @param id 电子价签商品id
	 */
	void deleteById(Long id);
	//批量删除
	void deleteByIds(List<Long> ids);
	//清空
	void deleteByElId(Long elId);


    /**
     * 检查skuids是否需要上传
     * @param skuIds
     * @return
     */
	List<Long> checkSkus(List<Long> skuIds);

	List<ElPriceProd> getElSpuList(List<Long> spuIds);
}
