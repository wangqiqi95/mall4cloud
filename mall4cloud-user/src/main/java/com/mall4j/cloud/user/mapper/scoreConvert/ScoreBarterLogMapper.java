package com.mall4j.cloud.user.mapper.scoreConvert;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.user.dto.scoreConvert.ScoreBarterLogListDTO;
import com.mall4j.cloud.user.model.scoreConvert.ScoreBarterLog;
import com.mall4j.cloud.user.vo.scoreConvert.ScoreBarterLogVO;
import java.util.List;

/**
 * 积分兑换
 *
 * @author shijing
 */

public interface ScoreBarterLogMapper extends BaseMapper<ScoreBarterLog> {

    List<ScoreBarterLogVO> logList(ScoreBarterLogListDTO param);


}
