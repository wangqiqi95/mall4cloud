package com.mall4j.cloud.group.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.api.group.feign.dto.UserPerfectDataActivityDTO;
import com.mall4j.cloud.group.dto.PerfectDataActivityPageDTO;
import com.mall4j.cloud.group.mapper.PerfectDataActivityMapper;
import com.mall4j.cloud.group.model.PerfectDataActivity;
import com.mall4j.cloud.group.service.PerfectDataActivityService;
import com.mall4j.cloud.group.vo.PerfectDataActivityListVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PerfectDataActivityServiceImpl extends ServiceImpl<PerfectDataActivityMapper, PerfectDataActivity> implements PerfectDataActivityService {
    @Resource
    private PerfectDataActivityMapper perfectDataActivityMapper;
    @Override
    public List<PerfectDataActivityListVO> perfectDataActivityList(PerfectDataActivityPageDTO param) {
        return perfectDataActivityMapper.perfectDataActivityList(param);
    }

    @Override
    public PerfectDataActivity selectFirstActivity(UserPerfectDataActivityDTO param) {
        return perfectDataActivityMapper.selectFirstActivity(param);
    }
}
