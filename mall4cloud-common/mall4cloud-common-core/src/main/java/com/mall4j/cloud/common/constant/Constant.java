package com.mall4j.cloud.common.constant;

import com.mall4j.cloud.common.i18n.LanguageEnum;

/**
 * 常量
 * @author FrozenWatermelon
 */
public class Constant {

	/** 超级管理员ID */
	public static final int SUPER_ADMIN_ID = 1;

	/**
	 * 自营店id
	 */
	public static final long MAIN_SHOP = 1L;

	/**
	 * 官方商城id
	 */
	public static final long WEBSITE_SHOP = 1000001L;

	/**
	 * 商家端用户如果注册了，而没有申请创建店铺创建时的shopId
	 */
	public static final long DEFAULT_SHOP_ID = -1L;

	/**
	 * 如果把平台的数据也保存在店铺里面，如分类，热搜之类的，保存的店铺id
	 */
	public static final long PLATFORM_SHOP_ID = 0L;

	/**
	 * 默认初始id
	 */
	public static final long DEFAULT_ID = 0L;
	/**
	 * 平台条件标签上限
	 */
	public static final long TAG_LIMIT_NUM = 20;
	/**
	 * 店铺签约的平台分类数量上限
	 */
	public static final int SIGNING_CATEGORY_LIMIT_NUM = 200;
	/**
	 * 店铺签约的品牌数量上限
	 */
	public static final int SIGNING_BRAND_LIMIT_NUM = 50;
	/**
	 * 店铺可以绑定的银行卡上限
	 */
	public static final int SHOP_BANK_CARD_LIMIT_NUM = 5;
	/**
	 * 平台店名称
	 */
	public static final String PLATFORM_SHOP_NAME = "官方店";

	/**
	 * 积分名称
	 */
	public static final String SCORE_CONFIG = "SCORE_CONFIG";
	public static final String SCORE_EXPLAIN = "SCORE_EXPLAIN";
	public static final String LEVEL_SHOW = "LEVEL_SHOW";
	public static final String SCORE_EXPIRE = "SCORE_EXPIRE";
	public static final String SCORE_QUESTION = "SCORE_QUESTION";
	/**
	 * 商城缺失sku属性时的字符描述
	 */
	public static final String SKU_PREFIX = "规格:";
	public static final String DEFAULT_SKU = "规格";
	/**
	 * 成长值名称
	 */
	public static final String GROWTH_CONFIG = "GROWTH_CONFIG";

	/** 会员初始等级id*/
	public static final int USER_LEVEL_INIT = 1;


	/** 系统菜单最大id */
	public static final int SYS_MENU_MAX_ID = 30;

	/**
	 * 最大确认收货退款时间7天
	 * 20220608 按客户要求修改为14天
	 */
	public static final int MAX_FINALLY_REFUND_TIME = 14;

	/**
	 * 退款最长申请时间，当申请时间过了这个时间段之后，会取消退款申请
	 */
	public static final int MAX_REFUND_APPLY_TIME = 30;
	/**
	 * 离即将退款超时x小时时提醒
	 */
	public static final int MAX_REFUND_HOUR = 12;
	/**
	 * 直播间置顶上限个数
	 */
	public static final int MAX_TOP_NUM = 10;

	/**
	 * 分销佣金结算在确认收货后的时间，维权期过后（7+7+1）
	 */
	public static final int DISTRIBUTION_SETTLEMENT_TIME = MAX_FINALLY_REFUND_TIME + MAX_REFUND_APPLY_TIME + 1;

	/**
	 * 查询订单成功状态
	 */
	public static final String SUCCESS = "SUCCESS";

	/**
	 * 一级分类id
	 */
	public static final Long CATEGORY_ID = 0L;

	/**
	 * 优惠券、店铺列表中的 3件商品
	 */
	public static final Integer SIZE_OF_THREE = 3;

	/**
	 * 店铺收藏返回的商品数量
	 */
	public static final Integer SPU_SIZE_FIVE = 5;
	/**
	 * 一个月天数
	 */
	public static final Integer MONTH_DAY = 30;
	/**
	 * 一天小时数
	 */
	public static final Integer DAY_HOUR = 24;

	/**
	 * 配置名称
	 */
	public static final String ALIPAY_CONFIG = "ALIPAY_CONFIG";
	public static final String WXPAY_CONFIG = "WXPAY_CONFIG";
	public static final String QINIU_CONFIG = "QINIU_CONFIG";
	public static final String ALI_OSS_CONFIG = "ALI_OSS_CONFIG";
	public static final String HUAWEI_OBS_CONFIG = "HUAWEI_OBS_CONFIG";
	public static final String QUICKBIRD_CONFIG = "QUICKBIRD_CONFIG";
	public static final String QUICK100_CONFIG = "QUICK100_CONFIG";
	public static final String ALI_QUICK_CONFIG = "ALI_QUICK_CONFIG";
	public static final String MA_CONFIG = "MA_CONFIG";
	public static final String MP_CONFIG = "MP_CONFIG";

	public static final String EC_CONFIG = "EC_CONFIG";

	public static final String EC_CHANNELS_SHOP_ONE_CONFIG = "EC_CHANNELS_SHOP_ONE_CONFIG";

	public static final String WECHAT_OPEN_CONFIG = "WECHAT_OPEN_CONFIG";
	public static final String MA_SHORLINK_DOMAIN = "MA_SHORLINK_DOMAIN";
	public static final String ALIDAYU_CONFIG = "ALIDAYU_CONFIG";
	public static final String DOMAIN_CONFIG = "DOMAIN_CONFIG";
	public static final String MX_APP_CONFIG = "MX_APP_CONFIG";
	public static final String SENSITIVE_WORDS = "SENSITIVE_WORDS";
	public static final String WECHAT_LIVE_STORE_OPEN_CONFIG = "WECHAT_LIVE_STORE_OPEN_CONFIG";
	//会员升级提示语
	public static final String UP_GRADE_HINT = "UP_GRADE_HINT";

