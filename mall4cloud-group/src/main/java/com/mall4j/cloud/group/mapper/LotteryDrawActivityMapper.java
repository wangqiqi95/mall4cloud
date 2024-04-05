package com.mall4j.cloud.group.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.group.dto.LotteryDrawPageDTO;
import com.mall4j.cloud.group.model.LotteryDrawActivity;
import com.mall4j.cloud.group.vo.LotteryDrawActivityCensusVO;
import com.mall4j.cloud.group.vo.LotteryDrawListVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LotteryDrawActivityMapper extends BaseMapper<LotteryDrawActivity> {
    List<LotteryDrawListVO> lotteryDrawList(LotteryDrawPageDTO param);

    LotteryDrawActivityCensusVO census(@Param("id") Integer id);
}
