package com.mall4j.cloud.api.biz.dto.channels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EcAddressDetail {

	//地址id
	@JsonProperty("address_id")
	private String addressId;
	
	//联系人姓名
	private String name;
	
	//地区信息
	@JsonProperty("address_info")
	private AddressInfo addressInfo;
	
	//座机
	private String landline;
	
	//备注
	private String remark;
	
	//是否为发货地址
	@JsonProperty("send_addr")
	private Boolean sendAddr;
	
	//是否为默认发货地址
	@JsonProperty("default_send")
	private Boolean defaultSend;
	
	//是否为收货地址
	@JsonProperty("recv_addr")
	private Boolean recvAddr;
	
	//是否为默认收货地址
	@JsonProperty("default_recv")
	private Boolean defaultRecv;
	
	//创建时间戳（秒)
	@JsonProperty("create_time")
	private Long createTime;
	
	//更新时间戳（秒）
	@JsonProperty("update_time")
	private Long updateTime;
	
	//线下配送地址类型
	@JsonProperty("address_type")
	private EcOfflineAddressType addressType;
}
