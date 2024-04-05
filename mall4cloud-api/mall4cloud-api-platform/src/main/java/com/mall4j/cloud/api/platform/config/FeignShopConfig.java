package com.mall4j.cloud.api.platform.config;


import cn.hutool.core.util.StrUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.mall4j.cloud.api.platform.feign.ConfigFeignClient;
import com.mall4j.cloud.api.platform.vo.OrderPriceDiscountConfigVO;
import com.mall4j.cloud.api.platform.vo.SkuPriceLogConfigVO;
import com.mall4j.cloud.api.platform.vo.SpuPriceDiscountConfigVO;
import com.mall4j.cloud.common.bean.*;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 商城配置文件
 * 支付配置、文件上传配置、短信配置、快递配置、小程序配置、微信网页开发配置、公众号配置
 * @author FrozenWatermelon
 */
@Component
public class FeignShopConfig {

    @Autowired
    private ConfigFeignClient configFeignClient;

    /**
     * 缓存
     */
    private static final Cache<Object, Object> CONFIG_MAP = Caffeine.newBuilder()
            // 数量上限
            .maximumSize(100)
            // 过期机制
            .expireAfterWrite(5, TimeUnit.MINUTES).build();


    public Qiniu getQiniu(){
        // 从数据库 / 缓存中获取到配置文件信息
        return getSysConfigObject(Constant.QINIU_CONFIG, Qiniu.class);
    }

    public AliOss getAliOss() {
        return getSysConfigObject(Constant.ALI_OSS_CONFIG, AliOss.class);
    }

    public HuaWeiOss getHuaWeiObs() {
        return getSysConfigObject(Constant.HUAWEI_OBS_CONFIG, HuaWeiOss.class);
    }

    public DaYu getDaYu(){
        DaYu daYu = getSysConfigObject(Constant.ALIDAYU_CONFIG, DaYu.class);
        if (daYu == null || StrUtil.isBlank(daYu.getAccessKeyId())) {
            throw new LuckException("无法获取短信配置，无法发送短信");
        }
        return daYu;
    }


    public QuickBird getQuickBird() {
        return getSysConfigObject(Constant.QUICKBIRD_CONFIG, QuickBird.class);
    }

    public Quick100 getQuick100() {
        return getSysConfigObject(Constant.QUICK100_CONFIG, Quick100.class);
    }

    public AliQuick getAliQuickConfig() {
        return getSysConfigObject(Constant.ALI_QUICK_CONFIG, AliQuick.class);
    }

    public String getMaShortlinkDomin() {
        String domain = getSysConfigObject(Constant.MA_SHORLINK_DOMAIN, String.class);
        if (domain == null || StrUtil.isBlank(domain)) {
            // 请在后台页面设置微信小程序信息后，再进行操作
            throw new LuckException("请设置短连接域名，再进行操作");
        }
        return domain;
    }

    public WxOpen getWxOpen() {
        WxOpen wxOpen = getSysConfigObject(Constant.WECHAT_OPEN_CONFIG, WxOpen.class);
        if (wxOpen == null || StrUtil.isBlank(wxOpen.getAppId())) {
            // 请在后台页面设置微信小程序信息后，再进行操作
            throw new LuckException("请在后台页面设置微信第三方平台信息后，再进行操作");
        }
        return wxOpen;
    }

    public WxOpen getWxLiveStoreOpen() {
        WxOpen wxOpen = getSysConfigObject(Constant.WECHAT_LIVE_STORE_OPEN_CONFIG, WxOpen.class);
        if (wxOpen == null || StrUtil.isBlank(wxOpen.getAppId())) {
            // 请在后台页面设置微信小程序信息后，再进行操作
            throw new LuckException("请在后台页面设置微信第三方平台信息后，再进行操作");
        }
        return wxOpen;
    }

    public WxOpen getWxChannelsOpen() {
        WxOpen wxOpen = getSysConfigObject(Constant.EC_CONFIG, WxOpen.class);
        if (wxOpen == null || StrUtil.isBlank(wxOpen.getAppId())) {
            // 请在后台页面设置微信小程序信息后，再进行操作
            throw new LuckException("请在后台页面设置微信第三方平台信息后，再进行操作");
        }
        return wxOpen;
    }

    public String getUpGradeHint() {
        String upgradehint = getSysConfigObject(Constant.UP_GRADE_HINT, String.class);
        if (StrUtil.isBlank(upgradehint)) {
            // 请在后台页面设置微信小程序信息后，再进行操作
            upgradehint="再消费\"${}\"元可以升级为SKECHERS\"${}\"";
        }
        return upgradehint;
    }

    public WxMiniApp getWxMiniApp() {
        WxMiniApp wxMiniApp = getSysConfigObject(Constant.MA_CONFIG, WxMiniApp.class);
        if (wxMiniApp == null || StrUtil.isBlank(wxMiniApp.getAppId())) {
            // 请在后台页面设置微信小程序信息后，再进行操作
            throw new LuckException("请在后台页面设置微信小程序信息后，再进行操作");
        }
        return wxMiniApp;
    }

