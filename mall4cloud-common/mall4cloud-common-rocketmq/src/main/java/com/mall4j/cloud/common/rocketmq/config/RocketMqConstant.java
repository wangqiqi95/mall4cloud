package com.mall4j.cloud.common.rocketmq.config;

/**
 * nameserver用;分割
 * 同步消息，如果两次
 * @author FrozenWatermelon
 * @date 2021/3/25
 */
public class RocketMqConstant {

    // 延迟消息 1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h (1-18)

    /**
     * 取消订单时间，实际上30分钟
     */
    public static final int CANCEL_ORDER_DELAY_LEVEL = 16;
    /**
     * 问卷 延迟提醒提交时间
     */
    public static final int QUESTIONNAIREACTIVITYBEGIN_DELAY_LEVEL = 17;
//    public static final int CANCEL_ORDER_DELAY_LEVEL = 9;

    /**
     * 订单取消时间 30分钟
     */
    public static final int CANCEL_TIME_INTERVAL = 30 * 60 * 1000;

    /**
     * 默认发送消息超时时间
     */
    public static final long TIMEOUT = 3000;

    /**
     * 商品搜索topic
     */
    public static final String CANAL_TOPIC = "canal-topic";

    /**
     * 库存解锁topic
     */
    public static final String STOCK_UNLOCK_TOPIC = "stock-unlock-topic";

    /**
     * 优惠券解锁topic
     */
//    public static final String COUPON_UNLOCK_TOPIC = "coupon-unlock-topic";
            
    public static final String COUPON_UNLOCK_TOPIC = "coupon-unlock-topic_1";

    /**
     * 优惠券赠送topic
     */
    public static final String COUPON_GIVE_TOPIC = "coupon-give-topic";
    /**
     * 等级提升赠送优惠券topic
     */
    public static final String LEVEL_UP_COUPON_GIVE_TOPIC = "level-up-coupon-give-topic";

    //    public static final String LEVEL_DOWN_COUPON_EXPIRE_TOPIC = "level-down-coupon-expire-topic";

    /**
     * 视频号订单同步到微信端topic
     */
    public static final String WECHAT_LIVESHOP_ORDER = "level-down-coupon-expire-topic";
    //创建订单
    public static final String WECHAT_LIVESHOP_ORDER_PUSH_TAG = "wechat_liveshop_order_push_tag";
    //  取消订单
    public static final String WECHAT_LIVESHOP_ORDER_CANCEL_TAG = "wechat_liveshop_order_cancel_tag";
    //发货
    public static final String WECHAT_LIVESHOP_ORDER_DELIVERY_TAG = "wechat_liveshop_order_delivery_tag";
    //确认收货
    public static final String WECHAT_LIVESHOP_ORDER_RECEIPT_TAG = "wechat_liveshop_order_receipt_tag";

    //发起售后
    public static final String WECHAT_LIVESHOP_REFUND_ORDER_PUSH_TAG = "wechat_liveshop_refund_order_push_tag";
    //取消售后
    public static final String WECHAT_LIVESHOP_REFUND_ORDER_CANCEL_TAG = "wechat_liveshop_refund_order_cancel_tag";
    //用户上传退货物流
    public static final String WECHAT_LIVESHOP_REFUND_ORDER_USER_UPLOADRETURNINFO_TAG = "wechat_liveshop_refund_order_user_uploadreturninfo_tag";
    //商家同意退款
    public static final String WECHAT_LIVESHOP_REFUND_ORDER_ACCEPTREFUNDL_TAG = "wechat_liveshop_refund_order_acceptrefundl_tag";
    //商家同意退货
    public static final String WECHAT_LIVESHOP_REFUND_ORDER_ACCEPTRETURN_TAG = "wechat_liveshop_refund_order_acceptreturn_tag";
    //商家同意退货
    public static final String WECHAT_LIVESHOP_REFUND_ORDER_REJECT_TAG = "wechat_liveshop_refund_order_reject_tag";




//    public static final String LEVEL_DOWN_COUPON_EXPIRE_TOPIC = "level-down-coupon-expire-topic";
//    /**
//     * 店铺钱包解锁topic
//     */
//    public static final String SHOP_WALLET_UNLOCK_TOPIC = "shop-wallet-unlock-topic";

