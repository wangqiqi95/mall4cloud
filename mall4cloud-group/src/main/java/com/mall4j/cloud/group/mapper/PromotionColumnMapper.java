package com.mall4j.cloud.group.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.group.dto.PromotionColumnDTO;
import com.mall4j.cloud.group.dto.TimeLimitDiscountActivityPageDTO;
import com.mall4j.cloud.group.model.PromotionColumn;
import com.mall4j.cloud.group.vo.PromotionColumnPageVO;

import java.util.List;

/**
 * 促销位 活动
 *
 * @author FrozenWatermelon
 * @date 2023-07-18 17:09:16
 */
public interface PromotionColumnMapper extends BaseMapper<PromotionColumn> {

    List<PromotionColumnPageVO> list(PromotionColumnDTO param);
}
