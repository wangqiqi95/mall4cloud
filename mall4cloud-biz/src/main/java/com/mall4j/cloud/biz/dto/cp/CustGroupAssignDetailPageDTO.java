package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 客群分配明细表 DTO
 *
 * @author hwy
 * @date 2022-02-10 18:25:57
 */
@Data
public class CustGroupAssignDetailPageDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("日志id")
    private Long resignId;

    @ApiModelProperty("客户姓名/群名")
    private String name;

    @ApiModelProperty("分配状态")
    private Integer status;

    @ApiModelProperty("接替门店的id")
    private List<Long> storeIds;

    @ApiModelProperty("接替员工姓名")
    private String replaceByName;

    @ApiModelProperty("客群或者客户分配tab值   0 客户分配  1 客群分配")
    private Integer type;

    private String custGroupId;


}
