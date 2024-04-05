package com.mall4j.cloud.biz.dto.channels.league;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description 达人返回列表
 * @Author axin
 * @Date 2023-02-13 16:38
 **/
@Data
public class PromoterListRespDto {

    @ApiModelProperty(value = "id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "视频号id")
    private String finderId;

    @ApiModelProperty(value = "视频号名称")
    private String finderName;

    @ApiModelProperty(value = "门店id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long storeId;

    @ApiModelProperty(value = "门店编码")
    private String storeCode;

    @ApiModelProperty(value = "门店名称")
    private String storeName;

    @ApiModelProperty(value = "合作状态")
    private Integer status;

    @ApiModelProperty(value = "合作状态描述")
    private String statusName;


}
