package com.mall4j.cloud.api.biz.dto.channels.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class EcDeliveryRequest {
	
	//定义一个占位属性,不然请求微信服务器会报错
	private String defineParam;
}
