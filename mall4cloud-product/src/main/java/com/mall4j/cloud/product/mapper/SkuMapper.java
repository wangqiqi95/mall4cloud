package com.mall4j.cloud.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.api.product.vo.SkuCodeVO;
import com.mall4j.cloud.common.database.util.PageAdapter;
import com.mall4j.cloud.common.product.dto.SkuDTO;
import com.mall4j.cloud.common.product.vo.SkcVO;
import com.mall4j.cloud.common.product.vo.SkuAddrVO;
import com.mall4j.cloud.common.product.vo.SkuVO;
import com.mall4j.cloud.product.dto.SkuPriceDTO;
import com.mall4j.cloud.product.model.Sku;
import com.mall4j.cloud.product.vo.SkuVo;
import com.mall4j.cloud.product.vo.SpuSkuVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * sku信息
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
public interface SkuMapper extends BaseMapper<Sku> {

	/**
	 * 保存sku信息
	 *
	 * @param sku sku信息
	 */
	void saveSku(@Param("sku") Sku sku);

	/**
	 * 更新sku信息
	 *
	 * @param sku sku信息
	 */
	void updateSku(@Param("sku") Sku sku);

	/**
	 * 根据sku信息id删除sku信息
	 *
	 * @param skuId
	 */
	void deleteById(@Param("skuId") Long skuId);

	/**
	 * 根据spuId获取sku信息
	 *
	 * @param spuId id
	 * @param storeId
	 * @return 返回sku信息
	 */
	List<SkuVO> listSkuWithAttrBySpuId(@Param("spuId") Long spuId, @Param("storeId") Long storeId);

	/**
	 *
	 * @param spuId		id
	 * @param storeId	门店ID
	 * @return
	 */
	List<SkcVO> listSkcWithAttrBySpuId(@Param("spuId") Long spuId, @Param("storeId") Long storeId);

	/**
	 * 批量保存
	 *
	 * @param skuList
	 */
	void saveSkuBatch(@Param("skuList") List<SkuDTO> skuList);

	/**
	 * 根据spuId删除sku信息
	 *
	 * @param spuId spuId
	 */
	void updateBySpuId(Long spuId);

	/**
	 * 批量修改
	 *
	 * @param skus 修改后的信息
	 */
	void updateSkuBatch(@Param("skus") List<Sku> skus);

	void updateSkuBatchByPriceCode(@Param("skus") List<Sku> skus);


	/**
	 * 根据skuid获取sku信息
	 *
	 * @param skuId
	 * @return
	 */
	SkuVO getSkuBySkuId(@Param("skuId") Long skuId);

    SkuVO getSkuBySkuCode(@Param("skuCode") String skuCode);

	SkuVO getSkuBySkuPriceCode(@Param("priceCode") String priceCode);

	List<Sku> getSkuByPriceCode(@Param("priceCode") String priceCode);

	/**
	 * 获取excel导出sku数据列表
	 *
	 * @param spuIds
	 * @return
	 */
	List<SkuVO> excelSkuList(@Param("spuIds") List<Long> spuIds,
							 @Param("storeId") Long storeId,
							 @Param("pageAdapter") PageAdapter pageAdapter);

	/**
	 * 获取excel导出sku数据列表
	 *
	 * @param spuIds
	 * @return
	 */
	List<SkuVO> excelMainShopAllSkuList(@Param("spuIds") List<Long> spuIds,
							 @Param("storeId") Long storeId,
							 @Param("pageAdapter") PageAdapter pageAdapter);

	/**
	 * 获取指定sku的价格
	 *
	 * @param skuIds
	 * @return
	 */
    List<SkuVO> listSkuPriceByIds(@Param("skuIds") List<Long> skuIds);

	/**
	 * 获取指定sku的code
	 *
	 * @param skuIds
	 * @return
	 */
	List<SkuVO> listSkuCodeByIds(@Param("skuIds") List<Long> skuIds);


	/**
	 * 根据skuId列表获取商品订单修改地址所需信息
	 * @param skuIds skuId列表
	 * @return sku列表
	 */
	List<SkuAddrVO> listSpuDetailByIds(@Param("skuIds") List<Long> skuIds);

	SkuVO getSkuBySkuIdAndStoreId(@Param("spuId") Long spuId,@Param("skuId") Long skuId,@Param("storeId") Long storeId);

    SkuCodeVO getCodeBySkuId(@Param("skuId") Long skuId);

    List<SkuVO> getSpuSkuInfo(@Param("spuId") Long spuId,@Param("storeId") Long storeId);

    SkuVO getSkuByCode(@Param("code") String code);

	List<SkuVO> getSkcByCode(@Param("code") String code);

    List<SkuVO> getSkuBySpuId(@Param("spuId") Long spuId);

    List<Long> getAppSkuBySkuIdList(@Param("spuIdList") List<Long> spuIdList);

	List<SkuPriceDTO> getAppSkuPriceBySkuIdList(@Param("spuIdList") List<Long> spuIdList, @Param("storeId") Long storeId);

	List<SpuSkuVo> getSpuSkuVos(@Param("channelName") String channelName);

	List<Sku> getCancelChannelPirceSkus(@Param("channelName") String channelName);

	List<SkuVo> getPriceFeeErrorSkus(@Param("discount") double discount);

	/**
	 * 已上架商品
	 * @return
	 */
	List<Long> getSkuIdListingList();

}
