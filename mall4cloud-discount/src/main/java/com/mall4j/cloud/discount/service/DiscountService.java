package com.mall4j.cloud.discount.service;

import com.mall4j.cloud.api.platform.dto.OfflineHandleEventDTO;
import com.mall4j.cloud.api.platform.vo.OfflineHandleEventVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.order.vo.DiscountOrderVO;
import com.mall4j.cloud.common.product.dto.ProductSearchDTO;
import com.mall4j.cloud.discount.dto.AppDiscountListDTO;
import com.mall4j.cloud.discount.dto.DiscountDTO;
import com.mall4j.cloud.discount.model.Discount;
import com.mall4j.cloud.discount.vo.DiscountVO;

import java.util.List;

/**
 * 满减满折优惠
 *
 * @author FrozenWatermelon
 * @date 2020-12-10 13:43:38
 */
public interface DiscountService {

	/**
	 * 满减活动信息
	 * @param discountId
	 * @return
	 */
	DiscountVO discountInfo(Long discountId,Long shopId);

	/**
	 * 满减活动信息
	 * @param discountId
	 * @return
	 */
	DiscountVO getDiscountAndSpu(Long discountId);

	/**
	 * 保存活动信息
	 * @param discountDTO 活动信息、活动项、商品id列表
	 */
	void insertDiscountAndItemAndSpu(DiscountDTO discountDTO);

	/**
	 * 更新活动信息
	 * @param discount 活动信息、活动项、商品id列表
	 */
	void updateDiscountAndItemAndSpu(DiscountDTO discount);

	/**
	 * 删除活动信息
	 * @param discountId 活动id
	 * @param shopId 店铺id
	 */
	void deleteDiscountsAndItemsAndSpuList(Long discountId, Long shopId);

	/**
	 * 移除缓存
	 * @param discountId 活动id
	 * @param shopId 店铺id
	 */
	void removeDiscountCache(Long discountId, Long shopId);

	/**
	 * 根据店铺id获取活动信息
	 * @param shopId 店铺id
	 * @return 活动信息列表
	 */
	List<DiscountOrderVO> listDiscountsAndItemsByShopId(Long shopId);

	/**
	 * 根据商品id获取正在进行的活动信息
	 * @param shopId 店铺id
	 * @param spuId 商品id
	 * @return 正在进行的活动列表
	 */
	List<DiscountVO> spuDiscountList(Long shopId, Long spuId);


	/**
	 * 分页获取全部在线满减数据
	 * @param page 分页信息
	 * @return 在线满减活动分页数据
	 */
	PageVO<DiscountVO> getAppDiscountList(PageDTO page, AppDiscountListDTO discount);

	/**
	 * 通过活动id获取促销活动详情
	 * @param discountId 活动id
	 * @return 促销活动详情
	 */
	Discount getDiscountByDiscountId(Long discountId);

	/**
	 * 更新活动状态
	 * @param spuIds 商品ids
	 * @return 标记
	 */
	List<Long> updateDiscountSpuByIds(List<Long> spuIds);

	/**
	 * 根据筛选条件获取满减列表
	 * @param pageDTO 分页数据
	 * @param transportDTO 过滤条件
	 * @return 满减列表
	 */
    PageVO<DiscountVO> page(PageDTO pageDTO, DiscountDTO transportDTO);

	/**
	 * 平台满减活动管理列表
	 * @param pageDTO 分页数据
	 * @param transportDTO 过滤条件
	 * @return 满减列表
	 */
    PageVO<DiscountVO> platformPage(PageDTO pageDTO, DiscountDTO transportDTO);

	/**
	 * 更新活动状态
	 * @param discountId
	 * @param status
	 */
	void changeDiscountStatus(Long discountId, Integer status);

	/**
	 * 平台下架活动
	 * @param offlineHandleEventDto
	 */
	void offline(OfflineHandleEventDTO offlineHandleEventDto);

	/**
	 * 获取下线的事件记录
	 * @param discountId
	 * @return
	 */
	OfflineHandleEventVO getOfflineHandleEvent(Long discountId);

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
	 * 关闭已经结束的满减折活动
	 */
	void closeDiscountBySetStatus();

	/**
	 * 处理下架事件
	 * @param spuIds
	 * @param shopIds
	 * @return
	 */
	void handleSpuOffline(List<Long> spuIds, List<Long> shopIds);

	/**
	 * 根据id获取活动详细信息
	 * @param discountId id
	 * @return
	 */
	DiscountVO getDiscountInfoById(Long discountId, ProductSearchDTO productSearchDTO);
}
