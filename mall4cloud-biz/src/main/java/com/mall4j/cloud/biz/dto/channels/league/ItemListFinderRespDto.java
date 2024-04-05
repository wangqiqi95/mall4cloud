package com.mall4j.cloud.biz.dto.channels.league;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description 达人
 * @Author axin
 * @Date 2023-04-20 17:55
 **/
@Getter
@Setter
public class ItemListFinderRespDto {

    @ApiModelProperty(value = "达人id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "优选联盟商品id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long productId;

    @ApiModelProperty(value = "达人id")
    private String finderId;

    @ApiModelProperty(value = "达人名称")
    private String finderName;
}
