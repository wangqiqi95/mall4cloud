package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 群发任务明细表DTO
 *
 * @author hwy
 * @date 2022-02-18 18:17:52
 */
@Data
public class TaskSendDetailDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("推送id")
    @NotNull(message = "群发id不能为空")
    private Long pushId;

    @ApiModelProperty("员工姓名")
    private String staffName;

    @ApiModelProperty("商店列表")
    private List<Long> storeIds;

    @ApiModelProperty("是否转发")
    private Integer isRelay;

    @ApiModelProperty("完成时间开始")
    private String completeTimeStart;

    @ApiModelProperty("完成时间结束")
    private String completeTimeEnd;


}
