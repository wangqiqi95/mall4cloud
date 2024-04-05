package com.mall4j.cloud.order.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 纸质券订单VO
 */
@Data
public class PaperCouponOrderItemVO {
	
	//订单id
	private Long orderId;
	
	//订单编号
	private String orderNumber;
	
	//商品数量
	private Integer count;
	
	private Long spuId;
	
	private Long skuId;
	
	//产品价格
	private Long price;
	
	//商品实际金额
	private Long actualTotal;
	
	//优惠金额
	private Long shareReduce;

	//订单创建时间
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date createTime;
	
	//订单支付时间
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date payTime;
}
