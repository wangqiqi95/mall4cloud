package com.mall4j.cloud.api.user.vo.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class AnalyzeUserStaffCpRelationDetailVO {

    @ApiModelProperty("折线图数据")
    private List<AnalyzeUserStaffCpRelationVO> relationVOS;

    @ApiModelProperty("总和")
    private Integer count;
}
