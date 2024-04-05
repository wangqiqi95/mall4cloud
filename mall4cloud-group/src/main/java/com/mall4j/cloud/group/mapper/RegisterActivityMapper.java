package com.mall4j.cloud.group.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.api.group.feign.dto.UserRegisterActivityDTO;
import com.mall4j.cloud.api.user.dto.UserRegisterGiftDTO;
import com.mall4j.cloud.group.dto.RegisterActivityPageDTO;
import com.mall4j.cloud.group.model.RegisterActivity;
import com.mall4j.cloud.group.vo.RegisterActivityListVO;

import java.util.List;

public interface RegisterActivityMapper extends BaseMapper<RegisterActivity> {
    List<RegisterActivityListVO> registerActivityList(RegisterActivityPageDTO param);

    RegisterActivity selectFirstActivity(UserRegisterGiftDTO param);
}
