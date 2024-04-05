package com.mall4j.cloud.api.biz.dto.channels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 视频号4.0运费模板地址信息
 */
@Data
public class AddressInfo {

	//收货人姓名
	@JsonProperty("user_name")
	private String userName;
	
	//邮编
	@JsonProperty("postal_code")
	private String postalCode;
	
	//国标收货地址第一级地址(省份)
	@JsonProperty("province_name")
	private String provinceName;
	
	//国标收货地址第二级地址(城市)
	@JsonProperty("city_name")
	private String cityName;
	
	//国标收货地址第三级地址(区)
	@JsonProperty("county_name")
	private String countyName;
	
	//详细收货地址信息
	@JsonProperty("detail_info")
	private String detailInfo;
	
	//收货地址国家码
	@JsonProperty("national_code")
	private String nationalCode;
	
	//收货人手机号码
	@JsonProperty("tel_number")
	private String telNumber;
	
	//纬度
	private Double lat;
	
	//维度
	private Double lng;
	
	//门牌号
	@JsonProperty("house_number")
	private String houseNumber;
	
}
