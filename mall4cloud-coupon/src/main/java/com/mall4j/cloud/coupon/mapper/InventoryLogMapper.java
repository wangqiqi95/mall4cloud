package com.mall4j.cloud.coupon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.coupon.model.InventoryLog;
import com.mall4j.cloud.coupon.vo.InventoryListVO;

import java.util.List;

/**
 * 库存调整记录
 *
 * @author shijing
 */


public interface InventoryLogMapper extends BaseMapper<InventoryLog> {

    List<InventoryListVO> list(Long activityId);


}
