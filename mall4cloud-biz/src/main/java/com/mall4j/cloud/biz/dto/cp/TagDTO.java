package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 客户标签配置表DTO
 *
 * @author hwy
 * @date 2022-01-24 11:05:43
 */
@Data
public class TagDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("标签组id")
    private Long id;

    @ApiModelProperty("标签组名称")
    private String tagName;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("删除标识")
    private Integer flag;

	@Override
	public String toString() {
		return "TagDTO{" +
				"id=" + id +
				",tagName=" + tagName +
				",status=" + status +
				",flag=" + flag +
				'}';
	}
}