    /**
     * 订单取消
     */
    public static final String ORDER_CANCEL_TOPIC = "order-cancel-topic";

    /**
     * 订单支付成功
     */
    public static final String ORDER_NOTIFY_TOPIC = "order-notify-topic";

    /**
     * 订单支付成功 通知商家分账服务
     */
    public static final String ORDER_NOTIFY_SHOP_TOPIC = "order-notify-shop-topic";

    /**
     * 订单支付成功 通知优惠券服务
     */
    public static final String ORDER_NOTIFY_COUPON_TOPIC = "order-notify-coupon-topic";

    /**
     * 订单支付成功 通知库存服务
     */
    public static final String ORDER_NOTIFY_STOCK_TOPIC = "order-notify-stock-topic";

    /**
     * 视频号4.0 订单支付成功 通知库存服务
     */
    public static final String EC_ORDER_NOTIFY_STOCK_TOPIC = "ec-order-notify-stock-topic";

    /**
     * 订单确认收货 通知自己然后向周围广播
     */
    public static final String ORDER_RECEIPT_TOPIC = "order-receipt-topic";

//    /**
//     * 订单确认收货 通知商家分账服务
//     */
//    public static final String ORDER_RECEIPT_SHOP_TOPIC = "order-receipt-shop-topic";


    /**
     * 订单退款 通知自己然后向周围广播
     */
    public static final String ORDER_REFUND_TOPIC = "order-refund-topic";

    /**
     * 订单退款 支付服务，开始进行退款啦
     */
    public static final String ORDER_REFUND_PAYMENT_TOPIC = "order-refund-payment-topic";

    /**
     * 订单退款退款成功
     */
    public static final String ORDER_REFUND_SUCCESS_TOPIC = "order-refund-success-topic";
    /**
     * 订单部分退款且订单关闭后结算的消息
     */
    public static final String ORDER_REFUND_SUCCESS_SETTLEMENT_TOPIC = "order-refund-success-settlement-topic";

    /**
     * 订单退款 商家减少余额
     */
    public static final String ORDER_REFUND_SHOP_TOPIC = "order-refund-shop-topic";

    /**
     * 退款还原使用的优惠券
     */
    public static final String ORDER_REFUND_SUCCESS_COUPON_TOPIC = "order-refund-success-coupon-topic";

    /**
     * 退款还原库存
     */
    public static final String ORDER_REFUND_SUCCESS_STOCK_TOPIC = "order-refund-success-stock-topic";

    /**
     * 确认收货订单退货退款还原用户成长值
     */
    public static final String ORDER_REFUND_SUCCESS_GROWTH_TOPIC = "order-refund-success-growth-topic";

    /**
     * 确认收货订单退货退款退款用户购物抵扣使用的积分
     */
    public static final String ORDER_REFUND_SUCCESS_SCORE_TOPIC = "order-refund-success-score-topic";

    /**
     * 团购订单创建通知
     */
    public static final String GROUP_ORDER_CREATE_TOPIC = "group-order-create-topic";

    /**
     * 团购订单取消
     */
    public static final String GROUP_ORDER_CANCEL_TOPIC = "group-order-cancel-topic";

    /**
     * 订单回调，通知团购订单已经支付
     */
    public static final String ORDER_NOTIFY_GROUP_TOPIC = "order-notify-group-topic";
    /**
     * 拼团成团成功通知
     */
    public static final String GROUP_ORDER_SUCCESS_TOPIC = "group-order-success-topic";

