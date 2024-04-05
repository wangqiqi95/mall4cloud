package com.mall4j.cloud.biz.dto.channels;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 视频号4.0地址管理DTO
 */
@Data
public class ChannelsAddressDTO {
	
	@ApiModelProperty("主键id")
	private Long id;
	
	@ApiModelProperty("微信侧地址id")
	private String addressId;

	@ApiModelProperty("收货人名称")
	private String receiverName;

	@ApiModelProperty("收货人手机号")
	private String telNumber;

	@ApiModelProperty("国家")
	private String country;

	@ApiModelProperty("省")
	private String province;

	@ApiModelProperty("市")
	private String city;

	@ApiModelProperty("区")
	private String town;
	
	@ApiModelProperty("详细收货地址")
	private String detailedAddress;

	@ApiModelProperty("邮编")
	private String postCode;

	@ApiModelProperty("是否默认收货地址 0 否 1是")
	private Integer defaultRecv;
	

}
