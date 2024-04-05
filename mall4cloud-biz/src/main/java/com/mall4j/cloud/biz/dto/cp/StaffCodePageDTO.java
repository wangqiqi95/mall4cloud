package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 员工活码表DTO
 *
 * @author hwy
 * @date 2022-01-24 11:05:42
 */
@Data
public class StaffCodePageDTO implements Serializable {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty("创建人id")
    private String createName;

    @ApiModelProperty("开始时间")
    private String  createTimeStart;

    @ApiModelProperty("结束时间")
    private String  createTimeEnd;

    @ApiModelProperty("员工Id数组")
    private List<Long> staffIdList;

}