    /**
     * 拼团订单成团失败通知
     */
    public static final String GROUP_ORDER_UN_SUCCESS_TOPIC = "group-order-un-success-topic";

    /**
     * 拼团失败进行退款
     */
    public static final String GROUP_ORDER_UN_SUCCESS_REFUND_TOPIC = "group-order-un-success-refund-topic";

    /**
     * 秒杀订单提交通知
     */
    public static final String SECKILL_ORDER_SUBMIT_TOPIC = "seckill-order-submit-topic";

    /**
     * 秒杀订单创建通知
     */
    public static final String SECKILL_ORDER_CREATE_TOPIC = "seckill-order-create-topic";

    /**
     * 秒杀订单取消
     */
    public static final String SECKILL_ORDER_CANCEL_TOPIC = "seckill-order-cancel-topic";

    /**
     * 一口价订单创建通知
     */
    public static final String FIXEDPRICE_ORDER_CREATE_TOPIC = "fixedprice-order-create-topic";

    /**
     * 订单支付成功 通知秒杀服务
     */
    public static final String ORDER_NOTIFY_SECKILL_TOPIC = "order-notify-seckill-topic";

    /**
     * 订单支付成功 通知分销服务
     */
    public static final String ORDER_NOTIFY_DISTRIBUTION_TOPIC = "order-notify-distribution-topic";

    /**
     * 分销通知自己，然后广播到订单和商家服务
     */
    public static final String DISTRIBUTION_NOTIFY_ORDER_SHOP_TOPIC = "distribution-notify-order-shop-topic";

    /**
     * 分销通知订单服务
     */
    public static final String DISTRIBUTION_NOTIFY_ORDER_TOPIC = "distribution-notify-order-topic";

    /**
     * 分销通知商家服务
     */
    public static final String DISTRIBUTION_NOTIFY_SHOP_TOPIC = "distribution-notify-shop-topic";

    /**
     * 用户服务通知分销服务分销员修改分销员信息
     */
    public static final String USER_NOTIFY_DISTRIBUTION_USER_TOPIC = "user-notify-distribution-user-topic";

    /**
     * 用户行为记录保存通知
     */
    public static final String USER_ACTION_SAVE_NOTIFY_TOPIC = "user-action-save-notify-topic";

    /**
     * 退款，通知分销服务
     */
    public static final String REFUND_SHOP_NOTIFY_DISTRIBUTION_TOPIC = "refund-shop-notify-distribution-topic";


    /**
     * 充值成功
     */
    public static final String RECHARGE_NOTIFY_TOPIC = "recharge-notify-topic";

    /**
     * 购买会员成功
     */
    public static final String BUY_VIP_NOTIFY_TOPIC = "buy-vip-notify-topic";

    /**
     * 余额支付
     */
    public static final String BALANCE_PAY_TOPIC = "balance-pay-topic";

    /**
     * 批量用户注册事件
     */
    public static final String BATCH_USER_REGISTER_TOPIC = "batch-user-register-topic";

    /**
     * 发送给用户的消息（订单支付成功、发货、退款同意、退款拒绝） 通知消息服务
     */
    public static final String SEND_NOTIFY_TO_USER_TOPIC = "send-notify-to-user-topic";

    public static final String SEND_NOTIFY_TO_USER_EXTENSION_TOPIC = "send-notify-to-user-extension-topic";

    /**
     * 发送店铺消息（订单确认收货、买家发起退款、买家已退货） 通知消息服务
     */
    public static final String SEND_NOTIFY_TO_SHOP_TOPIC = "send-notify-to-shop-topic";

    /**
     * 发送开团成团相关通知给用户（开团成功、拼团成功、拼团失败）
     */
    public static final String SEND_GROUP_NOTIFY_TO_USER_TOPIC = "send-group-notify-to-user-topic";

