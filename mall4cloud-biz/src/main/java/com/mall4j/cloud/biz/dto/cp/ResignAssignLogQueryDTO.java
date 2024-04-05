package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 离职分配日志表DTO
 *
 * @author hwy
 * @date 2022-01-24 11:05:43
 */
@Data
public class ResignAssignLogQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("操作人")
    private String createName;

    @ApiModelProperty("操作开始时间")
    private String createTimeStart;

    @ApiModelProperty("操作开始时间")
    private String createTimeEnd;

    @ApiModelProperty("分配类型")
    private Integer assignType;

    @ApiModelProperty("操作人门店列表")
    private List<Long> storeIds;

}