    public SpuPriceDiscountConfigVO getSpuPirceDiscountConfig() {
        SpuPriceDiscountConfigVO spuPriceDiscountConfigVO = getSysConfigObject(Constant.SPU_PRICE_DISCOUNT_CONNFIG, SpuPriceDiscountConfigVO.class);
        if (spuPriceDiscountConfigVO == null || StrUtil.isBlank(spuPriceDiscountConfigVO.getDiscount())) {
            // 请在后台页面设置微信小程序信息后，再进行操作
            throw new LuckException("请在后台页面设置信息后，再进行操作");
        }
        return spuPriceDiscountConfigVO;
    }

    public OrderPriceDiscountConfigVO getOrderPirceDiscountConfig() {
        OrderPriceDiscountConfigVO orderPriceDiscountConfigVO = getSysConfigObject(Constant.ORDER_PRICE_DISCOUNT_CONNFIG, OrderPriceDiscountConfigVO.class);
        if (orderPriceDiscountConfigVO == null || StrUtil.isBlank(orderPriceDiscountConfigVO.getDiscount())) {
            // 请在后台页面设置微信小程序信息后，再进行操作
            throw new LuckException("请在后台页面设置信息后，再进行操作");
        }
        return orderPriceDiscountConfigVO;
    }

    public WxMp getWxMp() {
        WxMp wxMp = getSysConfigObject(Constant.MP_CONFIG, WxMp.class);
        if (wxMp == null || StrUtil.isBlank(wxMp.getAppId())) {
            // 请在后台页面设置微信公众号信息后，再进行操作
            throw new LuckException("请在后台页面设置微信公众号信息后，再进行操作");
        }
        return wxMp;
    }

    public WxApp getWxApp() {
        WxApp wxApp = getSysConfigObject(Constant.MX_APP_CONFIG, WxApp.class);
        if (wxApp == null || StrUtil.isBlank(wxApp.getAppId())) {
            // 请在后台页面设置微信开放平申请应用相关信息后，再进行操作
            throw new LuckException("请在后台页面设置微信开放平申请应用相关信息后，再进行操作");
        }
        return wxApp;
    }

    public WxPay getWxPay() {
        WxPay wxPay = getSysConfigObject(Constant.WXPAY_CONFIG, WxPay.class);
        if (wxPay == null || StrUtil.isBlank(wxPay.getMchId())) {
            // 请在后台页面设置微信支付信息后，再进行操作
            throw new LuckException("请在后台页面设置微信支付信息后，再进行操作");
        }
        return wxPay;
    }

    /**
     * 微信视频号配置信息，同小程序共用基础类
     */
    public WxMiniApp getWxEcApp() {
        WxMiniApp wxMiniApp = getSysConfigObject(Constant.EC_CONFIG, WxMiniApp.class);
        if (wxMiniApp == null || StrUtil.isBlank(wxMiniApp.getAppId())) {
            // 请在后台页面设置微信小程序信息后，再进行操作
            throw new LuckException("请在后台页面设置微信视频号信息后，再进行操作");
        }
        return wxMiniApp;
    }

    /**
     * 微信视频号小店配置相关
     */
    public WxMiniApp getWxShopOneApp() {
        WxMiniApp wxMiniApp = getSysConfigObject(Constant.EC_CHANNELS_SHOP_ONE_CONFIG, WxMiniApp.class);
        if (wxMiniApp == null || StrUtil.isBlank(wxMiniApp.getAppId())) {
            // 请在后台页面设置微信小程序信息后，再进行操作
            throw new LuckException("请在后台页面设置微信视频号信息后，再进行操作");
        }
        return wxMiniApp;
    }

    public Alipay getAlipay() {
        return getSysConfigObject(Constant.ALIPAY_CONFIG,Alipay.class);
    }

    public SensitiveWord getSensitiveWord(){
        return getSysConfigObject(Constant.SENSITIVE_WORDS, SensitiveWord.class);
    }

    /**
     * 获取系统配置，缓存起来
     */
    @SuppressWarnings("unchecked")
    public <T> T  getSysConfigObject(String key, Class<T> clazz) {
        T configData = (T) CONFIG_MAP.get(key, cacheKey -> {
            ServerResponseEntity<String> configResponse = configFeignClient.getConfig(key);
            if (!configResponse.isSuccess()) {
                throw new LuckException(ResponseEnum.EXCEPTION);
            }
            String value = configResponse.getData();
            if(Objects.equals(String.class,clazz)){
                return (T)value;
            }else{
                return Json.parseObject(value, clazz);
            }
        });

        if (configData == null) {
            CONFIG_MAP.invalidate(key);
        }

        return configData;
    }

}