    /**
     * 订单15天通知店铺进行结算
     */
    public static final String ORDER_SETTLED_SHOP_TOPIC = "order-settled-shop-topic";
    /**
     * 积分解锁topic
     */
    public static final String SCORE_UNLOCK_TOPIC = "score-unlock-topic";
    /**
     * 计算分销活动效果数据
     */
    public static final String COUNT_DISTRIBUTION_ACTIVITY_SHARE = "count-distribution-activity-share";
    /**
     * 计算分销导购效果数据
     */
    public static final String COUNT_DISTRIBUTION_GUIDE_SHARE = "count-distribution-guide-share";
    /**
     * 统计分销埋点未读数据
     */
    public static final String COUNT_DISTRIBUTION_UNREAD_RECORD = "count-distribution-unread-record";
    /**
     * 批量发券（新优惠券）
     */
    public static final String BATCH_DISTRIBUTION_COUPON_TOPIC = "batch-distribution-coupon-topic";

    /**
     * 员工同步
     */
    public static final String STAFF_SYNC_TOPIC = "staff-sync-topic";


    /**
     * 用户注册有礼
     */
    public static final String USER_REGISTER_GIFT = "user-register-gift";

    /**
     * 用户完善资料有礼
     */
    public static final String USER_PERFECT_DATA = "user-perfect-data";


    /**
     * 中台订单推送
     */
    public static final String ORDER_NOTIFY_STD_TOPIC = "order-notify-std";

    public static final String ORDER_NOTIFY_STD_TOPIC_TAG = "order-notify-std-tag";

    /**
     * 中台退单推送
     */
//    public static final String ORDER_REFUND_STD_TOPIC = "order-refund-std";

    public static final String ORDER_REFUND_STD_GROUP = "order-refund-std-group";
    public static final String ORDER_REFUND_STD_TOPIC_TAG = "order-refund-std-tag";
    /**
     * 微信小程序发送订阅消息
     */
    public static final String SEND_MA_SUBCRIPT_MESSAGE_TOPIC = "send-ma-subcript-message-topic";

    /**
     * 积分礼品到货提醒消息主题
     */
    public static final String SCORE_PRODUCT_ARRIVAL_TOPIC = "score-product-arrival-topic";

    /**
     * 企微好友同步通知(添加或变更)
     */
    public static final String QI_WEI_FRIENDS_SYNC_NOTIFY_TOPIC = "qi-wei-friends-sync-notify-topic";

    /**
     * 导购发送群发任务
     */
    public static final String STAFF_GROUP_TASK_PUSH = "staff-group-task-push";

    /**
     * 导出excel且上传oss
     */
    public static final String SOLD_UPLOAD_EXCEL = "sold-upload-excel";

    /**
     * 会员注册成功发送企业微信消息
     */
    public static final String SEND_BIZ_QI_WEI_MESSAGE_TOPIC = "send-biz-qi_wei-message-topic";

    /**
     * 中台商品信息同步
     */
    public static final String ERP_PRODUCT_INFO_SYNC_MESSAGE_TOPIC = "erp-product-info-sync-message-topic";
    public static final String ERP_PRODUCT_TAG = "erp-product-tag";

    public static final String ERP_PRODUCT_PRICE_TAG = "erp-product-price-tag";
    public static final String ERP_PRODUCT_STOCK_TAG = "erp-product-stock-tag";
    public static final String ERP_PRODUCT_PRICE_GROUP = "iph-product-price-group";
    public static final String ERP_PRODUCT_STOCK_GROUP = "iph-product-stock-group";

    public static final String IPH_PRODUCT_GROUP = "iph-product-group";
    public static final String IPH_PRODUCT_TAG = "iph-product-tag";

    /**
     * 中台商品库存同步
     */
//    public static final String ERP_PRODUCT_STOCK_SYNC_MESSAGE_TOPIC = "erp-product-stock-sync-message-topic";
    public static final String ERP_PRODUCT_STOCK_SYNC_MESSAGE_TOPIC = "erp-product-stock-sync-message-topic20220929";

