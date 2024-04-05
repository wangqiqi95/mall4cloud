package com.mall4j.cloud.api.openapi.ipuhuo.enums;

/**
 * 类描述：请求方法名称 枚举类
 */
public enum ReqMethodType {
	GoodList("Differ.IPH.Business.GoodsList", "查询商品列表"),
	Goods("Differ.IPH.Business.Goods", "获取商品详细信息"),
	AddGoods("Differ.IPH.Business.AddGoods", "添加商品"),
	UploadImage("Differ.IPH.Business.UploadImage", "上传图片"),
	AddGoodsTag("Differ.IPH.Business.AddGoodsTag", "创建分组"),
	ShopInfo("Differ.IPH.Business.ShopInfo", "获取店铺基础信息"),
	UserInfo("Differ.IPH.Business.UserInfo", "用户"),
	CategoryList("Differ.IPH.Business.CategoryList", "获取类目");

	private String methodName;

	private String methodDesc;

	ReqMethodType(String methodName, String methodDesc) {
		this.methodName = methodName;
		this.methodDesc = methodDesc;
	}

	public String getMethodName() {
		return methodName;
	}

	public String getMethodDesc() {
		return methodDesc;
	}

	public static ReqMethodType getReqMethodType(String methodName) {
		ReqMethodType[] enums = values();
		for (ReqMethodType reqMethodType : enums) {
			if (reqMethodType.getMethodName().equals(methodName)) {
				return reqMethodType;
			}
		}
		return null;
	}
}
