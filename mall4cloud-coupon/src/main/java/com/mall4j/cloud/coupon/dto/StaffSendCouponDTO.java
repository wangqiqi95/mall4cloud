package com.mall4j.cloud.coupon.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 导购发券 DTO
 * @author gww
 * @date 2022-01-23
 */
@Data
public class StaffSendCouponDTO {

    @NotNull(message = "活动id不能为空")
    @ApiModelProperty("活动id")
    private Long activityId;

    @NotNull(message = "优惠券id不能为空")
    @ApiModelProperty("优惠券id")
    private Long couponId;

    @ApiModelProperty("导购id")
    private Long staffId;
    private String staffName;
    private String staffPhone;

    @ApiModelProperty("門店id")
    private Long shopId;

    @ApiModelProperty("触点号")
    String tentacleNo;
}
