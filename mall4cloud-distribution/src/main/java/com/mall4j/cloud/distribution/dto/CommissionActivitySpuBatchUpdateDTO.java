package com.mall4j.cloud.distribution.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class CommissionActivitySpuBatchUpdateDTO {

    @ApiModelProperty("活动ID")
    @NotNull(message = "活动ID不能为空")
    private Long activityId;

    @ApiModelProperty("Id集合")
    @NotNull(message = "Id集合不能为空")
    private List<Long> idList;

    @ApiModelProperty("活动开始时间")
    private Date startTime;

    @ApiModelProperty("活动结束时间")
    private Date endTime;

    @ApiModelProperty("导购佣金状态 0-禁用 1-启用")
    private Integer guideRateStatus;

    @ApiModelProperty("导购佣金")
    private BigDecimal guideRate;

    @ApiModelProperty("微客佣金状态 0-禁用 1-启用")
    private Integer shareRateStatus;

    @ApiModelProperty("微客佣金")
    private BigDecimal shareRate;

    @ApiModelProperty("发展佣金状态 0-禁用 1-启用")
    private Integer developRateStatus;

    @ApiModelProperty("发展佣金")
    private BigDecimal developRate;

}
