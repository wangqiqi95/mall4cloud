package com.mall4j.cloud.biz.service.channels;

import com.mall4j.cloud.api.biz.dto.channels.response.league.item.BatchAddResp;
import com.mall4j.cloud.biz.dto.channels.league.*;
import com.mall4j.cloud.common.database.vo.PageVO;

import java.util.List;

/**
 * @Description 联盟推广商品
 * @Author axin
 * @Date 2023-04-20 14:40
 **/
public interface LeagueItemService {

    /**
     * 查询商品列表
     * @param reqDto
     * @return
     */
    PageVO<ItemListPageRespDto> listPage(ItemListPageReqDto reqDto);

    /**
     * 根据推广类型查询可推广商品
     * @param reqDto
     * @return
     */
    PageVO<ItemAllowPromotionListPageRespDto> allowPromotionListPageByType(ItemAllowPromotionListPageReqDto reqDto);

    /**
     * 添加推广商品
     * @param reqDto
     * @return
     */
    List<BatchAddResp.ResultInfo> add(AddItemReqDto reqDto);

    /**
     * 编辑商品
     * @param reqDto
     */
    void upd(UpdItemReqDto reqDto);

    /**
     * 删除商品
     * @param reqDto
     */
    void delete(DeleteItem reqDto);

    /**
     * 上下架联盟商品
     * @param reqDto
     */
    void updProductStatus(DisableProductReqDto reqDto);

    /**
     * 查询商品详情
     * @param id
     * @return
     */
    ItemListDetailRespDto get(Long id);

    /**
     * 处理过期商品
     */
    void handleExpiredItem();

    /**
     * 商品基础数据下架
     * @param outProductId
     */
    void besedelisting(String outProductId, String spuCode, String reason);

}
