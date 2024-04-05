package com.mall4j.cloud.flow.service;

import cn.hutool.core.date.DateTime;
import com.mall4j.cloud.api.product.dto.ProdEffectDTO;
import com.mall4j.cloud.api.product.vo.ProdEffectRespVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.flow.dto.FlowLogDTO;
import com.mall4j.cloud.flow.model.ProductAnalyse;
import com.mall4j.cloud.flow.vo.ShopFlowInfoVO;

import java.util.List;

/**
 * 流量分析—商品分析
 *
 * @author YXF
 * @date 2021-05-21 15:25:19
 */
public interface ProductAnalyseService {

	/**
	 * 分页获取流量分析—商品分析列表
	 * @param pageDTO 分页参数
	 * @return 流量分析—商品分析列表分页数据
	 */
	PageVO<ProductAnalyse> page(PageDTO pageDTO);

	/**
	 * 根据流量分析—商品分析id获取流量分析—商品分析
	 *
	 * @param productAnalyseId 流量分析—商品分析id
	 * @return 流量分析—商品分析
	 */
	ProductAnalyse getByProductAnalyseId(Long productAnalyseId);

	/**
	 * 保存流量分析—商品分析
	 * @param productAnalyse 流量分析—商品分析
	 */
	void save(ProductAnalyse productAnalyse);

	/**
	 * 更新流量分析—商品分析
	 * @param productAnalyse 流量分析—商品分析
	 */
	void update(ProductAnalyse productAnalyse);

	/**
	 * 根据流量分析—商品分析id删除流量分析—商品分析
	 * @param productAnalyseId 流量分析—商品分析id
	 */
	void deleteById(Long productAnalyseId);

	/**
	 * 统计商品信息
	 * @param flowLogList
	 */
	void statisticalProduct(List<FlowLogDTO> flowLogList);

	/**
	 * 获取商品洞察-商品效果分析
	 * @param pageDTO 分页参数
	 * @param prodEffectDTO 筛选参数
	 * @return 商品效果分析数据
	 */
	PageVO<ProdEffectRespVO> getProductEffect(PageDTO pageDTO,ProdEffectDTO prodEffectDTO);

	/**
	 * 获取店铺流量排行榜
	 * @param endTime 结束时间
	 * @param dayCount 天数
	 * @param limit 条数
	 * @return
	 */
    List<ShopFlowInfoVO> listShopRankIngByFlow(DateTime endTime, Integer dayCount, Integer limit);

	/**
	 * 删除商品统计数据
	 * @param spuId 商品id
	 */
	void deleteSpuDataBySpuId(Long spuId);
}
