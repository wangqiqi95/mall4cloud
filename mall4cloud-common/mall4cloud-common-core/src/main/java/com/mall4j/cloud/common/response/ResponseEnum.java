package com.mall4j.cloud.common.response;

/**
 * @author FrozenWatermelon
 * @date 2020/7/9
 */
public enum ResponseEnum {

	/**
	 * ok
	 */
	OK("00000", "ok"),

	/**
	 * 用于直接显示提示用户的错误，内容由输入内容决定
	 */
	SHOW_FAIL("A00001", ""),

	/**
	 * 方法参数没有校验，内容由输入内容决定
	 */
	METHOD_ARGUMENT_NOT_VALID("A00002", ""),

	/**
	 * 无法读取获取请求参数
	 */
	HTTP_MESSAGE_NOT_READABLE("A00003", "请求参数格式有误"),

	/**
	 * 未授权
	 */
	UNAUTHORIZED("A00004", "Unauthorized"),

	/**
	 * 服务器出了点小差
	 */
	EXCEPTION("A00005", "服务器出了点小差"),

    RATELIMITEXCEPTION("B00016", "网络繁忙，请重试"),

	/**
	 * 验证码有误
	 */
	VERIFICATION_CODE_ERROR("A00006", "验证码有误或已过期"),

	/**
	 * 数据异常
	 */
	DATA_ERROR("A00007", "数据异常，请刷新后重新操作"),

	/**
	 * 一些需要登录的接口，而实际上因为前端无法知道token是否已过期，导致token已失效时，
	 * 应该返回一个状态码，告诉前端token已经失效了，及时清理
	 */
	CLEAN_TOKEN("A00008", "clean token"),

	/**
	 * 刷新token已过期
	 */
	REFRESH_TOKEN_EXIST("A00009", "refresh token exist"),

	/**
	 * 数据不完整
	 */
	DATA_INCOMPLETE("A00010", "数据不完整"),

	/**
	 * 有敏感词
	 */
	SENSITIVE_WORD("A00011","存在敏感词，请重新输入"),

	/**
	 * 重新登录
	 */
	NEED_LOGIN("A00020","请重新登录"),

	/**
	 * 01开头代表商品
	 * 商品已下架，返回特殊的状态码，用于渲染商品下架的页面
	 */
	SPU_NOT_EXIST("A01000", "商品不存在"),

	SPU_DOWN_STATUS("A01100", "该商品已经下架，如有疑问欢迎联系在线客服"),

	/**
	 * 02开头代表购物车
	 */
	SHOP_CART_NOT_EXIST("A02000", "shop cart not exist"),

	/**
	 * 03开头代表订单
	 */
	ORDER_NOT_EXIST("A03000", "当前订单不属于你哦"),

	/**
	 * 订单不支持该配送方式
	 */
	ORDER_DELIVERY_NOT_SUPPORTED("A03001", "The delivery method is not supported"),

	/**
	 * 请勿重复提交订单，
	 * 1.当前端遇到该异常时，说明前端防多次点击没做好
	 * 2.提示用户 订单已发生改变，请勿重复下单
	 */
	REPEAT_ORDER("A03002", "please don't repeat order"),

	/**
	 * 库存不足，body会具体返回那个skuid的库存不足，后台通过skuId知道哪个商品库存不足，前端不需要判断
	 */
	NOT_STOCK("A03010", "not stock"),

	/**
	 * 积分不足，用户拥有的积分小于购买商品所需的积分(积分订单使用)
	 */
	NOT_SCORE("A03020", "not score"),

	/**
	 * 04 开头代表注册登录之类的异常状态
	 * 社交账号未绑定，当前端看到该异常时，应该在合适的时间（比如在购买的时候跳）根据社交账号的类型，跳转到绑定系统账号的页面
	 */
	SOCIAL_ACCOUNT_NOT_BIND("A04001", "social account not bind"),

	/**
	 * 有些时候第三方系统授权之后，会有个临时的key，比如小程序的session_key
	 * 这个异常代表session_key过期，前端遇到这个问题的时候，应该再次调用社交登录的接口，刷新session_key
	 */
	BIZ_TEMP_SESSION_KEY_EXPIRE("A04002", "biz temp session key expire"),

	/**
	 * 账号未注册，前端看到这个状态码，弹出选择框，提示用户账号未注册，是否进入注册页面，用户选择是，进入注册页面
	 */
	ACCOUNT_NOT_REGISTER("A04003", "account not register"),

	/**
	 * 企微用户未绑定员工
	 */
	QI_WEI_ACCOUNT_NOT_BIND_STAFF("A04004", "qiwei account not bind staff"),

	/**
	 * 为了避免定时任务过慢，而商品活动实际已过期时，获取活动时异常信息用特殊的状态码返回，防止服务渲染出现错误
	 */
	ACTIVITY_END("A05001", "activity end"),
	NO_ACTIVITY("A05002", "no activity"),
	AD_LIMIT("A05003", "广告达到频率限制"),
	USER_LEVEL_LIMIT("A05004", "用户等级不满足"),

	USER_FRIED_ASSISTANCE("A05005","您已经助力过了"),
	
	USER_DYNAMIC_CODE_EXPIRE("A050016","会员码已失效，请重新扫码"),
	USER_ERROR_DYNAMIC_CODE("A050017","手机号未注册或会员二维码不正确"),

	CHOOSE_MEMBER_EVENT_RESTRICT_NUM_VALID("A06001","领取数量已达上限，可跳转优惠券查看"),
	CHOOSE_MEMBER_EVENT_START_TIME_VALID("A06002","活动尚未开始"),
	CHOOSE_MEMBER_EVENT_END_TIME_VALID("A06003","活动已结束"),
	CHOOSE_MEMBER_EVENT_ENABLED_STATUS_VALID("A06004","活动已失效，请联系工作人员"),
	CHOOSE_MEMBER_EVENT_NON_MEMBER_VALID("A06005",""),//非注册会员
	CHOOSE_MEMBER_EVENT_NON_CHOOSE_MEMBER_VALID("A06006",""),//非选中会员
	;

	private final String code;

	private final String msg;

	public String value() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	ResponseEnum(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "ResponseEnum{" + "code='" + code + '\'' + ", msg='" + msg + '\'' + "} " + super.toString();
	}

}