	public static final String SPU_PRICE_DISCOUNT_CONNFIG = "SPU_PRICE_DISCOUNT_CONNFIG";
	public static final String ORDER_PRICE_DISCOUNT_CONNFIG = "ORDER_PRICE_DISCOUNT_CONNFIG";


	/**
	 * 分销相关配置
	 */
	public static final String DISTRIBUTION_CONFIG = "DISTRIBUTION_CONFIG";
	public static final String DISTRIBUTION_RECRUIT_CONFIG = "DISTRIBUTION_RECRUIT_CONFIG";

	/**
	 * 记录缓存名称
	 */
	public static final String FLOW_ANALYSIS_LOG = "flowAnalysisLog";

	/**
	 * 已评论
	 */
	public static final Integer IS_COMM = 1;

	/**
	 * 消息类型，100为分界线，大于100为发送给用户的，小于100为发送给商家的
	 */
	public static final Integer MSG_TYPE = 100;

	/**
	 * 分类间隔
	 */
	public static final String CATEGORY_INTERVAL = "-";

	/**
	 * 逗号
	 */
	public static final String COMMA = ",";

	/**
	 * 中文逗号
	 */
	public static final String CN_COMMA = "，";

	/**
	 * 句号（英文符号）
	 */
	public static final String PERIOD = ".";
	/**
	 * 冒号
	 */
	public static final String COLON = ":";
	/**
	 * 分号
	 */
	public static final String SEMICOLON = ";";
	/**
	 * 下划线
	 */
	public static final String UNDERLINE = "_";

	/**
	 * 星号
	 */
	public static final String ASTERISK = "*";

	/**
	 * 默认金额
	 */
	public static final Long DEFAULT_AMOUNT = 0L;


	/**
	 * 字符串最大长度限制
	 */
	public static final Integer MAX_FIELD_LIMIT = 500;


	/**
	 * 中评
	 */
	public static final Integer MEDIUM_RATING = 3;

	/**
	 * 价格倍率
	 */
	public static final Double PRICE_MAGNIFICATION = 100D;

	/**
	 * 待支付
	 */
	public static final String UNPAID = "待支付";

	/**
	 * 暂无售后
	 */
	public static final String NO_AFTER_SALES = "暂无售后";
	/**
	 * 商品最小金额（一分钱）
	 */
	public static final Long MIN_SPU_AMOUNT = 1L;
	/**
	 * 商品最大金额（十万）
	 */
	public static final Long MAX_SPU_AMOUNT = 10000000L;

	/**
	 * excel下拉框行开始行
	 */
	public static final Integer START_ROW = 2;


	/**
	 * 最大的excel下拉框行
	 */
	public static final Integer LAST_ROW = 10000;

	/**
	 * x天后订单数据存入es（天）
	 */
	public static final Integer ES_ORDER_DATE = 10;

	/**
	 * 订单取消时间（分钟）
	 */
	public static final Integer CANCEL_ORDER = 30;

	/**
	 * 分销提现频率
	 */
	public static final Integer FREQUENCY_DAY = 30;


	/**
	 * 团购队伍-系统参团
	 */
	public static final String GROUP_SYSTEM_USER = "系统参团";


	/**
	 * 使用商品浏览记录进行推荐时，使用的数据数量
	 */
	public static final Integer MAX_SPU_BROWSE_NUM = 50;
	/**
	 * 置顶品牌显示数量
	 */
	public static final Integer BRAND_TOP_NUM = 24;
    public static final Integer DEFAULT_LANG = LanguageEnum.LANGUAGE_ZH_CN.getLang();
	/**
	 * 语言请求头字段
	 */
	public static final String LOCALE = "locale";
	/**
	 * 流量订单数据统计延后处理时间（单位：分钟）-支付最迟需要30分钟才处理完成订单，所以延后处理时间要大于30
	 */
	public static final Integer FLOW_ORDER_HANDLE_TIME = 40;
	/**
	 * 数据量大时，系统单次处理数据的数量
	 */
	public static final Long MAX_DATA_HANDLE_NUM = 5000L;
	/**
	 * 0-Integer类型
	 */
    public static final Integer ZERO_INTEGER = 0;
	/**
	 * 0-Long类型
	 */
    public static final Long ZERO_LONG = 0L;
	/**
	 * 3个月
	 */
	public static final  Integer THREE_MONTH = 3;
	/**
	 * 6个月
	 */
	public static final  Integer SIX_MONTH = 6;
	/**
	 * 12个月
	 */
	public static final  Integer TWELVE_MONTH = 12;

	public static final  String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final  String DATE_FORMAT_YMD = "yyyy-MM-dd";


	/**
	 * 商品分组中的商品数量
	 */
    public static final Integer TAG_SPU_NUM = 3;
	/**
	 * 订单最小金额
	 * 单位：分
	 */
	public static final Long MIN_ORDER_AMOUNT = 1L;
	/**
	 * 一天中最大的小时数
	 */
	public static final int MAX_HOUR_NUM_BY_DAY = 24;
	/**
	 * 100及以上则为店铺消息
	 */
	public static final Integer MULTI_SHOP_MESSAGE_TYPE = 100;
	/**
	 * 用户未关注公众号的错误码
	 */
	public static final int WX_MAX_NOT_FOLLOW = 43004;
	public static final Integer MAX_PAGE_SIZE = 10000;
	/**
	 * 用户拥有的最大余额
	 */
	public static final long MAX_USER_BALANCE = 99999999999L;
}
