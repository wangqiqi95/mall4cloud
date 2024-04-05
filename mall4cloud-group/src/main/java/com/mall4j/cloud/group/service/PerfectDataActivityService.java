package com.mall4j.cloud.group.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.api.group.feign.dto.UserPerfectDataActivityDTO;
import com.mall4j.cloud.group.dto.PerfectDataActivityPageDTO;
import com.mall4j.cloud.group.model.PerfectDataActivity;
import com.mall4j.cloud.group.vo.PerfectDataActivityListVO;

import java.util.List;

public interface PerfectDataActivityService extends IService<PerfectDataActivity> {
    List<PerfectDataActivityListVO> perfectDataActivityList(PerfectDataActivityPageDTO param);

    PerfectDataActivity selectFirstActivity(UserPerfectDataActivityDTO param);
}
