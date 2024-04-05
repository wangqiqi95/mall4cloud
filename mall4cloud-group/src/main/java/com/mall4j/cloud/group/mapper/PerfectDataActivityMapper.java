package com.mall4j.cloud.group.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.api.group.feign.dto.UserPerfectDataActivityDTO;
import com.mall4j.cloud.group.dto.PerfectDataActivityPageDTO;
import com.mall4j.cloud.group.model.PerfectDataActivity;
import com.mall4j.cloud.group.vo.PerfectDataActivityListVO;

import java.util.List;

public interface PerfectDataActivityMapper extends BaseMapper<PerfectDataActivity> {
    List<PerfectDataActivityListVO> perfectDataActivityList(PerfectDataActivityPageDTO param);

    PerfectDataActivity selectFirstActivity(UserPerfectDataActivityDTO param);
}
