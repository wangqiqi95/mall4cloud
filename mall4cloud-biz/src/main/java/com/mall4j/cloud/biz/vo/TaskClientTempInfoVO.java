package com.mall4j.cloud.biz.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TaskClientTempInfoVO {
    /**
     * 临时id，用于新增时匹配
     */
    @ApiModelProperty("临时id，用于新增时匹配")
    private String tempUuid;
    /**
     * 客户昵称
     */
    @ApiModelProperty("客户昵称")
    private String clientNickname;
    /**
     * 客户电话
     */
    @ApiModelProperty("客户电话")
    private String clientPhone;
}
