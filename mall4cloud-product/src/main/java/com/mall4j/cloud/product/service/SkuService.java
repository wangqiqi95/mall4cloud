package com.mall4j.cloud.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.api.product.vo.SkuCodeVO;
import com.mall4j.cloud.api.product.vo.SoldSpuExcelVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageAdapter;
import com.mall4j.cloud.common.product.dto.SkuDTO;
import com.mall4j.cloud.common.product.dto.SpuDTO;
import com.mall4j.cloud.common.product.vo.*;
import com.mall4j.cloud.common.product.vo.app.SkuAppVO;
import com.mall4j.cloud.product.dto.SkuPriceDTO;
import com.mall4j.cloud.product.model.Sku;
import com.mall4j.cloud.product.vo.SpuExcelVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * sku信息
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
public interface SkuService extends IService<Sku> {

	/**
	 * 保存sku信息
	 * @param spu 商品id
	 * @param skuList sku信息
	 */
	void save(Long spu, List<SkuDTO> skuList);

	/**
	 * 更新sku信息
	 * @param spuId spuId
	 * @param skuList sku列表信息
	 */
	void update(Long spuId, List<SkuDTO> skuList);

	/**
	 * 根据sku信息id删除sku信息
	 * @param skuId
	 */
	void deleteById(Long skuId);

	/**
	 *  根据商品id获取商品中的sku列表（将会被缓存起来）
	 * @param spuId id
	 * @param storeId
	 * @return 返回sku信息
	 */
	List<SkuVO> listSkuWithAttrBySpuId(Long spuId, Long storeId);

	/**
	 *  根据商品id获取商品中的skc列表（将会被缓存起来）
	 * @param spuId		id
	 * @param storeId	门店ID
	 * @return 返回skc-款色信息
	 */
	List<SkcVO> listSkcWithAttrBySpuId(Long spuId, Long storeId);

	/**
	 * 根据spuId删除sku信息
	 * @param spuId 商品id
	 */
	void deleteBySpuId(Long spuId);

	/**
	 * 根据skuId获取sku信息
	 * @param skuId skuId
	 * @return sku信息
	 */
    SkuVO getSkuBySkuId(Long skuId);

    SkuVO getSkuBySkuCode(String skuCode);

	/**
	 * 更新sku金额或者库存信息
	 * @param spuDTO
	 */
	void updateAmountOrStock(SpuDTO spuDTO);

	/**
	 * 获取商品详情需要的的sku列表（包括sku库存信息等）（仅获取启用状态）
	 * @param spuId
	 * @param storeId
	 * @return
	 */
	List<SkuAppVO> getSpuDetailSkuInfo(Long spuId, Long storeId);

	/**
	 * 拼接sku属性信息
	 * @param spuSkuAttrValueList
	 * @return
	 */
    String spliceProperties(List<SpuSkuAttrValueVO> spuSkuAttrValueList);

	/**
	 * 从缓存中获取sku的所有信息，包括attr，和库存
	 * @param spuId 商品id
	 * @param enable 是否是启用状态的sku
	 * @param storeId
	 * @return
	 */
	List<SkuVO> listSkuAllInfoBySpuId(Long spuId, boolean enable, Long storeId);

	/**
	 * 根据SpuId获取款色信息
	 * @param spuId		商品id
	 * @param enable	是否是启用状态的sku
	 * @param storeId	门店ID
	 * @return
	 */
	List<SkcVO> listSkcAllInfoBySpuId(Long spuId, boolean enable, Long storeId);

	/**
	 * 获取excel导出sku数据列表
	 * @param spuIds
	 * @return
	 */
//    List<SpuExcelVO> excelSkuList(List<Long> spuIds, Long storeId, PageDTO pageDTO);
    List<SoldSpuExcelVO> excelSkuList(List<Long> spuIds, Long storeId, PageAdapter pageAdapter,boolean isAllMainShop);

	/**
	 * 获取指定sku的价格
	 * @param skuIds
	 * @return
	 */
	List<SkuVO> listSkuPriceByIds(List<Long> skuIds);

	/**
	 * 获取指定sku的code
	 * @param skuIds
	 * @return
	 */
	List<SkuVO> listSkuCodeByIds(List<Long> skuIds);




	/**
	 * 根据skuId列表获取商品订单修改地址所需信息
	 * @param skuIds skuId列表
	 * @return sku列表
	 */
	List<SkuAddrVO> listSpuDetailByIds(List<Long> skuIds);

	SkuVO getSkuBySkuIdAndStoreId(Long spuId,Long skuId, Long storeId);

    SkuCodeVO getCodeBySkuId(Long skuId);

    List<SkuAppVO> getSpuSkuInfo(Long spuId, Long storeId);

    SkuVO getSkuByCode(String skuCode);

	List<SkuVO> getSkcByCode(String skcCode);

    List<SkuVO> getSkuBySpuId(Long spuId);

    List<Long> getAppSkuBySkuIdList(List<Long> spuIdList);

    List<SkuPriceDTO> getAppSkuPriceBySkuIdList(List<Long> spuIdList, Long storeId);

	List<SkuAppVO> getSpuSkuCaseStock(Long spuId, Long storeId);

	/**
	 * 价格逻辑判断
	 * @param spuId
	 * @param storeId
	 * @param skuProtectVOS
	 * @return
	 */
	List<Long> checkSkuStorePrice(Long spuId, Long storeId,List<SkuProtectVO> skuProtectVOS);

	List<Sku> getSkuByPriceCode(String priceCode);

	List<SkuVO> listSkusBySpuId(List<Long> spuIds);

	Sku getSkuBySkuCodeCach(String skuCode);

	List<SkuVO> getSkusByPriceCodeList(List<String> priceCodes);
}
