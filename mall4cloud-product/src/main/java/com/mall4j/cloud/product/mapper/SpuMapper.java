package com.mall4j.cloud.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.api.product.bo.SpuSimpleBO;
import com.mall4j.cloud.api.product.dto.SpuListDTO;
import com.mall4j.cloud.api.product.vo.StdPushSpuVO;
import com.mall4j.cloud.common.database.util.PageAdapter;
import com.mall4j.cloud.common.product.bo.EsProductBO;
import com.mall4j.cloud.common.product.dto.ProductSearchDTO;
import com.mall4j.cloud.common.product.dto.SpuDTO;
import com.mall4j.cloud.common.product.dto.SpuSearchAttrDTO;
import com.mall4j.cloud.common.product.vo.SpuCodeVo;
import com.mall4j.cloud.common.product.vo.SpuVO;
import com.mall4j.cloud.common.product.vo.search.SpuCommonVO;
import com.mall4j.cloud.product.dto.SpuAbbrReqDto;
import com.mall4j.cloud.product.dto.SpuAppPageVO;
import com.mall4j.cloud.product.dto.SpuPageSearchDTO;
import com.mall4j.cloud.product.dto.SpuTagReferenceDTO;
import com.mall4j.cloud.product.model.Spu;
import com.mall4j.cloud.product.vo.SimpleSpuExcelVO;
import com.mall4j.cloud.product.vo.SpuPageVO;
import com.mall4j.cloud.product.vo.SpuSkuVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * spu信息
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
public interface SpuMapper extends BaseMapper<Spu> {

	/**
	 * 获取spu信息列表
	 *
	 * @param spu
	 * @return spu信息列表
	 */
	List<SpuVO> listSpu(@Param("spu") SpuPageSearchDTO spu);

	/**
	 * 根据spu信息id获取spu信息
	 *
	 * @param spuId spu信息id
	 * @return spu信息
	 */
	SpuVO getBySpuId(@Param("spuId") Long spuId);

	/**
	 * 保存spu信息
	 *
	 * @param spu spu信息
	 */
	void saveSpu(@Param("spu") Spu spu);

	/**
	 * 更新spu信息
	 *
	 * @param spu spu信息
	 */
	void updateSpu(@Param("spu") Spu spu);

	/**
	 * 根据spu信息id删除spu信息
	 *
	 * @param spuId
	 */
	void deleteById(@Param("spuId") Long spuId);

	/**
	 * 根据商品id修改商品状态
	 *
	 * @param spuId 商品id
	 */
	void updateStatusBySpuId(@Param("spuId") Long spuId);

	void batchSpusChannelBySpuId(@Param("spus") List<Spu> spuId);

	/**
	 * 根据spuId获取商品信息（搜索）
	 *
	 * @param spuId
	 * @return 商品信息
	 */
	EsProductBO loadEsProductBO(@Param("spuId") Long spuId);

	/**
	 * 获取 spuId列表
	 *
	 * @param shopCategoryIds 店铺分类id列表
	 * @param categoryIds     平台分类Id列表
	 * @param brandId         品牌id
	 * @param shopId          店铺id
	 * @return spuId列表
	 */
	List<Long> getSpuIdsBySpuUpdateDTO(@Param("shopCategoryIds") List<Long> shopCategoryIds, @Param("categoryIds") List<Long> categoryIds
			, @Param("brandId") Long brandId, @Param("shopId") Long shopId);

	/**
	 * 改变商品状态（上下架）
	 *
	 * @param spuId
	 * @param status
	 */
	void changeSpuStatus(@Param("spuId") Long spuId, @Param("status") Integer status);

	/**
	 * 批量改变商品状态（上下架）
	 *
	 * @param spuIds
	 * @param status
	 */
	void batchChangeSpuStatus(@Param("spuIds") List<Long> spuIds, @Param("status") Integer status);

