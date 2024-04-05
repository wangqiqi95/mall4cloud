package com.mall4j.cloud.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
public interface ElPriceProdMapper extends BaseMapper<ElPriceProd> {

	/**
	 * 获取电子价签商品列表
	 * @return 电子价签商品列表
	 */
	List<ElPriceProdVO> getList(@Param("elId") String elId,@Param("prodName") String prodName);

	List<ElPriceProd> getElSpuList(@Param("spuIds") List<Long> spuIds);

	/**
	 * 根据电子价签商品id获取电子价签商品
	 *
	 * @param id 电子价签商品id
	 * @return 电子价签商品
	 */
	ElPriceProd getById(@Param("id") Long id);

	/**
	 * 保存电子价签商品
	 * @param elPriceProd 电子价签商品
	 */
	void save(@Param("elPriceProd") ElPriceProd elPriceProd);
	void saveBatch(@Param("elPriceProds") List<ElPriceProd> elPriceProds);

	/**
	 * 更新电子价签商品
	 * @param elPriceProd 电子价签商品
	 */
	void update(@Param("elPriceProd") ElPriceProd elPriceProd);

	/**
	 * 根据电子价签商品id删除电子价签商品
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
	void deleteByIds(@Param("ids") List<Long> ids);
	void deleteByElId(@Param("elId") Long elId);

	List<String> checkSkuIds(@Param("skuIds") List<String> skuIds);
}
