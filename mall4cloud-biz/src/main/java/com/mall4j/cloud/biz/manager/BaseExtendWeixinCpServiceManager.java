package com.mall4j.cloud.biz.manager;

import com.mall4j.cloud.biz.config.WxCpConfigurationPlus;
import me.chanjar.weixin.cp.api.WxCpService;

//@DependsOn("wxCpConfiguration")
//@Component
//@RequiredArgsConstructor
public class BaseExtendWeixinCpServiceManager {
    public final WxCpService mainService;

    public BaseExtendWeixinCpServiceManager() {
        this.mainService = WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId());
    }
}
