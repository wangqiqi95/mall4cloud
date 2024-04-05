package com.mall4j.cloud.distribution.dto;

import com.mall4j.cloud.distribution.model.DistributionDevelopingRewardStore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 分销推广-发展奖励DTO
 *
 * @author ZengFanChang
 * @date 2021-12-26 17:16:08
 */
@Data
public class DistributionDevelopingRewardDTO{

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("活动编号")
    private Integer numbering;

    @ApiModelProperty("选择门店类型 0全部门店 1部分门店")
    private Integer storeType;

    @ApiModelProperty("分销类型 0全部 1导购 2威客")
    private Integer distributionType;

    @ApiModelProperty("奖励金额")
    private Long rewardAmount;

    @ApiModelProperty("最多奖励人数")
    private Integer rewardMax;

    @ApiModelProperty("开始时间")
    private Date startTime;

    @ApiModelProperty("结束时间")
    private Date endTime;

    @ApiModelProperty("权重")
    private Integer index;

    @ApiModelProperty("状态 1启用 0禁用")
    private Integer status;

    @ApiModelProperty("导购数量")
    private Integer staffNum;

    @ApiModelProperty("威客数量")
    private Integer memberNum;

    @ApiModelProperty("累计奖励")
    private Long totalReward;

    @ApiModelProperty("查询门店ID")
    private Long queryStoreId;

    private List<Long> ids;

    @ApiModelProperty("适用门店列表")
    private List<DistributionDevelopingRewardStore> storeList;
}
