package com.mall4j.cloud.biz.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 微信门店回复内容DTO
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 16:43:04
 */
@Data
public class WeixinStoresPutDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private String id;

	@NotEmpty
    @ApiModelProperty("门店名称")
    private String storeName;
    private String stationName;

	@NotEmpty
    @ApiModelProperty("门店id")
    private String storeId;


}
