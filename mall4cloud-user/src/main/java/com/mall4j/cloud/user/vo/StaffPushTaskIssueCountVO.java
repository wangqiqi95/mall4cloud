package com.mall4j.cloud.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StaffPushTaskIssueCountVO {

    @ApiModelProperty("推送任务个数")
    private Integer issueCount;

    @ApiModelProperty("导购ID")
    private Long staffId;

}