	/**
	 * 更新spu表（canal监听后，会发送更新的消息，更新es中的数据）
	 *
	 * @param spuIds
	 * @param categoryIds
	 * @param shopIds
	 */
	void updateSpuUpdateTime(@Param("spuIds") List<Long> spuIds, @Param("categoryIds") List<Long> categoryIds, @Param("shopIds") List<Long> shopIds);

	/**
	 * 根据店铺和分类下架商品
	 *
	 * @param shopId      店铺id
	 * @param categoryIds 分类id
	 */
	void offlienSpuByShopIdAndCategoryId(@Param("shopId") Long shopId, @Param("categoryIds") List<Long> categoryIds);

	/**
	 * 下线店铺中的商品
	 *
	 * @param shopId
	 */
	void offlineSpuByShopId(@Param("shopId") Long shopId);

	/**
	 * 根据spuId或者为空直接获取可以参与秒杀活动的商品列表
	 *
	 * @param pageAdapter 分页数据
	 * @param spuDTO      店铺id
	 * @param lang        语言
	 * @return 商品信息
	 */
	List<SpuVO> listCanSeckillProd(@Param("page") PageAdapter pageAdapter, @Param("spuDTO") SpuDTO spuDTO, @Param("lang") Integer lang);

	/**
	 * 根据spuId或者为空直接获取可以参与秒杀活动的商品数量
	 *
	 * @param spuDTO 店铺id
	 * @return 商品信息
	 */
	Long countCanSeckillProd(@Param("spuDTO") SpuDTO spuDTO);

	/**
	 * 根据运费id获取商品数量
	 *
	 * @param transportId
	 * @return
	 */
	Integer countByTransportId(@Param("transportId") Long transportId);

	/**
	 * 根据spuId获取店铺Id
	 *
	 * @param spuId
	 * @return
	 */
	Long getShopIdBySpuId(@Param("spuId") Long spuId);

	/**
	 * 变为普通商品
	 *
	 * @param spuIds
	 */
	void changeToNormalSpu(@Param("spuIds") List<Long> spuIds);

	/**
	 * 根据分类id与店铺id查询使用该分类的商品数量
	 *
	 * @param categoryId
	 * @param shopId
	 * @return
	 */
	int countByCategoryAndShopId(@Param("categoryId") Long categoryId, @Param("shopId") Long shopId);

	/**
	 * 根据用户id查询商品收藏数量
	 *
	 * @param userId 用户id
	 * @return 商品收藏数量
	 */
	Integer countByUserId(@Param("userId") Long userId);

	/**
	 * 获取商品id列表， 商品的店铺id列表，
	 *
	 * @param spuIds 商品的店铺id列表
	 * @return 商品id列表
	 */
    List<Long> spuShopIdsBySpuIds(@Param("spuIds") List<Long> spuIds);

	/**
	 * 批量修改
	 *
	 * @param spuList 修改后的商品信息
	 */
	void updateBatch(@Param("spuList") List<SpuVO> spuList);

	/**
	 * 根据分类id列表与店铺id获取商品id列表
	 *
	 * @param cidList
	 * @param shopId
	 * @return
	 */
    List<Long> listIdByCidAndShopId(@Param("cidList") List<Long> cidList, @Param("shopId") Long shopId);

	/**
	 * 根据分类id列表与店铺id或者商品id获取活动商品列表
	 *
	 * @param type        下线商品和活动类型，1.直接通过商品ids，2.通过shopId判断为平台还是店铺分类ids进行下架，3.下线店铺通过shopId
	 * @param spuIds      商品ids
	 * @param shopId      店铺id
	 * @param categoryIds 分类id
	 * @param status      修改后的商品状态，为空 默认查询条件status是1
	 * @return 商品信息
	 */
	List<SpuVO> getActivitySpuInfoByCategoryIdsAndShopId(@Param("type") Integer type, @Param("spuIds") List<Long> spuIds,
														 @Param("shopId") Long shopId, @Param("categoryIds") List<Long> categoryIds, @Param("status") Integer status);

	/**
	 * 获取商品信息列表
	 *
	 * @param spuIds 商品ids
	 * @return
	 */
    List<SpuVO> listSpuBySpuIds(@Param("spuIds") List<Long> spuIds);

