package com.mall4j.cloud.platform.controller.platform;

import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.platform.model.SysConfig;
import com.mall4j.cloud.platform.service.SysConfigService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author chenlin
 * @DateTime: 2020/3/19 8:29
 * @description: 支付配置、文件上传配置、短信配置、快递配置、小程序配置、公众号配置
 */
@RestController
@RequestMapping("/p/sys_config")
@ConditionalOnProperty(prefix = "mall4cloud", name = "expose.permission", havingValue = "true", matchIfMissing = true)
public class SysConfigController {


    @Autowired
    private SysConfigService sysConfigService;

    /**
     * 获取保存配置信息
     */
    @GetMapping("/info/{key}")
    public ServerResponseEntity<String> info(@PathVariable("key")String key){
        return ServerResponseEntity.success(sysConfigService.getValue(key));
    }

    /**
     * 获取保存配置信息
     */
    @GetMapping("/ua/info/{key}")
    @ApiOperation(value = "获取配置信息", notes = "获取配置信息")
    public ServerResponseEntity<String> noLogin(@PathVariable("key")String key){
        return ServerResponseEntity.success(sysConfigService.getValue(key));
    }

    /**
     * 保存配置
     */
    @PostMapping("/save")
    public ServerResponseEntity<Void> save(@RequestBody @Valid SysConfig sysConfig){
        sysConfigService.saveOrUpdateSysConfig(sysConfig);
        return ServerResponseEntity.success();
    }




}
