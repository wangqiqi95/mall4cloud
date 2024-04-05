package com.mall4j.cloud.group.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.group.dto.PromotionColumnDTO;
import com.mall4j.cloud.group.model.AdvanceCommodity;
import com.mall4j.cloud.group.model.PromotionColumn;
import com.mall4j.cloud.group.vo.PromotionColumnPageVO;

/**
 * 促销位 活动
 *
 * @author FrozenWatermelon
 * @date 2023-07-18 17:09:16
 */
public interface PromotionColumnService extends IService<PromotionColumn> {

    PageVO<PromotionColumnPageVO> page(PageDTO pageDTO, PromotionColumnDTO param);

    PromotionColumnPageVO detail(int intValue);
}
