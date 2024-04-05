package com.mall4j.cloud.coupon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.coupon.dto.QueryChooseMemberEventPageDTO;
import com.mall4j.cloud.coupon.model.ChooseMemberEvent;
import com.mall4j.cloud.coupon.vo.ChooseMemberEventVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 指定会员活动表（提供最具价值会员活动表） Mapper 接口
 * </p>
 *
 * @author ben
 * @since 2022-11-03
 */
public interface ChooseMemberEventMapper extends BaseMapper<ChooseMemberEvent> {

    List<ChooseMemberEventVO> queryEventPage(@Param("pageDTO") QueryChooseMemberEventPageDTO pageDTO);
}
