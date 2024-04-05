package com.mall4j.cloud.seckill.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

/**
 * 秒杀活动skuVO
 *
 * @author lhd
 * @date 2021-03-30 14:53:09
 */
public class SeckillSkuVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("秒杀活动单个skuid")
    private Long seckillSkuId;

    @ApiModelProperty("skuId")
    private Long skuId;

    @ApiModelProperty("秒杀活动id")
    private Long seckillId;

    @ApiModelProperty("用于秒杀的库存")
    private Integer seckillStocks;

    @ApiModelProperty("秒杀价格")
    private Long seckillPrice;

	@ApiModelProperty("banner图片")
	private String imgUrl;

	@ApiModelProperty("sku名称")
	private String skuName;

	@ApiModelProperty("售价，整数方式保存")
	private Long priceFee;

	@ApiModelProperty("属性")
	private String properties;

	public String getProperties() {
		return properties;
	}

	public void setProperties(String properties) {
		this.properties = properties;
	}

	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}
	public Long getPriceFee() {
		return priceFee;
	}

	public void setPriceFee(Long priceFee) {
		this.priceFee = priceFee;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}



	public Long getSeckillSkuId() {
		return seckillSkuId;
	}

	public void setSeckillSkuId(Long seckillSkuId) {
		this.seckillSkuId = seckillSkuId;
	}

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	public Long getSeckillId() {
		return seckillId;
	}

	public void setSeckillId(Long seckillId) {
		this.seckillId = seckillId;
	}

	public Integer getSeckillStocks() {
		return seckillStocks;
	}

	public void setSeckillStocks(Integer seckillStocks) {
		this.seckillStocks = seckillStocks;
	}

	public Long getSeckillPrice() {
		return seckillPrice;
	}

	public void setSeckillPrice(Long seckillPrice) {
		this.seckillPrice = seckillPrice;
	}

	@Override
	public String toString() {
		return "SeckillSkuVO{" +
				"seckillSkuId=" + seckillSkuId +
				", skuId=" + skuId +
				", seckillId=" + seckillId +
				", seckillStocks=" + seckillStocks +
				", seckillPrice=" + seckillPrice +
				", imgUrl='" + imgUrl + '\'' +
				", skuName='" + skuName + '\'' +
				", priceFee=" + priceFee +
				", properties='" + properties + '\'' +
				'}';
	}
}
