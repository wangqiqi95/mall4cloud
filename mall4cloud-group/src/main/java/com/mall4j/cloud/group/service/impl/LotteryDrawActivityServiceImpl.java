package com.mall4j.cloud.group.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.group.dto.LotteryDrawPageDTO;
import com.mall4j.cloud.group.dto.OpenScreenAdPageDTO;
import com.mall4j.cloud.group.mapper.LotteryDrawActivityMapper;
import com.mall4j.cloud.group.model.LotteryDrawActivity;
import com.mall4j.cloud.group.service.LotteryDrawActivityService;
import com.mall4j.cloud.group.vo.LotteryDrawActivityCensusVO;
import com.mall4j.cloud.group.vo.LotteryDrawListVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LotteryDrawActivityServiceImpl extends ServiceImpl<LotteryDrawActivityMapper, LotteryDrawActivity> implements LotteryDrawActivityService {
    @Resource
    private LotteryDrawActivityMapper lotteryDrawActivityMapper;
    @Override
    public List<LotteryDrawListVO> lotteryDrawList(LotteryDrawPageDTO param) {
        return lotteryDrawActivityMapper.lotteryDrawList(param);
    }

    @Override
    public LotteryDrawActivityCensusVO census(Integer id) {
        return lotteryDrawActivityMapper.census(id);
    }
}
