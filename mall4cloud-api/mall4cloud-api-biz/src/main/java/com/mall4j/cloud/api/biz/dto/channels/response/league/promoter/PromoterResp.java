package com.mall4j.cloud.api.biz.dto.channels.response.league.promoter;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description
 * @Author axin
 * @Date 2023-05-06 13:35
 **/
@Data
public class PromoterResp {
    private Long id;

    @ApiModelProperty(value = "视频号id")
    private String finderId;

    @ApiModelProperty(value = "名称")
    private String finderName;

    @ApiModelProperty(value = "绑定门店id")
    private Long storeId;

}
