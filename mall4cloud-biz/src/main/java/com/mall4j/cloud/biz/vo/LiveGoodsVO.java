package com.mall4j.cloud.biz.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * VO
 *
 * @author chaoge
 * @date 2022-03-05
 */
@Data
@ApiModel("直播商品")
public class LiveGoodsVO{

    @ApiModelProperty("商品ID")
    private Long goodsId;

    @ApiModelProperty("商品封面")
    private String coverPic;

    @ApiModelProperty("商品名称")
    private String name;

	@ApiModelProperty("价格类型，1：一口价（只需要传入price，price2不传） 2：价格区间（price字段为左边界，price2字段为右边界，price和price2必传） 3：显示折扣价（price字段为原价，price2字段为现价， price和price2必传）")
	private Integer priceType;

	@ApiModelProperty("商品价格(元)")
	private BigDecimal price;

	@ApiModelProperty("商品价格(元)")
	private BigDecimal price2;

	@ApiModelProperty("商品详情页的小程序路径")
	private String url;

	@ApiModelProperty("商品详情页的小程序路径")
	private String anchorWechat;

	@ApiModelProperty("0：未审核，1：审核中，2:审核通过，3审核失败")
	private Integer status;

	@ApiModelProperty("提交审核时间")
	private Date verifyTime;

	@ApiModelProperty("添加时间")
	private Date createTime;

	@ApiModelProperty("更新时间")
	private Date updateTime;
}
