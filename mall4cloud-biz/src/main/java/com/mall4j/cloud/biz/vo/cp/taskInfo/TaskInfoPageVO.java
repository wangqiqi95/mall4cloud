package com.mall4j.cloud.biz.vo.cp.taskInfo;

import com.mall4j.cloud.biz.dto.TaskClientInfoDTO;
import com.mall4j.cloud.biz.dto.TaskRemindInfoDTO;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskInfoPageVO {
    @ApiModelProperty("任务id")
    private Long id;
    @ApiModelProperty("任务名称")
    private String taskName;
    @ApiModelProperty("企业id")
    private String companyId;
    @ApiModelProperty("门店数量")
    private Integer storeNum;
    @ApiModelProperty("任务导购类型 1全部导购 2指定导购。指定导购时使用shoppingGuideNums字段拼接")
    private Integer taskShoppingGuideType;
    @ApiModelProperty("任务门店类型 1全部门店 2指定门店。指定门店时使用storeNum字段拼接")
    private Integer taskStoreType;
    @ApiModelProperty("导购数量")
    private Integer shoppingGuideNum;
    @ApiModelProperty("任务状态")
    private Integer taskStatus;
    @ApiModelProperty("任务开始时间")
    private Date startTime;
    @ApiModelProperty("任务结束时间")
    private Date endTime;

}
