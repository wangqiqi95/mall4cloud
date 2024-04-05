
package com.mall4j.cloud.delivery.bo;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 订单快递100信息VO
 *
 * @author lhd
 * @date 2020-05-18 15:10:00
 */
public class DeliveryHundredInfoBO {

	@ApiModelProperty(value = "物流公司名称",required=true)
	private String companyName;

	@ApiModelProperty(value = "物流公司官网",required=true)
	private String companyHomeUrl;

	@ApiModelProperty(value = "物流订单号",required=true)
	private String dvyFlowId;

	@ApiModelProperty(value = "物流状态 0:没有记录 1:已揽收 2:运输途中 201:达到目的城市 3:已签收 4:问题件",required=true)
	private Integer State;

	@ApiModelProperty(value = "查询出的物流信息",required=true)
	private List<DeliveryHundredItemInfoBO> data;

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyHomeUrl() {
		return companyHomeUrl;
	}

	public void setCompanyHomeUrl(String companyHomeUrl) {
		this.companyHomeUrl = companyHomeUrl;
	}

	public String getDvyFlowId() {
		return dvyFlowId;
	}

	public void setDvyFlowId(String dvyFlowId) {
		this.dvyFlowId = dvyFlowId;
	}

	public Integer getState() {
		return State;
	}

	public void setState(Integer state) {
		this.State = switchState(state);
	}

	public List<DeliveryHundredItemInfoBO> getData() {
		return data;
	}

	public void setData(List<DeliveryHundredItemInfoBO> data) {
		this.data = data;
	}

	/**
	 * 快递100的13种状态码转成快递鸟的6种
	 *
	 * @param state 快递100状态码
	 *              0	 在途	快件处于运输过程中
	 *              1	 揽收	快件已由快递公司揽收
	 *              2	 疑难	快递100无法解析的状态，或者是需要人工介入的状态， 比方说收件人电话错误。
	 *              3	 签收	正常签收
	 *              4	 退签	货物退回发货人并签收
	 *              5	 派件	货物正在进行派件
	 *              6	 退回	货物正处于返回发货人的途中
	 *              7	 转单	货物转给其他快递公司邮寄
	 *              10 待清关	货物等待清关
	 *              11 清关中	货物正在清关流程中
	 *              12 已清关	货物已完成清关流程
	 *              13 清关异常	货物在清关过程中出现异常
	 *              14 收件人拒签	收件人明确拒收
	 * @return 快递鸟状态码： 0:没有记录 1:已揽收 2:运输途中 201:达到目的城市 3:已签收 4:问题件
	 */
	private Integer switchState(Integer state) {
		int i = 0;
		switch (state) {
			case 1:
				i = 1;
				break;
			case 5:
				i = 201;
				break;
			case 3:
				i = 3;
				break;
			case 0:
			case 7:
			case 10:
			case 11:
			case 12:
				i = 2;
				break;
			case 2:
			case 13:
			case 14:
			case 4:
			case 6:
				i = 4;
				break;
			default:
				break;
		}
		return i;
	}

	@Override
	public String toString() {
		return "DeliveryHundredInfoBO{" +
				"companyName='" + companyName + '\'' +
				", companyHomeUrl='" + companyHomeUrl + '\'' +
				", dvyFlowId='" + dvyFlowId + '\'' +
				", state=" + State +
				", data=" + data +
				'}';
	}
}
