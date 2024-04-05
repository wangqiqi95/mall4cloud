package com.mall4j.cloud.platform.controller.app;

import com.mall4j.cloud.common.constant.ConfigNameConstant;
import com.mall4j.cloud.common.i18n.I18nMessage;
import com.mall4j.cloud.common.i18n.LanguageEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.platform.service.SysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @Author lth
 * @Date 2021/6/1 13:59
 */
@RestController("appSysConfigController")
@RequestMapping("/ua/app/sys_config")
@Api(tags = "app-系统配置信息")
public class SysConfigController {

    @Autowired
    private SysConfigService sysConfigService;

    @GetMapping("/service_protocol")
    @ApiOperation(value = "获取服务条款", notes = "获取服务条款")
    public ServerResponseEntity<String> getServiceProtocol(){
        Integer dbLang = I18nMessage.getLang();
        if(Objects.equals(LanguageEnum.LANGUAGE_ZH_CN.getLang(), dbLang)) {
            return ServerResponseEntity.success(sysConfigService.getValue(ConfigNameConstant.SERVICE_PROTOCOL_CN));
        } else {
            return ServerResponseEntity.success(sysConfigService.getValue(ConfigNameConstant.SERVICE_PROTOCOL_EN));
        }
    }

    @GetMapping("/privacy_protocol")
    @ApiOperation(value = "获取隐私策略", notes = "获取隐私策略")
    public ServerResponseEntity<String> getPrivacyProtocol(){
        Integer dbLang = I18nMessage.getLang();
        if(Objects.equals(LanguageEnum.LANGUAGE_ZH_CN.getLang(), dbLang)) {
            return ServerResponseEntity.success(sysConfigService.getValue(ConfigNameConstant.PRIVACY_PROTOCOL_CN));
        } else {
            return ServerResponseEntity.success(sysConfigService.getValue(ConfigNameConstant.PRIVACY_PROTOCOL_EN));
        }
    }
}
