package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 离职客户分配表DTO
 *
 * @author hwy
 * @date 2022-01-24 11:05:43
 */
@Data
public class CustAssginDetailDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("客户ID")
    private Long custId;

    @ApiModelProperty("客户名称")
    private String custName;

    @ApiModelProperty("离职操作ID")
    private Long resignId;

    @ApiModelProperty("分配状态")
    private Integer staus;

    @ApiModelProperty("删除标识")
    private Integer flag;

	@Override
	public String toString() {
		return "CustAssginDetailDTO{" +
				"id=" + id +
				",custId=" + custId +
				",custName=" + custName +
				",resignId=" + resignId +
				",staus=" + staus +
				",flag=" + flag +
				'}';
	}
}
