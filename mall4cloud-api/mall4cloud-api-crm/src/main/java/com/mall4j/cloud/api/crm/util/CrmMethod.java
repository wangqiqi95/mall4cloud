package com.mall4j.cloud.api.crm.util;


public enum CrmMethod {
	MEMBER_POINT_QUERY("/fast-service/v1.0.0/api/point/get", "积分末额查询"),
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
