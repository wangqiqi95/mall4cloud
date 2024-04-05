package com.mall4j.cloud.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StaffPushTaskNotAddFriendCountVO {

    @ApiModelProperty("未加好友个数")
    private Integer notAddCount;

    @ApiModelProperty("导购ID")
    private Long staffId;

}
