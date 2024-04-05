package com.mall4j.cloud.common.order.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

/**
 * 用户提货人信息VO
 *
 * @author FrozenWatermelon
 * @date 2020-12-15 17:18:56
 */
public class UserConsigneeVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("提货人姓名")
    private String name;

    @ApiModelProperty("提货人联系方式")
    private String mobile;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Override
	public String toString() {
		return "UserConsigneeVO{" +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",name=" + name +
				",mobile=" + mobile +
				'}';
	}
}
