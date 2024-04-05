package com.mall4j.cloud.auth.service.impl;

import com.mall4j.cloud.auth.service.AuthLogService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.mall4j.cloud.auth.mapper.AuthLogMapper;

/**
 * @author FrozenWatermelon
 * @date 2020/7/2
 */
@Service
public class AuthLogServiceImpl implements AuthLogService {

	@Resource
	private AuthLogMapper authLogMapper;

}
