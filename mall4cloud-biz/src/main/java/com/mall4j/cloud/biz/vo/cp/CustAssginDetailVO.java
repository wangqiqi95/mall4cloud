package com.mall4j.cloud.biz.vo.cp;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 离职客户分配表VO
 *
 * @author hwy
 * @date 2022-01-24 11:05:43
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CustAssginDetailVO extends BaseVO{
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
		return "CustAssginDetailVO{" +
				"id=" + id +
				",custId=" + custId +
				",custName=" + custName +
				",resignId=" + resignId +
				",staus=" + staus +
				",flag=" + flag +
				'}';
	}
}
