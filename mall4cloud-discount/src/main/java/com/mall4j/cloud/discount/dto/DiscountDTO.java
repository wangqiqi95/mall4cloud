package com.mall4j.cloud.discount.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 满减满折优惠DTO
 *
 * @author lhd
 * @date 2020-12-10 13:43:38
 */
@Data
public class DiscountDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("满减满折优惠id")
    private Long discountId;

    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty("活动名称")
	@NotNull(message = "活动名称不能为空")
    private String discountName;

    @ApiModelProperty("枚举DiscountRule(0 满钱减钱 3满件打折)")
    private Integer discountRule;

    @ApiModelProperty("减免类型 0按满足最高层级减一次 1每满一次减一次")
    private Integer discountType;

    @ApiModelProperty("适用商品类型 0全部商品参与 1指定商品参与")
	@Max(value = 1, message = "适用商品类型错误")
	@Min(value = 0, message = "适用商品类型错误")
    private Integer suitableSpuType;

    @Max(999999999)
    @ApiModelProperty("最多减多少")
    private Long maxReduceAmount;

    @ApiModelProperty("开始时间")
    private Date startTime;

    @ApiModelProperty("结束时间")
    private Date endTime;

    @ApiModelProperty("活动状态:(0:关闭、1:开启、2:违规下线、3:平台审核)")
	@Min(value = 0, message = "活动添加或改变后的状态异常")
	@Max(value = 3, message = "活动添加或改变后的状态异常")
    private Integer status;

    @ApiModelProperty("手机端活动图片")
    private String mobilePic;

    @ApiModelProperty("pc端活动列表图片")
    private String pcPic;

    @ApiModelProperty("pc端活动背景图片")
    private String pcBackgroundPic;

	@ApiModelProperty("商品Id列表")
	private List<Long> spuIds;

	@ApiModelProperty("活动项")
	private List<DiscountItemDTO> discountItemList;

    @ApiModelProperty("门店列表")
    private List<Long> shopIds;

    @ApiModelProperty("是否全部门店 0 否 1是")
    private Integer isAllShop;

}
