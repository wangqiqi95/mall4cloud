package com.mall4j.cloud.biz.vo.cp;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 客群分配表 VO
 *
 * @author hwy
 * @date 2022-01-24 11:05:43
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CustGroupAssginVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty("群ID")
    private Long groupId;

    @ApiModelProperty("群客户数")
    private Integer custGroupNums;

    @ApiModelProperty("离职操作ID")
    private Long resignId;

    @ApiModelProperty("分配状态")
    private Integer status;

    @ApiModelProperty("删除标识")
    private Integer flag;

	@Override
	public String toString() {
		return "CustGroupAssginVO{" +
				"id=" + id +
				",groupId=" + groupId +
				",custGroupNums=" + custGroupNums +
				",resignId=" + resignId +
				",status=" + status +
				",flag=" + flag +
				'}';
	}
}
