package com.mall4j.cloud.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.api.platform.dto.OfflineHandleEventDTO;
import com.mall4j.cloud.api.platform.vo.OfflineHandleEventVO;
import com.mall4j.cloud.api.product.bo.SpuSimpleBO;
import com.mall4j.cloud.api.product.dto.*;
import com.mall4j.cloud.api.product.vo.StdPushSpuVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.product.bo.EsProductBO;
import com.mall4j.cloud.common.product.dto.ProductSearchDTO;
import com.mall4j.cloud.common.product.dto.SpuDTO;
import com.mall4j.cloud.common.product.dto.SpuSearchAttrDTO;
import com.mall4j.cloud.common.product.vo.SpuActivityAppVO;
import com.mall4j.cloud.common.product.vo.SpuCodeVo;
import com.mall4j.cloud.common.product.vo.SpuVO;
import com.mall4j.cloud.common.product.vo.app.SpuAppVO;
import com.mall4j.cloud.common.product.vo.search.SpuCommonVO;
import com.mall4j.cloud.product.dto.SpuAppPageVO;
import com.mall4j.cloud.product.dto.SpuCategoryUpdateDTO;
import com.mall4j.cloud.product.dto.SpuIphStatusDTO;
import com.mall4j.cloud.product.dto.SpuPageSearchDTO;
import com.mall4j.cloud.product.model.Spu;
import com.mall4j.cloud.product.model.SpuExtension;
import com.mall4j.cloud.product.vo.SpuExcelImportDataVO;
import com.mall4j.cloud.product.vo.SpuPageVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

/**
 * spu信息
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
public interface SpuService extends IService<Spu> {

	/**
	 * 分页获取spu信息列表
	 * @param pageDTO 分页参数
	 * @param spuDTO
	 * @return spu信息列表分页数据
	 */
	PageVO<SpuVO> page(PageDTO pageDTO, SpuPageSearchDTO spuDTO);

	/**
	 * spu基本信息(国际化信息还没处理，如需用到商品名等国际化的信息，调用接口后需要处理)
	 *
	 * @param spuId spu信息id
	 * @return spu信息
	 */
	SpuVO getBySpuId(Long spuId);

	/**
	 * spu扩展信息
	 *
	 * @param spuId spu信息id
	 * @return spu信息
	 */
	SpuExtension getSpuExtension(Long spuId);

	/**
	 * 保存spu信息
	 * @param spuDTO spu信息
	 */
	void save(SpuDTO spuDTO);

	/**
	 * 更新spu信息
	 * @param spuDTO spu信息
	 */
	void updateSpu(SpuDTO spuDTO);

	/**
	 * 根据spu信息id删除spu信息
	 * @param spuId
	 */
	void deleteById(Long spuId);

	/**
	 * 根据spuId列表， 批量清除缓存
	 * @param spuIds id
	 */
	void batchRemoveSpuActivityCache(List<Long> spuIds);

	/**
	 * 改变商品状态（上下架）
	 * @param spuId
	 * @param status
	 */
	void changeSpuStatus(Long spuId, Integer status);

	/**
	 * 批量改变商品状态（上下架）
	 * @param spuIds
	 * @param status
	 */
	void batchChangeSpuStatus(List<Long> spuIds, Integer status);

	/**
	 * 更新商品的信息
	 * @param spuDTO
	 */
	void updateSpuOrSku(SpuDTO spuDTO);

	/**
	 * 更新spu表（canal监听后，会发送更新的消息，更新es中的数据）
	 * @param spuIds
	 * @param categoryIds
	 * @param shopIds
	 */
    void updateSpuUpdateTime(List<Long> spuIds, List<Long> categoryIds, List<Long> shopIds);

	/**
	 * 根据spuId获取商品信息
	 * @param spuId
	 * @return 商品信息
	 */
	EsProductBO loadEsProductBO(Long spuId);

	/**
	 * 获取 spuId列表
	 * @param shopCategoryIds 店铺分类id列表
	 * @param categoryIds 平台分类Id列表
	 * @param brandId 品牌id
	 * @param shopId 店铺id
	 * @return spuId列表
	 */
	List<Long> getSpuIdsBySpuUpdateDTO(List<Long> shopCategoryIds, List<Long> categoryIds, Long brandId, Long shopId);

	/**
	 * 商品活动信息
	 * @param shopId
	 * @param spuId
	 * @return
	 */
	SpuActivityAppVO spuActivityBySpuId(Long shopId, Long spuId);

	/**
	 * 根据店铺id，获取店铺spu列表
	 * @param shopId
	 * @return
	 */
	List<Long> listSpuIdsByShopId(Long shopId);

	/**
	 * 下线店铺中的商品
	 * @param shopId
	 */
    void offlineSpuByShopId(Long shopId);

	/**
	 * 获取spu列表
	 * @param spu
	 * @return
	 */
	List<SpuVO> list(SpuPageSearchDTO spu);

	/**
	 * 更新商品信息
	 * @param spu
	 */
	void updateSpu(Spu spu);

	/**
	 * 平台下架优惠券
	 * @param offlineHandleEventDto
	 */
	void offline(OfflineHandleEventDTO offlineHandleEventDto);

	/**
	 * 获取下线的事件记录
	 * @param spuId
	 * @return
	 */
	OfflineHandleEventVO getOfflineHandleEvent(Long spuId);

	/**
	 * 平台审核商家提交的申请
	 * @param offlineHandleEventDto
	 */
	void audit(OfflineHandleEventDTO offlineHandleEventDto);

	/**
	 * 违规活动提交审核
	 * @param offlineHandleEventDto
	 */
	void auditApply(OfflineHandleEventDTO offlineHandleEventDto);

	/**
	 * 根据spuId或者为空直接获取可以参与秒杀活动的商品列表
	 * @param spuId spuId
	 * @param shopId 店铺id
	 * @return 商品信息
	 */
	List<SpuVO> listCanSeckillProd(Long spuId, Long shopId);
