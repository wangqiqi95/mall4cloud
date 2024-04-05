package com.mall4j.cloud.biz.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 *
 * @author hlc
 * @date 2024/4/1 11:28
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TaskManagementVO extends BaseVO{
    private static final long serialVersionUID = 1L;


    @ApiModelProperty("任务id")
    private Long id;

    @ApiModelProperty("任务名称")
    private String taskName;

    @ApiModelProperty("任务类型  0加企微好友 1 分享素材 2回访客户")
    private Integer taskType;

    @ApiModelProperty("门店任务类型 0全部 1指定")
    private Integer storeSelectionType;

    @ApiModelProperty("门店数量")
    private Integer storeNum;

    @ApiModelProperty("导购任务范围类型 0全部 1指定")
    private Integer userRangeType;

    @ApiModelProperty("导购数量")
    private Integer userNum;

    @ApiModelProperty("所属公司类型 0麦吉利")
    private Integer affiliatedCompanyType;

    @ApiModelProperty("任务开始时间")
    private Date taskStartTime;

    @ApiModelProperty("任务结束时间")
    private Date taskEndTime;

    @ApiModelProperty("任务状态 0：未开始、1：进行中、2：已结束")
    private Integer status;



}
