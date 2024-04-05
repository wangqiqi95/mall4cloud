package com.mall4j.cloud.coupon.mapper;

import com.mall4j.cloud.coupon.model.ChooseMemberEventShopRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ben
 * @since 2022-11-03
 */
public interface ChooseMemberEventShopRelationMapper extends BaseMapper<ChooseMemberEventShopRelation> {

    void insertBatch(@Param("relationList") List<ChooseMemberEventShopRelation> relationList);
}
