package com.mall4j.cloud.group.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

/**
 * 拼团活动商品规格VO
 *
 * @author YXF
 * @date 2021-03-20 10:39:32
 */
public class GroupSkuVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("拼团活动商品规格id")
    private Long groupSkuId;

    @ApiModelProperty("拼团活动id")
    private Long groupActivityId;

    @ApiModelProperty("商品规格id")
    private Long skuId;

    @ApiModelProperty("活动价格")
    private Long actPrice;

    @ApiModelProperty("商品sku价格")
    private Long price;

    @ApiModelProperty("已售数量")
    private Long sellNum;

    @ApiModelProperty("sku名称")
    private String skuName;

	@ApiModelProperty("前端显示 barCode")
	private String skuCode;

	public Long getGroupSkuId() {
		return groupSkuId;
	}

	public void setGroupSkuId(Long groupSkuId) {
		this.groupSkuId = groupSkuId;
	}

	public Long getGroupActivityId() {
		return groupActivityId;
	}

	public void setGroupActivityId(Long groupActivityId) {
		this.groupActivityId = groupActivityId;
	}

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	public Long getActPrice() {
		return actPrice;
	}

	public void setActPrice(Long actPrice) {
		this.actPrice = actPrice;
	}

	public Long getSellNum() {
		return sellNum;
	}

	public void setSellNum(Long sellNum) {
		this.sellNum = sellNum;
	}

	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}


	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	@Override
	public String toString() {
		return "GroupSkuVO{" +
				"groupSkuId=" + groupSkuId +
				",groupActivityId=" + groupActivityId +
				",skuId=" + skuId +
				",actPrice=" + actPrice +
				",price=" + price +
				",sellNum=" + sellNum +
				",skuName=" + skuName +
				'}';
	}
}
