package com.mall4j.cloud.api.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 联营分佣查询dto
 *
 * @author Zhang Fan
 * @date 2022/8/12 16:52
 */
@ApiModel("联营分佣查询dto")
@Data
public class DistributionJointVentureOrderSearchDTO {

    @ApiModelProperty("联营分佣状态 联营分佣状态 0-待分佣 1-完成分佣")
    private Integer jointVentureCommissionStatus;

    @ApiModelProperty("联营客户id")
    @NotNull(message = "联营分佣客户id不能为空")
    private Long jointVentureCommissionCustomerId;

    @ApiModelProperty("支付的时间范围:开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "支付开始时间不能为空")
    private Date payStartTime;

    @ApiModelProperty("支付的时间范围:结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "支付结束时间不能为空")
    private Date payEndTime;

    @ApiModelProperty("门店id列表")
    private List<Long> storeIdList;

    @ApiModelProperty("开始页")
    private Integer pageNum;

    @ApiModelProperty("每页大小")
    private Integer pageSize;

    @ApiModelProperty("订单id集合")
    private List<Long> selectedOrderIdList;

    @Override
    public String toString() {
        return "DistributionJointVentureOrderSearchDTO{" +
                "jointVentureCommissionStatus=" + jointVentureCommissionStatus +
                ", jointVentureCommissionCustomerId=" + jointVentureCommissionCustomerId +
                ", payStartTime=" + payStartTime +
                ", payEndTime=" + payEndTime +
                ", storeIdList=" + storeIdList +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }
}
