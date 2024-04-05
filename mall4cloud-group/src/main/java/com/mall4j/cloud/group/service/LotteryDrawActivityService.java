package com.mall4j.cloud.group.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.group.dto.LotteryDrawPageDTO;
import com.mall4j.cloud.group.model.LotteryDrawActivity;
import com.mall4j.cloud.group.vo.LotteryDrawActivityCensusVO;
import com.mall4j.cloud.group.vo.LotteryDrawListVO;

import java.util.List;

public interface LotteryDrawActivityService extends IService<LotteryDrawActivity> {
    List<LotteryDrawListVO> lotteryDrawList(LotteryDrawPageDTO param);

    LotteryDrawActivityCensusVO census(Integer id);
}
