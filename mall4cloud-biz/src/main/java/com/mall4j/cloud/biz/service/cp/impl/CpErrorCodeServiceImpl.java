package com.mall4j.cloud.biz.service.cp.impl;

import com.mall4j.cloud.biz.mapper.cp.CpErrorCodeMapper;
import com.mall4j.cloud.biz.model.cp.CpErrorCodeLog;
import com.mall4j.cloud.biz.service.cp.CpErrorCodeService;
import com.mall4j.cloud.common.cache.constant.CacheNames;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 */
@Slf4j
@Service
public class CpErrorCodeServiceImpl implements CpErrorCodeService {

    @Autowired
    private CpErrorCodeMapper cpErrorCodeMapper;

    @Override
    @Cacheable(cacheNames = CacheNames.CP_ERROR_CODE)
    public Map<String,CpErrorCodeLog> listData(String errorcode) {
        return cpErrorCodeMapper.list().stream().collect(Collectors.toMap(CpErrorCodeLog::getErrorcode, errorCodeLog -> errorCodeLog));
    }

}
