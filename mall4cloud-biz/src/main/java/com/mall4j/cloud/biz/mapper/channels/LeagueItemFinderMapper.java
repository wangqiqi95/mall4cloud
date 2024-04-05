package com.mall4j.cloud.biz.mapper.channels;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.dto.channels.league.ItemListFinderRespDto;
import com.mall4j.cloud.biz.model.channels.LeagueItemFinder;
import com.mall4j.cloud.biz.model.channels.LeagueItemPlanFinder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description 优选联盟商品推广计划达人
 * @Author axin
 * @Date 2023-02-20 14:52
 **/
@Mapper
public interface LeagueItemFinderMapper extends BaseMapper<LeagueItemFinder> {


    List<ItemListFinderRespDto> queryListByProductIds(@Param("productIds") List<Long> productIds);
}
