package com.mall4j.cloud.user.mapper.scoreConvert;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.user.model.scoreConvert.ScoreConvertShop;
import java.util.List;

/**
 * 积分兑换
 *
 * @author shijing
 * @date 2021-12-10 18:02:09
 */

public interface ScoreConvertShopMapper extends BaseMapper<ScoreConvertShop> {
    void insertBatch(List<ScoreConvertShop> list);

}
