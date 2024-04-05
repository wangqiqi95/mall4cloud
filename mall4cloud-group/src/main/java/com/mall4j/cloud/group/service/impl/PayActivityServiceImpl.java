package com.mall4j.cloud.group.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.group.dto.PayActivityPageDTO;
import com.mall4j.cloud.group.mapper.PayActivityMapper;
import com.mall4j.cloud.group.model.PayActivity;
import com.mall4j.cloud.group.service.PayActivityService;
import com.mall4j.cloud.group.vo.PayActivityListVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PayActivityServiceImpl extends ServiceImpl<PayActivityMapper, PayActivity> implements PayActivityService {
    @Resource
    private PayActivityMapper payActivityMapper;
    @Override
    public List<PayActivityListVO> payActivityList(PayActivityPageDTO param) {
        return payActivityMapper.payActivityList(param);
    }

    @Override
    public PayActivity selectFirstActivity(String shopId) {
        return payActivityMapper.selectFirstActivity(shopId);
    }
}