//
//	/**
//	 * 根据spuId或者为空直接获取可以参与秒杀活动的商品分页列表
//	 * @param pageDTO 分页信息
//	 * @param spuDTO 店铺id
//	 * @return 商品分页列表
//	 */
//	PageVO<SpuVO> pageCanSeckillProd(PageDTO pageDTO, SpuDTO spuDTO);

	/**
	 * 根据运费id获取商品数量
	 * @param transportId
	 * @return
	 */
    Integer countByTransportId(Long transportId);

	/**
	 * 变为普通商品
	 * @param spuIds
	 */
	void changeToNormalSpu(List<Long> spuIds);

	/**
	 * 根据分类id与店铺id查询使用该分类的商品数量
	 * @param categoryId
	 * @param shopId
	 * @return
	 */
	int countByCategoryAndShopId(Long categoryId, Long shopId);

	/**
	 * 商品状态发生改变时，需要处理的事件
	 *
	 * @param spuIds 商品id
	 */
    void handleStatusChange(List<Long> spuIds);

	/**
	 * 根据用户id查询商品收藏数量
	 * @param userId 用户id
	 * @return 商品收藏数量
	 */
	Integer countByUserId(Long userId);

	/**
	 * 根据商品id列表获取商品名称列表
	 * @param spuIds
	 * @return
	 */
	List<SpuVO> listSpuNameBySpuIds(List<Long> spuIds);


	/**
	 * 批量改变商品状态（上下架）
	 * @param cidList
	 * @param status
	 * @param shopId
	 */
    void batchChangeSpuStatusByCidListAndShopId(List<Long> cidList, Integer status, Long shopId);

	/**
	 * 根据商品id列表获取商品列表
	 * @param spuIds
	 * @return
	 */
    List<SpuVO> listSpuBySpuIds(List<Long> spuIds);

    /**
     * 获取商铺列表分页
     * @param param
     * @return
     */
    PageVO<SpuVO> pageList(SpuListDTO param);

	/**
	 * 根据商品code列表获取商品列表
	 *
	 * @param spuCodes
	 * @return
	 */
    List<SpuCodeVo> listSpuBySpuCodes(List<String> spuCodes);

	List<SpuPageVO> listSpu(SpuPageSearchDTO searchDTO);

	/**
	 * 更新商品数据
	 */
	void verifySpuData();

	/**
	 * 获取商品信息
	 * @param spuId
	 * @param storeId
	 * @return
	 */
    SpuAppVO prodInfo(Long spuId, Long storeId);

	/**
	 * 根据店铺id列表下架商品
	 * @param type
	 * @param shopIds
	 */
	void offlineSpuByShopIds(Integer type, List<Long> shopIds);

	/**
	 * 根据商品id设置商品是否置顶
	 * @param spuId
	 */
    void toTopBySpuId(Long spuId);

	/**
	 * 根据基本信息获取商品列表
	 * @param spuSimpleBO
	 * @return
	 */
	List<SpuSimpleBO> listSimple(SpuSimpleBO spuSimpleBO);

	/**
	 * 移除商品缓存通过商品id
	 * @param spuId 商品id
	 */
	void removeSpuCacheBySpuId(Long spuId);

	PageVO<Spu> pageLibrary(Long storeId,SpuPageSearchDTO searchDTO);

	void sync(ErpSyncDTO dtoList);

	void priceSyncNew(ErpPriceDTO erpPriceDto);

	void stockSync(ErpStockDTO erpStockDTO);

	PageVO<SpuPageVO> spuPage(Long storeId, SpuPageSearchDTO searchDTO);

	PageVO<SpuAppPageVO> appPage(SpuPageSearchDTO searchDTO);

	List<Long> validSpus(SpuPageSearchDTO searchDTO);

	SpuVO getBySpuIdAndStoreId(Long spuId, Long storeId);

	PageVO<SpuCommonVO> commonPage(ProductSearchDTO productSearch);

	PageVO<SpuCommonVO> couponSearch(ProductSearchDTO productSearch);

	void iphSync(SpuDTO spuDTO);

