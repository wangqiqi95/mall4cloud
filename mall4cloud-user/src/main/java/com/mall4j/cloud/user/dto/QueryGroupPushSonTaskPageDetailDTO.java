package com.mall4j.cloud.user.dto;


import com.mall4j.cloud.common.database.dto.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class QueryGroupPushSonTaskPageDetailDTO extends PageDTO {

    @ApiModelProperty(value = "子任务id")
    @NotNull(message = "子任务id")
    private Long sonTaskId;

    @ApiModelProperty(value = "任务模式：0-客户群发 1-群群发")
    @NotNull(message = "任务模式不可为空")
    private Integer taskMode;

    @ApiModelProperty("执行员工name")
    private String staffName;

    @ApiModelProperty(value = "完成开始时间")
    private Date startTime;

    @ApiModelProperty(value = "完成开始时间")
    private Date endTime;

    @ApiModelProperty("0未完成，1完成")
    private Integer finishState;

}
