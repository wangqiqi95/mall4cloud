package com.mall4j.cloud.user.mapper.scoreConvert;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.user.dto.scoreConvert.ScoreConvertListDTO;
import com.mall4j.cloud.user.model.scoreConvert.ScoreConvert;
import com.mall4j.cloud.user.vo.scoreConvert.ScoreBarterListVO;
import com.mall4j.cloud.user.vo.scoreConvert.ScoreConvertListVO;
import com.mall4j.cloud.user.vo.scoreConvert.ScoreCouponAppVO;
import com.mall4j.cloud.user.vo.scoreConvert.ScoreCouponListVO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;
import java.util.List;

/**
 * 积分兑换
 *
 * @author shijing
 * @date 2021-12-10 18:02:09
 */

public interface ScoreConvertMapper extends BaseMapper<ScoreConvert> {

    List<ScoreBarterListVO> scoreBarterList(ScoreConvertListDTO param);

    List<ScoreCouponListVO> scoreCouponList(ScoreConvertListDTO param);

    List<ScoreCouponAppVO> listForApp(Long shopId);

    /**
     * 更新库存
     */
    int updateStocks( @Param("convertId") Long activityId, @Param("version") Integer version);

    List<ScoreConvert> selectNewActivity(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    /**
     * 查询用户积分能够兑换积分活动的活动信息（商城小程序）
     * @param userScore     用户个人所剩积分
     * @param type   兑换活动种类
     * @param shopId   用户所在门店
     * @return
     */
    List<ScoreConvertListVO> selectUserScoreGeScoreConvertList(@Param("userScore")Long userScore, @Param("type")Integer type, @Param("shopId")Long shopId);

    /**
     * 查询用户积分小于积分活动兑换积分的活动信息（商城小程序）
     * @param userScore     用户个人所剩积分
     * @param type   兑换活动种类
     * @param shopId   用户所在门店
     * @return
     */
    List<ScoreConvertListVO> selectUserScoreLtScoreConvertList(@Param("userScore")Long userScore, @Param("type")Integer type,  @Param("shopId")Long shopId);

}
