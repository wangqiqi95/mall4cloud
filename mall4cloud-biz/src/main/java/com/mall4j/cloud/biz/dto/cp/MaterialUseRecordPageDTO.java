package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MaterialUseRecordPageDTO {

    @ApiModelProperty("素材id")
    private Long matId;

    @ApiModelProperty("开始时间")
    private String  createTimeStart;

    @ApiModelProperty("结束时间")
    private String  createTimeEnd;

}
