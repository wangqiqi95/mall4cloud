package com.mall4j.cloud.biz.service.channels;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.api.biz.dto.channels.response.EcProductResponse;
import com.mall4j.cloud.api.biz.vo.ChannelsSpuSkuVO;
import com.mall4j.cloud.biz.dto.channels.PriceCheckDTO;
import com.mall4j.cloud.biz.dto.channels.event.ProductSpuAuditDTO;
import com.mall4j.cloud.biz.dto.channels.query.ChannelsSpuQueryDTO;
import com.mall4j.cloud.biz.dto.channels.request.AddChannlesSpuRequest;
import com.mall4j.cloud.biz.model.channels.ChannelsSpu;
import com.mall4j.cloud.biz.vo.channels.ChannelsSpuExtraVO;
import com.mall4j.cloud.biz.vo.channels.ChannelsSpuProductSpuVO;
import com.mall4j.cloud.biz.vo.channels.ChannelsSpuSkuDetailVO;
import com.mall4j.cloud.common.database.vo.PageVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 视频号4.0商品
 *
 * @author FrozenWatermelon
 * @date 2023-02-07 15:01:48
 */
public interface ChannelsSpuService extends IService<ChannelsSpu> {

	/**
	 * 分页获取视频号4.0商品列表
	 * @param dto 查询参数
	 * @return 视频号4.0商品列表分页数据
	 */
	PageVO<ChannelsSpu> page(ChannelsSpuQueryDTO dto);

	/**
	 * 根据视频号4.0商品id获取视频号4.0商品
	 *
	 * @param id 视频号4.0商品id
	 * @return 视频号4.0商品
	 */
	ChannelsSpuSkuDetailVO getById(Long id);


	/**
	 * 添加spu
	 *
	 * @param addChannlesSpuRequest
	 * @return
	 */
	Long addChannelsSpu(AddChannlesSpuRequest addChannlesSpuRequest);

	/**
	 * 更新
	 * @param addChannlesSpuRequest
	 */
	void updateChannelsSpu(AddChannlesSpuRequest addChannlesSpuRequest);

	/**
	 * 更新视频号4.0商品
	 * @param channelsSpu 视频号4.0商品
	 */
	void update(ChannelsSpu channelsSpu);

	/**
	 * <p>根据视频号4.0商品id删除视频号4.0商品</p>
	 * <p>同步删除sku</p>
	 * @param id 视频号4.0商品id
	 */
	void deleteById(Long id);

	void auditCancel(Long id);

	void listing(List<Long> id);

	void delisting(List<Long> id);

	Integer getStock(Long id,Long skuId);

	List<String> listProduct();

	EcProductResponse getEcProductById(Long id, Integer type);

	/**
	 * 更新库存
	 *
	 * @param id            内部商品ID
	 * @param channelsSkuId 内部SKU ID
	 * @param stock         变更库存数量
	 * @param type
	 */
	void updateStock(Long id, Long channelsSkuId, Integer stock, Integer type);

	/**
	 * 审核回调
	 * @param productSpuAuditDTO dto
	 */
	void auditNotify(ProductSpuAuditDTO productSpuAuditDTO);

	/**
	 * 上下架回调
	 * @param productSpuAuditDTO dto
	 */
	void listingNotify(ProductSpuAuditDTO productSpuAuditDTO);

	void delisting(Long spuId);

	void export(ChannelsSpuQueryDTO dto, HttpServletResponse response);

	List<ChannelsSpuSkuVO> listChannelsSpuSkuVO(List<Long> outSpuIds);

	ChannelsSpuSkuVO getChannelsSpuSkuVO(Long outSpuId);

	/**
	 * 商品规格价格检查
	 * @param priceCheckDTO dto
	 */
	void checkPrice(List<PriceCheckDTO> priceCheckDTO);

	/**
	 * 保存并上架
	 * @param addChannlesSpuRequest dto
	 */
	void saveAndListing(AddChannlesSpuRequest addChannlesSpuRequest);

	ChannelsSpuProductSpuVO getChannelsSpuProductSpuVOBySpuId(Long spuId);

	PageVO<ChannelsSpuExtraVO> extraPage(ChannelsSpuQueryDTO dto);

	void checkPriceAll(ChannelsSpuQueryDTO dto);
}
