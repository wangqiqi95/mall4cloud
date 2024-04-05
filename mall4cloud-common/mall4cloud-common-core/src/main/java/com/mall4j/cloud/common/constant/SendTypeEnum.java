package com.mall4j.cloud.common.constant;


/**
 * @author lhd
 * @date 2020/12/30
 */
public enum SendTypeEnum {
    /**
     * 自定义消息
     */
    CUSTOMIZE(0, 0,"自定义消息"),
    /**
     * 订单催付
     */
    PRESS_PAY(1, 1,"订单催付"),

    /**
     * 付款成功通知
     */
    PAY_SUCCESS(2, 1,"付款成功通知"),

    /**
     * 商家同意退款
     */
    AGREE_REFUND(3, 1,"商家同意退款"),

    /**
     * 商家拒绝退款
     */
    REFUSE_REFUND(4, 1,"商家拒绝退款"),

    /**
     * 核销提醒
     */
    WRITE_OFF(5, 1,"核销提醒"),

    /**
     * 发货提醒
     */
    DELIVERY(6, 1,"发货提醒"),

    /**
     * 拼团失败提醒
     */
    GROUP_FAIL(7, 1,"拼团失败提醒"),

    /**
     * 拼团成功提醒
     */
    GROUP_SUCCESS(8, 1,"拼团成功提醒"),

    /**
     * 拼团开团提醒
     */
    GROUP_START(9, 1,"拼团开团提醒"),

    /**
     * 会员升级通知
     */
    MEMBER_LEVEL(10, 1,"会员升级通知"),

    /**
     * 退款临近超时提醒
     */
    REFUND_OUT_TIME(11, 1,"退款临近超时提醒"),
    /**
     * 用户注册验证码
     */
    REGISTER(12, 1,"用户注册验证码"),
    /**
     * 发送登录验证码
     */
    LOGIN(13, 1,"发送登录验证码"),
    /**
     * 修改密码验证码
     */
    UPDATE_PASSWORD(14, 1,"修改密码验证码"),
    /**
     * 身份验证验证码
     */
    VALID(15, 1,"身份验证验证码"),

    /**
     * 确认收货提醒
     */
    RECEIPT_ORDER(102, 2,"确认收货提醒"),

    /**
     * 买家发起退款提醒
     */
    LAUNCH_REFUND(103, 2,"买家发起退款提醒"),

    /**
     * 买家已退货提醒
     */
    RETURN_REFUND(104, 2,"买家已退货提醒"),
    /**
     * 签到提醒
     */
    USER_SIGN(105, 1,"用户签到提醒")
    ;

    private final Integer value;
    /**
     * 0.自定义消息 1.为全部发送给用户消息，2.为发送给商家的
     */
    private final Integer type;
    private final String desc;
    public Integer getType() {
        return type;
    }
    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    SendTypeEnum(Integer value, Integer type, String desc) {
        this.value = value;
        this.type = type;
        this.desc = desc;
    }

    public static SendTypeEnum instance(Integer value) {
        SendTypeEnum[] enums = values();
        for (SendTypeEnum statusEnum : enums) {
            if (statusEnum.getValue().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
