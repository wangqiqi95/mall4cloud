package com.mall4j.cloud.coupon.dto;

import com.mall4j.cloud.common.database.dto.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class QueryChooseMemberEventPageDTO extends PageDTO {

    @ApiModelProperty(value = "活动标题")
    private String eventTitle;

    @ApiModelProperty(value = "0未开启，1开启")
    private Integer eventEnabledStatus;

    @ApiModelProperty(value = "0兑礼到店，1快递")
    private Integer exchangeType;

//    @ApiModelProperty(value = "适用门店列表")
//    private List<Long> shopIdList;

}