//    List<SpuExcelImportVO> importParseSpus(MultipartFile multipartFile);
	SpuExcelImportDataVO importParseSpus(MultipartFile multipartFile);

	SpuExcelImportDataVO importSkuCodeTemplateExcel(MultipartFile multipartFile);

    List<SpuVO> listGiftSpuBySpuIds(List<Long> spuIdList);

	void updateIphStatus(List<SpuIphStatusDTO> spuIphStatusDTOS);

	List<Long> isSpuSkuChannel(SpuSkuRDTO spuSkuRDTO);

	List<StdPushSpuVO> pushStdSpus(List<String> spuCodes);

	boolean updateShopCategorys(SpuCategoryUpdateDTO shopCategoryUpdateDTO);

	/**
	 * 批量置零视频号库存
	 * @param dto dto
	 */
	void batchZeroSetChannelsStock(ZeroSetStockDto dto);

	/**
	 * 更新视频号库存
	 * @param spuId spuId
	 * @param skuStockDtoList skuStock
	 */
	void updateChannelsStock(Long spuId, List<UpdateChannelsSkuStockDto> skuStockDtoList);

	Set<Long> getAttrFilterSpuIds(SpuSearchAttrDTO spuSearchAttr);
//	/**
//	 * 根据参数获取商品数据
//	 * @param param 筛选参数
//	 * @param spuIds 商品id列表
//	 * @return 商品数据
//	 */
//	List<ProdEffectRespVO> getSpuListByParam(ProdEffectDTO param, Set<Long> spuIds);
//
//	/**
//	 * 根据参数获取商品数据总条目
//	 * @param param 参数
//	 * @param spuIds 商品id列表
//	 * @return 总条目数量
//	 */
//	Long getSpuListTotal( ProdEffectDTO param,Set<Long> spuIds);
}
