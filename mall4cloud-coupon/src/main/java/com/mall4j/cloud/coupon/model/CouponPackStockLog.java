package com.mall4j.cloud.coupon.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "调整券包库存记录")
@TableName(value = "coupon_pack_stock_log")
public class CouponPackStockLog implements Serializable {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("记录id")
    private Integer id;
    @ApiModelProperty("活动id")
    private Integer activityId;
    @ApiModelProperty("调整库存")
    private Integer optStock;
    @ApiModelProperty("调整时间")
    private Date optTime;
    @ApiModelProperty("调整员工姓名")
    private String optUserName;
    @ApiModelProperty("员工工号")
    private String optUserNo;
}
