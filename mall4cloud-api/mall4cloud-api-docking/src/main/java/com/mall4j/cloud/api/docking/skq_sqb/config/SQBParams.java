package com.mall4j.cloud.api.docking.skq_sqb.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * 收钱吧聚合支付参数配置类
 */
@RefreshScope
@Configuration
@Data
public class SQBParams {
	//轻POS应用编号，商户入网后由收钱吧技术支持提供
	@Value("${sqb.appid}")
	private String appid;
	//品牌编号，系统对接前由"收钱吧"分配并提供
	@Value("${sqb.brand_code}")
	private String brand_code;
	//门店编号
	@Value("${sqb.store_sn}")
	private String store_sn;
	//商户私钥
	@Value("${sqb.privateKey}")
	private String privateKey;
	//收钱吧公钥
	@Value("${sqb.publicKey}")
	private String publicKey;
	//订单过期时间
	@Value("${sqb.expire_time}")
	private String expire_time;
	//支付类型
	@Value("${sqb.orderPayType}")
	private Integer orderPayType;
	
	public String getStore_sn() {
		return store_sn;
	}
	
	public void setStore_sn(String store_sn) {
		this.store_sn = store_sn;
	}
	
	public Integer getOrderPayType() {
		return orderPayType;
	}
	
	public void setOrderPayType(Integer orderPayType) {
		this.orderPayType = orderPayType;
	}
	
	public String getExpire_time() {
		return expire_time;
	}
	
	public void setExpire_time(String expire_time) {
		this.expire_time = expire_time;
	}
	
	public String getAppid() {
		return appid;
	}
	
	public void setAppid(String appid) {
		this.appid = appid;
	}
	
	public String getBrand_code() {
		return brand_code;
	}
	
	public void setBrand_code(String brand_code) {
		this.brand_code = brand_code;
	}
	
	public String getPrivateKey() {
		return privateKey;
	}
	
	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
	
	public String getPublicKey() {
		return publicKey;
	}
	
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}
	
	@Override
	public String toString() {
		return "SQBParams{" +
				"appid='" + appid + '\'' +
				", brand_code='" + brand_code + '\'' +
				", store_sn='" + store_sn + '\'' +
				", privateKey='" + privateKey + '\'' +
				", publicKey='" + publicKey + '\'' +
				", expire_time='" + expire_time + '\'' +
				", orderPayType=" + orderPayType +
				'}';
	}
}
