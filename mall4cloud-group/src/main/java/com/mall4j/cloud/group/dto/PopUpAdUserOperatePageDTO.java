package com.mall4j.cloud.group.dto;

import com.mall4j.cloud.common.database.dto.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PopUpAdUserOperatePageDTO extends PageDTO {

    @ApiModelProperty(value = "广告ID")
    private Long adId;
}
