package com.mall4j.cloud.order.dto.app;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class DistributionOrderSearchDTO {

    @ApiModelProperty("商品名称")
    private String spuName;

    @ApiModelProperty("分销佣金状态 0-待结算 1-已结算待提现 2-已结算已提现")
    private List<Integer> distributionStatusList;

    @ApiModelProperty("下单的时间范围:开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty("下单的时间范围:结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty("分销佣金范围:min")
    private Long distributionAmountMin;

    @ApiModelProperty("分销佣金范围:max")
    private Long distributionAmountMax;

    @ApiModelProperty("分销关系 1分享关系 2服务关系 3自主下单 4代客下单")
    private Integer distributionRelation;

    @ApiModelProperty("分销关系 1分享关系 2服务关系 3自主下单 4代客下单")
    private List<Integer> distributionRelationList;

    /**
     * 分销用户类型 1-导购 2-微客
     */
    private Integer distributionUserType;
    /**
     * 分销用户ID(导购或微客id)
     */
    private Long distributionUserId;

}
