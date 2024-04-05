package com.mall4j.cloud.biz.wx.wx.constant;

/**
 * @Date 2021年12月30日, 0030 14:59
 * @Created by eury
 */
public class WxMessageConstants {

    /**
     * 返回消息类型：文本
     */
    public static final String RESP_MESSAGE_TYPE_TEXT = "text";

    /**
     * 返回消息类型：音乐
     */
    public static final String RESP_MESSAGE_TYPE_MUSIC = "music";

    /**
     * 返回消息类型：图文
     */
    public static final String RESP_MESSAGE_TYPE_NEWS = "news";

    /**
     * 返回消息类型：图片
     */
    public static final String RESP_MESSAGE_TYPE_IMAGE = "image";

    /**
     * 返回消息类型：语音
     */
    public static final String RESP_MESSAGE_TYPE_VOICE = "voice";

    /**
     * 返回消息类型：视频
     */
    public static final String RESP_MESSAGE_TYPE_VIDEO = "video";

    /**
     * 返回消息类型：扩展接口业务回复
     */
    public static final String RESP_MESSAGE_TYPE_EXPAND = "expand";

    /**
     * 请求消息类型：文本
     */
    public static final String REQ_MESSAGE_TYPE_TEXT = "text";

    /**
     * 请求消息类型：图片
     */
    public static final String REQ_MESSAGE_TYPE_IMAGE = "image";

    /**
     * 请求消息类型：链接
     */
    public static final String REQ_MESSAGE_TYPE_LINK = "link";

    /**
     * 请求消息类型：地理位置
     */
    public static final String REQ_MESSAGE_TYPE_LOCATION = "location";

    /**
     * 请求消息类型：音频
     */
    public static final String REQ_MESSAGE_TYPE_VOICE = "voice";

    /**
     * 请求消息类型：小视频
     */
    public static final String REQ_MESSAGE_TYPE_SHORTVIDEO = "shortvideo";

    /**
     * 请求消息类型：推送
     */
    public static final String REQ_MESSAGE_TYPE_EVENT = "event";
    /**
     * 请求消息类型：转发至多客服
     */
    public static final String REQ_MESSAGE_TYPE_CUSTOMERSERVICE = "transfer_customer_service";
    /**
     * 事件类型：subscribe(订阅)
     */
    public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";

    /**
     * 事件类型：unsubscribe(取消订阅)
     */
    public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";

    /**
     * 事件类型：扫描二维码
     */
    public static final String EVENT_TYPE_SCAN="SCAN";

    /**
     * 事件类型：CLICK(自定义菜单点击事件)
     */
    public static final String EVENT_TYPE_CLICK = "CLICK";
    /**
     * 事件类型：VIEW(自定义菜单点击事件)
     */
    public static final String EVENT_TYPE_VIEW = "VIEW";
    /**
     * 上报地理位置事件
     */
    public static final String EVENT_TYPE_LOCATION = "LOCATION";

    /**
     * 礼券：审核事件推送
     */
    public static final String EVENT_CARD_PASS_CHECK = "card_pass_check";
    /**
     * 礼券：领取事件推送
     */
    public static final String EVENT_USER_GET_CARD = "user_get_card";
    /**
     * 礼券：删除事件推送
     */
    public static final String EVENT_USER_DEL_CARD = "user_del_card";
    /**
     * 礼券：核销事件推送
     */
    public static final String EVENT_USER_CONSUME_CARD = "user_consume_card";
    /**
     * 礼券：进入会员卡事件推送
     */
    public static final String EVENT_USER_VIEW_CARD = "user_view_card";
    /**
     * 礼券：从卡券进入公众号会话事件推送
     */
    public static final String EVENT_USER_ENTER_SESSION_FROM_CARD = "user_enter_session_from_card";
    /**
     * 卡劵： 领取失败事件推送
     */
    public static final String EVENT_CARD_NOT_PASS_CHECK = "card_not_pass_check";
    /**
     * 卡劵： 转赠事件推送
     */
    public static final String EVENT_USER_GIFTING_CARD = "user_gifting_card";
    /**
     * 卡劵： 买单事件推送
     */
    public static final String EVENT_USER_PAY_FROM_PAY_CELL = "user_pay_from_pay_cell";
    /**
     * 卡劵： 会员卡内容更新事件
     */
    public static final String EVENT_UPDATE_MEMBER_CARD = "update_member_card";
    /**
     * 卡劵： 库存报警事件
     */
    public static final String EVENT_CARD_SKU_REMIND = "card_sku_remind";
    /**
     * 卡劵： 券点流水详情事件
     */
    public static final String EVENT_CARD_PAY_ORDER = "card_pay_order";
    /**
     * 卡劵： 会员卡激活事件推送
     */
    public static final String EVENT_SUBMIT_MEMBERCARD_USER_INFO = "submit_membercard_user_info";
    /**
     * 群发： 事件推送群发结果
     */
    public static final String EVENT_MASSSENDJOBFINISH = "MASSSENDJOBFINISH";
    /**
     * 摇一摇： 事件通知
     */
    public static final String EVENT_SHAKEAROUNDUSERSHAKE = "ShakearoundUserShake";
    /**
     * 红包： 事件通知
     */
    public static final String EVENT_SHAKEAROUNDLOTTERYBIND = "ShakearoundLotteryBind";
    /**
     * 扫一扫： 打开商品主页事件推送
     */
    public static final String EVENT_USER_SCAN_PRODUCT = "user_scan_product";
    /**
     * 扫一扫： 关注公众号事件推送
     */
    public static final String EVENT_SUBSCRIBE = "subscribe";
    /**
     * 扫一扫： 进入公众号事件推送
     */
    public static final String EVENT_USER_SCAN_PRODUCT_ENTER_SESSION = "user_scan_product_enter_session";
    /**
     * 扫一扫： 地理位置信息异步推送
     */
    public static final String EVENT_USER_SCAN_PRODUCT_ASYNC = "user_scan_product_async";
    /**
     * 扫一扫： 商品审核结果推送
     */
    public static final String EVENT_USER_SCAN_PRODUCT_VERIFY_ACTION = "user_scan_product_verify_action";
    /**
     * 模版消息： 模版消息发送任务完成后
     */
    public static final String EVENT_TEMPLATESENDJOBFINISH = "TEMPLATESENDJOBFINISH";
    /**
     * 支付完成事件
     */
    public static final String EVENT_TRANSACTION_ID = "transaction_id";

    public static final String view_miniprogram = "view_miniprogram";
}