    /**
     * 中台sku价格同步
     */
    public static final String ERP_PRODUCT_PRICE_SYNC_MESSAGE_TOPIC = "erp-product-price-sync-message-topic";

    /**
     * 电子价签同步
     */
    public static final String ALI_ELECTRONIC_SIGN_SYNC_MESSAGE_TOPIC = "ali-electronic-sign-sync-message-topic";

    /**
     * 用户拉新
     */
    public static final String USER_PULL_NEW_TOPIC="user-pull-new-topic";

    /**
     * 同步积分兑换数据
     */
    public static final String SYNC_POINT_CONVERT_DATA="sync-point-convert-data";

    /**
     * 库存同步设置视频号可售库存为0
     */
    public static final String EC_ZERO_SET_PRODUCT_STOCK_TOPIC="ec-zero-set-product-stock-topic";

    /**
     * 视频号4.0订单支付成功重新计算分销信息
     */
    public static final String EC_ORDER_REBUILD_DISTRIBUTION_TOP ="ec-order-rebuild-distribution-top";

    /**
     * 完善群发任务队列
     * */
    public static final String WRAPPER_GROUP_PUSH_TASK="wrapper-group-push-task";

    /**
     * 创建企微群发任务队列
     * */
//    public static final String START_GROUP_PUSH_TOPIC="start-group-push-topic";

    /**
     * 停止企微群发任务队列
     * */
    public static final String END_GROUP_PUSH_TOPIC="end-group-push-topic";

    /**
     * 同步有数商品相关
     */
    public static final String SYNC_ZHLS_PRODUCT_TOPIC="sync-zhls-product-topic";

    /**
     * 同步有数上架商品
     */
    public static final String SYNC_ZHLS_PUT_SHELF_GROUP="sync-zhls-put-shelf-group";
    public static final String SYNC_ZHLS_PUT_SHELF_TAG="sync-zhls-put-shelf-tag";

    /**
     * 同步有数下架商品
     */
    public static final String SYNC_ZHLS_PUT_OFF_SHELF_GROUP="sync-zhls-put-off-shelf-group";
    public static final String SYNC_ZHLS_PUT_OFF_SHELF_TAG="sync-zhls-put-off-shelf-tag";

    /**
     * 同步有数爱铺货商品
     */
    public static final String SYNC_ZHLS_IPH_PRODUT_GROUP="sync-zhls-iph-produt-group";
    public static final String SYNC_ZHLS_IPH_PRODUT_TAG="sync-zhls-iph-produt-tag";

    /**
     * 同步有数分类修改
     */
    public static final String SYNC_ZHLS_CATEGORY_UPDATE_GROUP="sync-zhls-category-update-group";
    public static final String SYNC_ZHLS_CATEGORY_UPDATE_TAG="sync-zhls-category-update-tag";

    /**
     * 同步有数分类删除
     */
    public static final String SYNC_ZHLS_CATEGORY_DEL_GROUP="sync-zhls-category-del-group";
    public static final String SYNC_ZHLS_CATEGORY_DEL_TAG="sync-zhls-category-del-tag";

    /**
     * 同步有数库存更新
     */
    public static final String SYNC_ZHLS_PRODUCT_STOCK_GROUP="sync-zhls-product-stock-group";
    public static final String SYNC_ZHLS_PRODUCT_STOCK_TAG="sync-zhls-product-stock-tag";

    /**
     * 同步有数价格更新
     */
    public static final String SYNC_ZHLS_PRODUCT_PRICE_GROUP="sync-zhls-product-price-group";
    public static final String SYNC_ZHLS_PRODUCT_PRICE_TAG="sync-zhls-product-price-tag";



    /**
     * 问卷活动开始提醒
     * 延时任务 半小时后检查会员是否提交问卷，如果没有提交发送订阅消息提醒
     */
    public static final String QUESTIONNAIRE_ACTIVITY_BEGIN_TOP ="questionnaire-activity-begin-top";
}
