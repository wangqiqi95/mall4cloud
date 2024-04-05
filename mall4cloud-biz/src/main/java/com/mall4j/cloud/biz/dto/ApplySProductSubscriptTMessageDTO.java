package com.mall4j.cloud.biz.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Grady_Lu
 */
@Data
public class ApplySProductSubscriptTMessageDTO extends ApplySubscriptTMessageDTO{

    @ApiModelProperty(value = "积分活动Id",required = true)
    private Long convertId;

    @ApiModelProperty(value = "门店编码",required = true)
    private String storeCode;

    @ApiModelProperty(value = "spuId集合",required = true)
    private List<Long> spuIdList;

}
