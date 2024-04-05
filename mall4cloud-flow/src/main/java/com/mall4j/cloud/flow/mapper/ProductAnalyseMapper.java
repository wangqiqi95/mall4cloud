package com.mall4j.cloud.flow.mapper;

import cn.hutool.core.date.DateTime;
import com.mall4j.cloud.api.product.vo.ProdEffectRespVO;
import com.mall4j.cloud.flow.model.ProductAnalyse;
import com.mall4j.cloud.flow.vo.FlowProdEffectRespVO;
import com.mall4j.cloud.flow.vo.ShopFlowInfoVO;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 流量分析—商品分析
 *
 * @author YXF
 * @date 2021-05-21 15:25:19
 */
public interface ProductAnalyseMapper {

	/**
	 * 获取流量分析—商品分析列表
	 *
	 * @return 流量分析—商品分析列表
	 */
	List<ProductAnalyse> list();

	/**
	 * 根据流量分析—商品分析id获取流量分析—商品分析
	 *
	 * @param productAnalyseId 流量分析—商品分析id
	 * @return 流量分析—商品分析
	 */
	ProductAnalyse getByProductAnalyseId(@Param("productAnalyseId") Long productAnalyseId);

	/**
	 * 保存流量分析—商品分析
	 *
	 * @param productAnalyse 流量分析—商品分析
	 */
	void save(@Param("productAnalyse") ProductAnalyse productAnalyse);

	/**
	 * 更新流量分析—商品分析
	 *
	 * @param productAnalyse 流量分析—商品分析
	 */
	void update(@Param("productAnalyse") ProductAnalyse productAnalyse);

	/**
	 * 根据流量分析—商品分析id删除流量分析—商品分析
	 *
	 * @param productAnalyseId
	 */
	void deleteById(@Param("productAnalyseId") Long productAnalyseId);

	/**
	 * 根据时间和spuId列表，获取商品统计的数据信息
	 *
	 * @param date 时间
	 * @param spuIds 商品id列表
	 * @param userIds userId列表
	 * @return 商品分析数据列表
	 */
    List<ProductAnalyse> listByDate(@Param("date") Date date, @Param("spuIds") Collection spuIds,@Param("userIds") Collection userIds);

	/**
	 * 批量保存
	 *
	 * @param productAnalyses
	 */
	void saveBatch(@Param("productAnalyses") List<ProductAnalyse> productAnalyses);

	/**
	 * 批量更新
	 *
	 * @param productAnalyses
	 */
	void updateBatch(@Param("productAnalyses") List<ProductAnalyse> productAnalyses);

	/**
	 * 根据参数获取商品洞察数据
	 *
	 * @param spuIds    商品id列表
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @return 商品洞察数据
	 */
	List<FlowProdEffectRespVO> getProdEffectByParam(@Param("spuIds") List<Long> spuIds, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

	/**
	 * 获取店铺流量排行榜
	 *
	 * @param startTime
	 * @param endTime
	 * @param limit
	 * @return
	 */
	List<ShopFlowInfoVO> listShopRankIngByFlow(@Param("startTime") Date startTime, @Param("endTime") DateTime endTime, @Param("limit") Integer limit);

	/**
	 * 根据时间以及店铺id获取商品id列表
	 *
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @param shopId    店铺id
	 * @return 商品id列表
	 */
	List<ProdEffectRespVO> getProdEffectRespByTime(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("shopId") Long shopId);

	/**
	 * 删除商品统计数据
	 *
	 * @param spuId 商品id
	 */
    void deleteSpuDataBySpuId(@Param("spuId") Long spuId);
}
