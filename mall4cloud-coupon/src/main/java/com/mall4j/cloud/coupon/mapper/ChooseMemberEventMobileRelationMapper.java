package com.mall4j.cloud.coupon.mapper;

import com.mall4j.cloud.coupon.model.ChooseMemberEventMobileRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 指定会员活动关系表 Mapper 接口
 * </p>
 *
 * @author ben
 * @since 2022-11-03
 */
public interface ChooseMemberEventMobileRelationMapper extends BaseMapper<ChooseMemberEventMobileRelation> {

    void insertBatch(@Param("relationList") List<ChooseMemberEventMobileRelation> relationList);

}