    /**
     * 商铺分页列表
     * @return
     */
    List<SpuVO> pageList(@Param("param") SpuListDTO param);
	/**
	 * 获取商品信息列表
	 *
	 * @param spuCodes 商品Codes
	 * @return
	 */
    List<SpuCodeVo> listSpuBySpuCodes(@Param("spuCodes") List<String> spuCodes);

	/**
	 * 获取未删除商品数量
	 *
	 * @param time
	 * @return 商品数量
	 */
    Long getNotDeleteSpuNum(@Param("time") Date time);

	/**
	 * 根据商品更新时间，获取商品id列表
	 *
	 * @param time
	 * @return
	 */
	List<Spu> listNotDeleteSpu(@Param("time") Date time);

	/**
	 * 根据店铺id列表与商品状态获取商品id列表
	 *
	 * @param shopIds
	 * @param status
	 * @return
	 */
	List<Long> listSpuIdByShopIdsAndStatus(@Param("shopIds") List<Long> shopIds, @Param("status") Integer status);

	/**
	 * 根据商品id设置商品是否置顶
	 *
	 * @param spuId
	 */
    void toTopBySpuId(@Param("spuId") Long spuId);

	/**
	 * 批量更新商品，允许设置字段为空
	 *
	 * @param spuList 商品集合
	 * @return
	 */
    Integer updateBatchFieldSetNull(@Param("spuList") List<SpuVO> spuList);

	/**
	 * 根据基本信息获取商品列表
	 *
	 * @param spuSimpleBO
	 * @return
	 */
	List<SpuSimpleBO> listSimple(@Param("spuSimpleBO") SpuSimpleBO spuSimpleBO);

	/**
	 * 根据商品id列表获取商品名称列表
	 *
	 * @param spuIds 商品id列表
	 * @return 商品列表
	 */
    List<SpuVO> listSpuNameBySpuIds(@Param("spuIds") List<Long> spuIds);

	List<SpuPageVO> listPageVO(@Param("searchDTO") SpuPageSearchDTO searchDTO);

	List<SpuAppPageVO> appList(@Param("searchDTO") SpuPageSearchDTO searchDTO);

	SpuVO getBySpuIdAndStoreId(@Param("spuId") Long spuId, @Param("storeId") Long storeId);

	List<SpuCommonVO> commonList(@Param("productSearch") ProductSearchDTO productSearch);

    SpuVO getSpuByCode(@Param("spuCode") String spuCode);

	List<SpuPageVO> listByTag(@Param("tagParam") SpuTagReferenceDTO spuTagReferenceDTO);

	List<SpuSkuVo> listSpuMarketPrice(@Param("spuCodes") List<String> spuCodes);

	List<StdPushSpuVO> pushStdSpus(@Param("spuCodes") List<String> spuCodes);

	List<SimpleSpuExcelVO> listSimpleSpuExcelVO();

	List<SpuPageVO> listAllSpus();

	void batchUpdateAbbrBySpuCode(@Param("reqDtos") List<SpuAbbrReqDto> reqDtos);

	Set<Long> getAttrFilterSpuIds(@Param("dto") SpuSearchAttrDTO dto,@Param("exactAttrGroup") List<String> exactAttrGroup,@Param("vagueAttrGroup") List<String> vagueAttrGroup);


//	/**
//	 * 根据参数获取商品数据
//	 * @param param 筛选参数
//	 * @param spuIds 商品id列表
//	 * @return 商品数据
//	 */
//	List<ProdEffectRespVO> getSpuListByParam(@Param("param") ProdEffectDTO param, @Param("spuIds")Set<Long> spuIds);
//
//	/**
//	 * 根据参数获取商品数据总条目
//	 * @param param 参数
//	 * @param spuIds 商品id列表
//	 * @return 总条目数量
//	 */
//	Long getSpuListTotal(@Param("param") ProdEffectDTO param, @Param("spuIds")Set<Long> spuIds);
}
