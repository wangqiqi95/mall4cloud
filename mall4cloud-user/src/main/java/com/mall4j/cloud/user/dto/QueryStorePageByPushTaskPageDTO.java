package com.mall4j.cloud.user.dto;


import com.mall4j.cloud.common.database.dto.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class QueryStorePageByPushTaskPageDTO extends PageDTO {


    @NotNull(message = "群发任务ID为必传项")
    @ApiModelProperty(value = "群发任务ID")
    private Long pushTaskId;

    @ApiModelProperty(value = "店铺关键字（名称/编号）")
    private String storeKeyword;

}
