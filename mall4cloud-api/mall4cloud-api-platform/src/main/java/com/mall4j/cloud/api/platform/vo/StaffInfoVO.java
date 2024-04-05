package com.mall4j.cloud.api.platform.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class StaffInfoVO {

    @ApiModelProperty("员工基础信息对象")
    private StaffVO staffVO;

    @ApiModelProperty("员工门店标签")
    private List<TzTagDetailVO> tagDetailVOList;

    @ApiModelProperty("员工门店标签 所包含的可用门店（去重了的）")
    private List<Long> tagStoreIds;


}
