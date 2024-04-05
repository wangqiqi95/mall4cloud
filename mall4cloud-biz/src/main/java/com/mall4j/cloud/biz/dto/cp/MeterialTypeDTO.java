package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 分类类型查询DTO
 *
 * @author hwy
 * @date 2022-01-24 11:05:43
 */
@Data
public class MeterialTypeDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty("分类ID")
	private Long id;

	@Override
	public String toString() {
		return "MeterialTypeDTO{" +
				"id=" + id +
				'}';
	}
}
