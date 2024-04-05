package com.mall4j.cloud.api.platform.feign;

import com.mall4j.cloud.api.platform.vo.SysConfigApiVO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author FrozenWatermelon
 * @date 2020/11/23
 */
@FeignClient(value = "mall4cloud-platform",contextId = "config")
public interface ConfigFeignClient {


    /**
     * 获取配置信息
     * @param key key
     * @return 配置信息json
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/config/getConfig")
    ServerResponseEntity<String> getConfig(@RequestParam("key") String key);


    /**
     * 保存or修改配置
     * @param sysConfigApiVO sysConfig
     * @return void
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/config/saveOrUpdateSysConfig")
    ServerResponseEntity<Void> saveOrUpdateSysConfig(@RequestBody SysConfigApiVO sysConfigApiVO);

}
