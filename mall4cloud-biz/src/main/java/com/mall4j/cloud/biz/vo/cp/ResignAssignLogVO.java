package com.mall4j.cloud.biz.vo.cp;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 离职分配日志表VO
 *
 * @author hwy
 * @date 2022-01-24 11:05:43
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ResignAssignLogVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("操作人")
    private Long operater;

    @ApiModelProperty("操作时间")
    private Date createTime;

    @ApiModelProperty("分配类型")
    private Integer assignType;

    @ApiModelProperty("原添加员工")
    private Integer addBy;

    @ApiModelProperty("接替员工")
    private Integer replaceBy;

    @ApiModelProperty("需要分配的客群数")
    private Integer assignTotal;

    @ApiModelProperty("分配成功的人数/群数")
    private Integer successTotal;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("删除标识")
    private Integer flag;



	@Override
	public String toString() {
		return "ResignAssignLogVO{" +
				"id=" + id +
				",operater=" + operater +
				",createTime=" + createTime +
				",assignType=" + assignType +
				",addBy=" + addBy +
				",replaceBy=" + replaceBy +
				",assignTotal=" + assignTotal +
				",successTotal=" + successTotal +
				",status=" + status +
				",flag=" + flag +
				'}';
	}
}
