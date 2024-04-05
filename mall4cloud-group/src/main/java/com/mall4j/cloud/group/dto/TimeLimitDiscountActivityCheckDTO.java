package com.mall4j.cloud.group.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @luzhengxiang
 * @create 2022-03-10 4:58 PM
 **/
@Data
public class TimeLimitDiscountActivityCheckDTO implements Serializable {

    @ApiModelProperty("")
    private Integer id;

    @ApiModelProperty("审核状态：0待审核 1审核通过 2驳回")
    private Integer checkStatus;

    @ApiModelProperty("忽略不提交")
    private String checkBy;
}