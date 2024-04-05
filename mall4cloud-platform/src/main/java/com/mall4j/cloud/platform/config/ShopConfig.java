package com.mall4j.cloud.platform.config;


import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.common.bean.*;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.platform.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 商城配置文件
 * 支付配置、文件上传配置、短信配置、快递配置、小程序配置、微信网页开发配置、公众号配置
 * @author FrozenWatermelon
 */
@Component
public class ShopConfig {

    @Autowired
    private SysConfigService sysConfigService;

    public Qiniu getQiniu(){
        // 从数据库 / 缓存中获取到配置文件信息
        return sysConfigService.getSysConfigObject(Constant.QINIU_CONFIG, Qiniu.class);
    }

    public AliOss getAliOss() {
        return sysConfigService.getSysConfigObject(Constant.ALI_OSS_CONFIG, AliOss.class);
    }

    public HuaWeiOss getHuaWeiObs() {
        return sysConfigService.getSysConfigObject(Constant.HUAWEI_OBS_CONFIG, HuaWeiOss.class);
    }

    public DaYu getDaYu(){
        DaYu daYu = sysConfigService.getSysConfigObject(Constant.ALIDAYU_CONFIG, DaYu.class);
        if (daYu == null || StrUtil.isBlank(daYu.getAccessKeyId())) {
            throw new LuckException("无法获取短信配置，无法发送短信");
        }
        return daYu;
    }


    public QuickBird getQuickBird() {
        return sysConfigService.getSysConfigObject(Constant.QUICKBIRD_CONFIG, QuickBird.class);
    }

    public Quick100 getQuick100() {
        return sysConfigService.getSysConfigObject(Constant.QUICK100_CONFIG, Quick100.class);
    }

    public AliQuick getAliQuick() {
        return sysConfigService.getSysConfigObject(Constant.ALI_QUICK_CONFIG, AliQuick.class);
    }
}
