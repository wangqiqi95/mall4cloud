package com.mall4j.cloud.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.payment.model.PayConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * 支付配置
 */
@Mapper
public interface PayConfigMapper extends BaseMapper<PayConfig> {
	
}
