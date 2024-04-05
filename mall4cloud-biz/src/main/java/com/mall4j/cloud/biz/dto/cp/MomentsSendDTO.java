package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MomentsSendDTO {

    @ApiModelProperty("朋友圈id")
    private Long momentsId;

    @ApiModelProperty("导购id")
    private Long staffId;

}
