package com.mall4j.cloud.api.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CountNotMemberUsersVO {
    @ApiModelProperty("导购staffId")
    private Long staffId;
    @ApiModelProperty("数量")
    private Integer nums;
}
