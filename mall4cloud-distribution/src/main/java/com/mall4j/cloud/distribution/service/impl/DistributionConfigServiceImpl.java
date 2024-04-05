package com.mall4j.cloud.distribution.service.impl;

import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.api.platform.feign.ConfigFeignClient;
import com.mall4j.cloud.api.platform.vo.DistributionConfigApiVO;
import com.mall4j.cloud.api.platform.vo.DistributionRecruitConfigApiVO;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.Json;
import com.mall4j.cloud.distribution.service.DistributionConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author cl
 * @date 2021-08-12 10:06:38
 */
@Service
public class DistributionConfigServiceImpl implements DistributionConfigService {

    @Autowired
    private ConfigFeignClient configFeignClient;

    @Override
    public DistributionConfigApiVO getDistributionConfig() {
        ServerResponseEntity<String> config = configFeignClient.getConfig(Constant.DISTRIBUTION_CONFIG);
        if (!config.isSuccess()) {
            throw new LuckException(config.getMsg());
        }
        String value = config.getData();
        if (StrUtil.isBlank(value)) {
            throw new LuckException("分销基本设置没有配置");
        }
        return Json.parseObject(value, DistributionConfigApiVO.class);
    }

    @Override
    public DistributionRecruitConfigApiVO getDistributionRecruitConfig() {
        ServerResponseEntity<String> config = configFeignClient.getConfig(Constant.DISTRIBUTION_RECRUIT_CONFIG);
        if (!config.isSuccess()) {
            throw new LuckException(config.getMsg());
        }
        String value = config.getData();
        if (StrUtil.isBlank(value)) {
            throw new LuckException("分销推广设置没有配置");
        }
        return Json.parseObject(value, DistributionRecruitConfigApiVO.class);
    }
}
