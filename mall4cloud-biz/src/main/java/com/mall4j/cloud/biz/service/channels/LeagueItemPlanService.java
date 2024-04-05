package com.mall4j.cloud.biz.service.channels;

import com.mall4j.cloud.biz.dto.channels.league.*;
import com.mall4j.cloud.common.database.vo.PageVO;

/**
 * @Description 优选联盟推广计划
 * @Author axin
 * @Date 2023-02-20 17:27
 **/
@Deprecated
public interface LeagueItemPlanService {

    /**
     * 添加计划
     * @param reqDto
     */
    String addPlan(AddItemPlanReqDto reqDto);

    /**
     * 修改优选联盟推广计划
     * @param reqDto
     */
    void updPlan(UpdItemPlanReqDto reqDto);

    /**
     * 禁用商品
     * @param reqDto
     */
    void updProductStatus(DisableProductReqDto reqDto);

    /**
     * 查询计划详情
     * @param id
     * @return
     */
    ItemPlanRespDto get(Long id);

    /**
     * 查询计划商品分页详情
     * @param reqDto
     * @return
     */
    PageVO<ItemDetail> getProductPage(ProductPageReqDto reqDto);


    /**
     * 查询计划列表
     * @param reqDto
     * @return
     */
    PageVO<ItemPlanListPageRespDto> planListPage(ItemPlanListPageReqDto reqDto);
}
