package com.mall4j.cloud.api.user.crm;


public enum CrmMethod {
//	MEMBER_POINT_QUERY("/customized-service/v1/member/point/query", "积分末额查询"),
	MEMBER_POINT_QUERY("/customized-service/v1/mainData/point/query", "积分末额查询"),

	MEMBER_POINT_POST("/customized-service/v1/member/point", "积分变更接口"),
	//积分明细查询
	MEMBER_POINTRECORD_GET("/customized-service/v1/member/pointRecord/query", "积分明细查询"),
	MEMBER_CATEGORY_QUERY("/customized-service/v1/member/category/query", "标签分类查询"),
	MEMBER_TAGCATEGORY_QUERY("/customized-service/v1/member/tagCategory/query", "标签列表查询"),
	MEMBER_TAG_QUERY("/customized-service/v1/member/tag/query", "查询用户标签"),
	MEMBER_TAG_UPDATE("/customized-service/v1/member/tag/update", "修改用户标签"),
	TASK_SAVE("/customized-service/v1/guide/task/save", "群发任务同步"),
	MEMBER_QYWX_SAVE("/customized-service/v1/member/qywx/save", "企微数据接收"),
	EDITAGEUSER("/customized-service/v1/mainData/editageUser", "用户数据查询"),
	EDITAGEENQUIRY("/customized-service/v1/mainData/editageEnquiry", "询价单数据查询"),
	EDITAGEORDER("/customized-service/v1/mainData/editageOrder", "订单数据查询"),
	EDITAGEUSERTAG("/customized-service/v1/mainData/editageUserTag", "PLUS会员数据记录查询"),
	EDITAGECARD("/customized-service/v1/mainData/editageCard", "预存款数据记录查询"),
	EDITAGEGROUP("/customized-service/v1/mainData/editageGroup", "团队信息查询"),
	EDITAGEPAYMENT("/customized-service/v1/mainData/editagePayment", "支付信息查询"),
	EDITAGECOUPON("/customized-service/v1/mainData/editageCoupon", "优惠券信息查询"),
	EDITAGECOUPONTRACKER("/customized-service/v1/mainData/editageCouponTracker", "优惠券信息查询"),
	EDITAGEFEEDBACK("/customized-service/v1/mainData/editageFeedBack", "订单评价信息查询"),
	EDITAGEPUBLISH("/customized-service/v1/mainData/editagePublish", "发表信息查询"),
	EDITAGEINVOICE("/customized-service/v1/mainData/editageInvoice", "发票信息查询"),
	EDITAGEBOOKING("/customized-service/v1/mainData/editageBooking", "编辑偏好数据查询"),
	EDITAGEREFERFRIEND("/customized-service/v1/mainData/editageReferFriend", "老带新数据查询"),
	MEMBER_LOG_SMS("/customized-service/v1/mainData/log/sms", "短信发送记录查询"),
	MEMBER_LOG_EDM("/customized-service/v1/mainData/log/edm", "邮件发送记录查询"),
	QRYSERVICEEVALUATION("/customized-service/v1/mainData/qryServiceEvaluation", "服务评价查询"),
	QRYPOINTEXCHANGEORDER("/customized-service/v1/mainData/qryPointExchangeOrder", "积分兑换订单查询"),
	QRYPROSPECTCLIENTPROFILE("/customized-service/v1/mainData/qryProspectClientProfile", "询价单跳出查询"),
	QRYPROSPECTINTERACTION("/customized-service/v1/mainData/qryprospectInteraction", "询价单跳出详情信息查询"),
	QRYCREDITDEBIT("/customized-service/v1/mainData/qryCreditDebit", "qryPaymentDetail"),
	QRYAPPLETUSERPROPERTIES("/customized-service/v1/mainData/qryappletUserproperties", "qryappletUserproperties"),
	QRYFAVORITEEDITOR("/customized-service/v1/mainData/qryFavoriteEditor", "qryFavoriteEditor"),
	QRYEDITORBLACKLISTING("/customized-service/v1/mainData/qryEditorBlacklisting", "qryEditorBlacklisting")
	;



	private String uri;

	private String methodDesc;

	CrmMethod(String uri, String methodDesc) {
		this.uri = uri;
		this.methodDesc = methodDesc;
	}

	public String uri() {
		return uri;
	}

	public String methodDesc() {
		return methodDesc;
	}
}
