package com.mall4j.cloud.payment.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.payment.mapper.PayConfigMemberMapper;
import com.mall4j.cloud.payment.model.PayConfigMember;
import com.mall4j.cloud.payment.service.PayConfigMemberService;
import org.springframework.stereotype.Service;


@Service("payConfigUserService")
public class PayConfigMemberServiceImpl extends ServiceImpl<PayConfigMemberMapper, PayConfigMember> implements PayConfigMemberService {


}