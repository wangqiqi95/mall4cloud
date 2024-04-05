package com.mall4j.cloud.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GroupPushTaskRecordStatisticVO {

    @ApiModelProperty("数量")
    private Integer count;
    @ApiModelProperty("日期")
    private String date;

}
