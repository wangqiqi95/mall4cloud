package com.mall4j.cloud.order.dto.app;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class DistributionSalesStatDTO {

    @ApiModelProperty(value = "分销用户类型 1-导购 2-微客")
    private Integer distributionUserType;
    @ApiModelProperty(value = "分销用户ID(导购或微客id)")
    private Long distributionUserId;

    @ApiModelProperty("分销门店ID")
    private Long distributionStoreId;

    @ApiModelProperty("开始时间")
    private Date startTime;
    @ApiModelProperty("结束时间")
    private Date endTime;

    @ApiModelProperty("入账开始时间")
    private Date accountStartTime;
    @ApiModelProperty("入账结束时间")
    private Date accountEndTime;

    /**
     * 类型 1分销 2发展
     */
    private Integer type;

    /**
     * 退款中
     */
    private Boolean refund;

    /**
     * 是否已结算
     */
    private Boolean settlement;

    @ApiModelProperty("分销用户ID集合")
    private List<Long> distributionUserIds;

    @ApiModelProperty("下单会员ID集合")
    private List<Long> userIds;

    @ApiModelProperty("门店ID集合")
    private List<Long> storeIds;

    @ApiModelProperty("门店类型 1门店 2片区 3全部")
    private Integer storeType;

    /**
     * 分销关系 1分享关系 2服务关系 3自主下单 4代客下单
     */
    private Integer distributionRelation;

    @ApiModelProperty("订单ID集合")
    private List<Long> orderIds;

    /**
     * 导购ID集合
     */
    private List<Long> staffIds;


}
