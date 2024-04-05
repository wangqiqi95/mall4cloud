package com.mall4j.cloud.common.constant;

/**
 * 小程序app订阅消息发送类型
 *
 * @luzhengxiang
 * @create 2022-03-06 3:25 PM
 **/
public enum MaSendTypeEnum {

    USER_BIRTHDAY(100, "会员生日到期提醒"),
    COUPON_EXPIRE(200, "优惠券到期提醒"),
    COUPON_ARRIVAL(201,"优惠券到账提醒"),
    POINT_CHANGE(300, "积分变更提醒"),
    LEVEL_EXPIRE(301, "等级到期提醒"),
    POINT_ACTIVITY_CHANGE(302, "积分兑换活动上新"),
    ORDER_DELIVERY(400, "订单发货提醒"),
    ORDER_SIGNING(401, "订单签收提醒"),
    CHARGE_BACK_EXCHANGE(500, "退单审核结果提醒"),
    CHARGE_BACK_COMPLETE(501, "退单完成提醒"),
    ACTIVITY_NEW(600, "活动上新提醒"),
    ACTIVITY_BEGIN(601, "活动开始提醒"),
    ACTIVITY_END(602, "活动结束提醒"),
    ACTIVITY_BEGIN_H5(603, "活动开始提醒"),
    SIGIN_IN_DAY(701, "每日签到"),
    SCORE_PRODUCT_ARRIVAL(702,"积分礼品到货提醒"),
    LIVE_NEW(801, "直播上新通知"),
    WEI_KE_CHECK(901, "微客审核"),
    QUESTIONNAIRE_ACTIVITY_BEGIN(2002, "问卷活动开始"),
    QUESTIONNAIRE_AWARDED(2001, "问卷活动奖励到账"),
    ;

    private final Integer value;
    private final String desc;
    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    MaSendTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static MaSendTypeEnum instance(Integer value) {
        MaSendTypeEnum[] enums = values();
        for (MaSendTypeEnum statusEnum : enums) {
            if (statusEnum.getValue().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
