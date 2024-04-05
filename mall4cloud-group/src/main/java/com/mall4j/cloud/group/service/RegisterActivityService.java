package com.mall4j.cloud.group.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.api.group.feign.dto.UserRegisterActivityDTO;
import com.mall4j.cloud.api.user.dto.UserRegisterGiftDTO;
import com.mall4j.cloud.group.dto.RegisterActivityPageDTO;
import com.mall4j.cloud.group.model.RegisterActivity;
import com.mall4j.cloud.group.vo.RegisterActivityListVO;

import java.util.List;

public interface RegisterActivityService extends IService<RegisterActivity> {
    List<RegisterActivityListVO> registerActivityList(RegisterActivityPageDTO param);

    RegisterActivity selectFirstActivity(UserRegisterGiftDTO param);
}
