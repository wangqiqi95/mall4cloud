package com.mall4j.cloud.user.mapper.scoreConvert;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.user.dto.scoreConvert.ScoreConvertListDTO;
import com.mall4j.cloud.user.model.scoreConvert.ScoreBanner;
import com.mall4j.cloud.user.vo.scoreConvert.ScoreBannerListVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 积分Banner活动
 *
 * @author shijing
 * @date 2022-01-23
 */

public interface ScoreBannerMapper extends BaseMapper<ScoreBanner> {

    List<ScoreBannerListVO> list(ScoreConvertListDTO param);

    Long appActivity();

    Long bannerData(@Param("shopId") Long shopId, @Param("type") Integer type);

}
