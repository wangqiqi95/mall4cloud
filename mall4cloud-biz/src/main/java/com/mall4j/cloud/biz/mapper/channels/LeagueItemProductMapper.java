package com.mall4j.cloud.biz.mapper.channels;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.dto.channels.league.ItemAllowPromotionListPageReqDto;
import com.mall4j.cloud.biz.dto.channels.league.ItemAllowPromotionListPageRespDto;
import com.mall4j.cloud.biz.dto.channels.league.ItemListPageReqDto;
import com.mall4j.cloud.biz.dto.channels.league.ItemListPageRespDto;
import com.mall4j.cloud.biz.model.channels.LeagueItemProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description 优选联盟商品推广商品
 * @Author axin
 * @Date 2023-02-20 14:52
 **/
@Mapper
public interface LeagueItemProductMapper extends BaseMapper<LeagueItemProduct> {

    List<ItemListPageRespDto> queryList(@Param("reqDto") ItemListPageReqDto reqDto);

    List<ItemAllowPromotionListPageRespDto> allowPromotionListPageByType(@Param("reqDto") ItemAllowPromotionListPageReqDto reqDto);
}
