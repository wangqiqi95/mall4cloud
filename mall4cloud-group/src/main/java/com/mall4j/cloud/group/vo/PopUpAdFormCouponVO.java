package com.mall4j.cloud.group.vo;

import com.mall4j.cloud.group.vo.PopUpAdContainerVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class PopUpAdFormCouponVO extends PopUpAdAttachmentExtractionVO{

    private List<CouponDetail> couponDetailList;

    @Data
    public static class CouponDetail{
        @ApiModelProperty(value = "优惠券ID")
        private Long couponId;

        @ApiModelProperty(value = "优惠券名称")
        private String couponName;

        @ApiModelProperty(value = "优惠券封面URL")
        private String couponPicUrl;

        @ApiModelProperty(value = "折扣面额")
        private String symbol;

        @ApiModelProperty(value = "券类型名称")
        private String flag;
    }

}
