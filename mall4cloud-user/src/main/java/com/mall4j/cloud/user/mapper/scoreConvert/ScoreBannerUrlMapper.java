package com.mall4j.cloud.user.mapper.scoreConvert;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.user.model.scoreConvert.ScoreBannerShop;
import com.mall4j.cloud.user.model.scoreConvert.ScoreBannerUrl;
import java.util.List;


/**
 * 积分Banner活动
 *
 * @author shijing
 * @date 2022-01-23
 */

public interface ScoreBannerUrlMapper extends BaseMapper<ScoreBannerUrl> {

    void insertBatch(List<ScoreBannerUrl> list);

}
