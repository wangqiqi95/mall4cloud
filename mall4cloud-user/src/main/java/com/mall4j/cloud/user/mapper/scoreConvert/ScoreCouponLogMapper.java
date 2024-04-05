package com.mall4j.cloud.user.mapper.scoreConvert;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.user.dto.scoreConvert.ScoreBarterLogListDTO;
import com.mall4j.cloud.user.model.scoreConvert.ScoreBarterLog;
import com.mall4j.cloud.user.model.scoreConvert.ScoreCouponLog;
import com.mall4j.cloud.user.vo.scoreConvert.ScoreBarterLogVO;
import com.mall4j.cloud.user.vo.scoreConvert.ScoreCouponLogVO;
import com.mall4j.cloud.user.vo.scoreConvert.UserCouponLogVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 积分兑换
 *
 * @author shijing
 */

public interface ScoreCouponLogMapper extends BaseMapper<ScoreCouponLog> {

    List<ScoreCouponLogVO> logList(ScoreBarterLogListDTO param);

    List<UserCouponLogVO> appLogList(@Param("userId") Long userId, @Param("type") Short type);

    void confirmExprot(@Param("ids") List<Long> ids);


}
