package com.mall4j.cloud.biz.mapper.channels;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.model.channels.LeagueItemPlanProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description 优选联盟商品推广计划商品
 * @Author axin
 * @Date 2023-02-20 14:52
 **/
@Mapper
@Deprecated
public interface LeagueItemPlanProductMapper extends BaseMapper<LeagueItemPlanProduct> {

    void insertBatch(@Param("reqs") List<LeagueItemPlanProduct> reqs);
}
