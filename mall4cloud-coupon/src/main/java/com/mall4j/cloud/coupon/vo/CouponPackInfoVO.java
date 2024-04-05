package com.mall4j.cloud.coupon.vo;

import com.mall4j.cloud.api.coupon.vo.CouponListVO;
import com.mall4j.cloud.coupon.model.CouponPackShop;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("券包活动信息小程序实体")
public class CouponPackInfoVO implements Serializable {
    @ApiModelProperty("券包活动id")
    private Integer id;
    @ApiModelProperty("活动名称")
    private String activityName;
    @ApiModelProperty("活动banner")
    private String  activityBannerUrl;
    @ApiModelProperty("活动开始时间")
    private Date activityBeginTime;
    @ApiModelProperty("活动截止时间")
    private Date activityEndTime;
    @ApiModelProperty("是否全部门店")
    private Integer isAllShop;
    @ApiModelProperty("活动状态 0 禁用 1启用 2进行中 3 未开始 4已结束 ")
    private Integer status;
    @ApiModelProperty("描述")
    private String description;
    @ApiModelProperty("优惠券信息集合")
    private List<CouponListVO> coupons;
    @ApiModelProperty("适用门店集合")
    private List<CouponPackShop> shops;
}
