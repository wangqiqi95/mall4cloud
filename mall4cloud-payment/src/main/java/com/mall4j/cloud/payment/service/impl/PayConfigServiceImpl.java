package com.mall4j.cloud.payment.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.payment.mapper.PayConfigMapper;
import com.mall4j.cloud.payment.model.PayConfig;
import com.mall4j.cloud.payment.service.PayConfigService;
import org.springframework.stereotype.Service;


@Service("payConfigService")
public class PayConfigServiceImpl extends ServiceImpl<PayConfigMapper, PayConfig> implements PayConfigService {

}