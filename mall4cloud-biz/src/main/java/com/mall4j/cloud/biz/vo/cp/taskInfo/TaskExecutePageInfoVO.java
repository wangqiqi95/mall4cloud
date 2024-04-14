package com.mall4j.cloud.biz.vo.cp.taskInfo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class TaskExecutePageInfoVO {
    @ApiModelProperty("主键id")
    private Long id;
    @ApiModelProperty("员工姓名")
    private String shoppingGuideName;
    @ApiModelProperty("员工手机号")
    private String shoppingGuidePhone;
    @ApiModelProperty("状态 1完成 0未完成")
    private Integer status;

    @ApiModelProperty("需完成数量")
    private Integer clientSum;

    @ApiModelProperty("已完成数量")
    private Integer successClientSum;

}
