package com.mall4j.cloud.distribution.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 门店活动
 *
 * @author gww
 * @date 2021-12-26 21:17:59
 */
@Data
public class DistributionStoreActivitySizes implements Serializable{
    private static final long serialVersionUID = 1L;

	@ApiModelProperty("主键ID")
    private Long id;

	@ApiModelProperty("活动ID")
    private Long activityId;

	@ApiModelProperty("类型 ： 1：衣服，2：鞋子")
    private Integer type;

	@ApiModelProperty("尺码值")
	private String size;
}
