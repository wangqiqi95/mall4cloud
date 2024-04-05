package com.mall4j.cloud.api.docking.skq_crm.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 类描述：积分明细
 *
 * @date 2022/1/24 9:30：20
 */
public class PointDetailVo implements Serializable {

	private static final long serialVersionUID = 7502637382351333415L;
	@ApiModelProperty(value = "积分类型,定义后续提供")
	private String point_type;

	@ApiModelProperty(value = "变更时间,yyyy-MM-dd HH:mm:ss")
	private String change_date;

	@ApiModelProperty(value = "变更分值")
	private Integer change_value;

	@ApiModelProperty(value = "变更类型,GAIN:增加,DEDUCT:扣减")
	private String change_type;

	@ApiModelProperty(value = "变更后积分")
	private Integer after_point;

	@ApiModelProperty(value = "变更说明")
	private String change_desc;

	public String getPoint_type() {
		return point_type;
	}

	public void setPoint_type(String point_type) {
		this.point_type = point_type;
	}

	public String getChange_date() {
		return change_date;
	}

	public void setChange_date(String change_date) {
		this.change_date = change_date;
	}

	public Integer getChange_value() {
		return change_value;
	}

	public void setChange_value(Integer change_value) {
		this.change_value = change_value;
	}

	public String getChange_type() {
		return change_type;
	}

	public void setChange_type(String change_type) {
		this.change_type = change_type;
	}

	public Integer getAfter_point() {
		return after_point;
	}

	public void setAfter_point(Integer after_point) {
		this.after_point = after_point;
	}

	public String getChange_desc() {
		return change_desc;
	}

	public void setChange_desc(String change_desc) {
		this.change_desc = change_desc;
	}

	@Override
	public String toString() {
		return "PointDetailVo{" + "point_type='" + point_type + '\'' + ", change_date='" + change_date + '\'' + ", change_value=" + change_value
				+ ", change_type='" + change_type + '\'' + ", after_point=" + after_point + ", change_desc='" + change_desc + '\'' + '}';
	}
}
