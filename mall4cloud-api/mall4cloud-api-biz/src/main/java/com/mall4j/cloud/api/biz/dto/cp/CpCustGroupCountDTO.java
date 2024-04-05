package com.mall4j.cloud.api.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CpCustGroupCountDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;

    @ApiModelProperty("筛选员工")
    private List<Long> staffIds;

}
