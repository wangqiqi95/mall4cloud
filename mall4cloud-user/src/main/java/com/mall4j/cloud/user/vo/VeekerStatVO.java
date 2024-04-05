package com.mall4j.cloud.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class VeekerStatVO {

    @ApiModelProperty("累计微客")
    private Integer total;
    @ApiModelProperty("今日新增")
    private Integer today;
    @ApiModelProperty("本月新增")
    private Integer month;

}
