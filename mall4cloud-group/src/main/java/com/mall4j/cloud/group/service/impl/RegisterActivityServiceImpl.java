package com.mall4j.cloud.group.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.api.group.feign.dto.UserRegisterActivityDTO;
import com.mall4j.cloud.api.user.dto.UserRegisterGiftDTO;
import com.mall4j.cloud.group.dto.RegisterActivityPageDTO;
import com.mall4j.cloud.group.mapper.RegisterActivityMapper;
import com.mall4j.cloud.group.model.RegisterActivity;
import com.mall4j.cloud.group.service.RegisterActivityService;
import com.mall4j.cloud.group.vo.RegisterActivityListVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RegisterActivityServiceImpl extends ServiceImpl<RegisterActivityMapper, RegisterActivity> implements RegisterActivityService {
    @Resource
    private RegisterActivityMapper registerActivityMapper;
    @Override
    public List<RegisterActivityListVO> registerActivityList(RegisterActivityPageDTO param) {
        return registerActivityMapper.registerActivityList(param);
    }

    @Override
    public RegisterActivity selectFirstActivity(UserRegisterGiftDTO param) {
        return registerActivityMapper.selectFirstActivity(param);
    }
}
