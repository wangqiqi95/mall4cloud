package com.mall4j.cloud.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.payment.model.PayConfigMember;
import org.apache.ibatis.annotations.Mapper;

/**
 * 支付配置用户关联
 */
@Mapper
public interface PayConfigMemberMapper extends BaseMapper<PayConfigMember> {
	
}
