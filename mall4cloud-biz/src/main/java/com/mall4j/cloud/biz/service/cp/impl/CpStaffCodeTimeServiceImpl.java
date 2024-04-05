package com.mall4j.cloud.biz.service.cp.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.biz.mapper.cp.CpStaffCodeTimeMapper;
import com.mall4j.cloud.biz.model.cp.CpStaffCodeTime;
import com.mall4j.cloud.biz.service.cp.CpStaffCodeTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 渠道活码人员时间段
 *
 * @author gmq
 * @date 2023-10-25 16:39:38
 */
@Service
public class CpStaffCodeTimeServiceImpl extends ServiceImpl<CpStaffCodeTimeMapper, CpStaffCodeTime> implements CpStaffCodeTimeService {

    @Autowired
    private CpStaffCodeTimeMapper cpStaffCodeTimeMapper;

}
