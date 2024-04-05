package com.mall4j.cloud.platform.feign;

import com.mall4j.cloud.api.platform.feign.ConfigFeignClient;
import com.mall4j.cloud.api.platform.vo.SysConfigApiVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.platform.model.SysConfig;
import com.mall4j.cloud.platform.service.SysConfigService;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author FrozenWatermelon
 * @date 2020/12/30
 */
@RestController
public class ConfigFeignController implements ConfigFeignClient {

    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private MapperFacade mapperFacade;

    @Override
    public ServerResponseEntity<String> getConfig(String key) {
        return ServerResponseEntity.success(sysConfigService.getValue(key));
    }

    @Override
    public ServerResponseEntity<Void> saveOrUpdateSysConfig(SysConfigApiVO sysConfigApiVO) {
        SysConfig sysConfig =  mapperFacade.map(sysConfigApiVO,SysConfig.class);
        sysConfigService.saveOrUpdateSysConfig(sysConfig);
        return ServerResponseEntity.success();
    }
}
