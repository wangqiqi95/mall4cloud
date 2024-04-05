package com.mall4j.cloud.common.cache.constant;

/**
 * @author FrozenWatermelon
 * @date 2021/01/25
 */
public interface BizCacheNames {

    /**
     * 前缀
     */
    String COUPON_PREFIX = "mall4cloud_biz:";

    /**
     * 验证码key
     */
    String SMS_CODE_KEY = COUPON_PREFIX + "sms_code_key:";

    /**
     * 消息 通知前缀
     */
    String  CONFIG_MSG_ALL = "mall4cloud_biz_msg:all";
    /**
     * 消息 通知前缀
     */
    String  CONFIG_MSG_TYPE = "mall4cloud_biz:msg_type:";

    String  CP_ERROR_CODE = "mall4cloud_biz:cp_error_code:";

    /**
     * 线下活动上新 消息 通知前缀
     */
    String MA_MESSAGE_ACTIVITY_NEW = COUPON_PREFIX + "mall4cloud_biz:ma:activity_new:";

    /**
     * 微信门店触点二维码 Key
     */
    String WECHAT_QRCODE_TENTACLE_KEY = "wechat_qrcode_tentacle:";

}
