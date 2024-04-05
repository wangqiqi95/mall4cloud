
package com.mall4j.cloud.delivery.bo;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 阿里订单快递信息VO
 *
 * @author lhd
 * @date 2020-05-18 15:10:00
 */
public class DeliveryAliInfoBO {

	@ApiModelProperty(value = "物流公司名称",required=true)
	private String expName;

	@ApiModelProperty(value = "物流公司官网",required=true)
	private String expSite;

	@ApiModelProperty(value = "物流订单号",required=true)
	private String number;

	@ApiModelProperty(value = "物流状态",required=true)
	private Integer deliverystatus;

	@ApiModelProperty(value = "物流状态 0:没有记录 1:已揽收 2:运输途中 201:达到目的城市 3:已签收 4:问题件",required=true)
	private List<DeliveryAliItemInfoBO> list;

	public String getExpName() {
		return expName;
	}

	public void setExpName(String expName) {
		this.expName = expName;
	}

	public String getExpSite() {
		return expSite;
	}

	public void setExpSite(String expSite) {
		this.expSite = expSite;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Integer getDeliverystatus() {
		return deliverystatus;
	}

	public void setDeliverystatus(Integer deliverystatus) {
		this.deliverystatus = switchState(deliverystatus);
	}

	public List<DeliveryAliItemInfoBO> getList() {
		return list;
	}

	public void setList(List<DeliveryAliItemInfoBO> list) {
		this.list = list;
	}

	/**
	 * 阿里快递的6种状态码转成快递鸟的6种
	 *
	 * @param deliverystatus 阿里快递状态码
	 * 0 快递收件(揽件)
	 * 1 在途中
	 * 2 正在派件
	 * 3 已签收
	 * 4 派送失败（无法联系到收件人或客户要求择日派送，地址不详或手机号不清）
	 * 5 疑难件（收件人拒绝签收，地址有误或不能送达派送区域，收费等原因无法正常派送）
	 * 6 退件签收
	 * @return 快递鸟状态码： 0:没有记录 1:已揽收 2:运输途中 201:达到目的城市 3:已签收 4:问题件
	 */
	private Integer switchState(Integer deliverystatus) {
		int status = deliverystatus;
		switch (deliverystatus) {
			case 0:
				status = 1;
				break;
			case 1:
				status = 2;
				break;
			case 5:
			case 6:
				status = 4;
				break;
			default:
				break;
		}
		return status;
	}

	@Override
	public String toString() {
		return "DeliveryAliInfoBO{" +
				"expName='" + expName + '\'' +
				", expSite='" + expSite + '\'' +
				", number='" + number + '\'' +
				", deliverystatus=" + deliverystatus +
				", list=" + list +
				'}';
	}

}
