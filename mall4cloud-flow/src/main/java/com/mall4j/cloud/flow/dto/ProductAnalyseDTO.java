package com.mall4j.cloud.flow.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 流量分析—商品分析DTO
 *
 * @author YXF
 * @date 2021-05-21 15:25:19
 */
public class ProductAnalyseDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long productAnalyseId;

    @ApiModelProperty("创建日期")
    private Date createDate;

    @ApiModelProperty("页面编号")
    private Long pageId;

    @ApiModelProperty("系统类型")
    private Integer systemType;

    @ApiModelProperty("商品id")
    private Long spuId;

    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty("访问次数")
    private Long visis;

    @ApiModelProperty("点击数")
    private Long click;

    @ApiModelProperty("分享访问次数")
    private Long shareVisit;

    @ApiModelProperty("下单金额")
    private Double placeOrderAmount;

    @ApiModelProperty("支付金额")
    private Double payAmount;

    @ApiModelProperty("加购数量")
    private Integer plusShopCart;

	public Long getProductAnalyseId() {
		return productAnalyseId;
	}

	public void setProductAnalyseId(Long productAnalyseId) {
		this.productAnalyseId = productAnalyseId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Long getPageId() {
		return pageId;
	}

	public void setPageId(Long pageId) {
		this.pageId = pageId;
	}

	public Integer getSystemType() {
		return systemType;
	}

	public void setSystemType(Integer systemType) {
		this.systemType = systemType;
	}

	public Long getSpuId() {
		return spuId;
	}

	public void setSpuId(Long spuId) {
		this.spuId = spuId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Long getVisis() {
		return visis;
	}

	public void setVisis(Long visis) {
		this.visis = visis;
	}

	public Long getClick() {
		return click;
	}

	public void setClick(Long click) {
		this.click = click;
	}

	public Long getShareVisit() {
		return shareVisit;
	}

	public void setShareVisit(Long shareVisit) {
		this.shareVisit = shareVisit;
	}

	public Double getPlaceOrderAmount() {
		return placeOrderAmount;
	}

	public void setPlaceOrderAmount(Double placeOrderAmount) {
		this.placeOrderAmount = placeOrderAmount;
	}

	public Double getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(Double payAmount) {
		this.payAmount = payAmount;
	}

	public Integer getPlusShopCart() {
		return plusShopCart;
	}

	public void setPlusShopCart(Integer plusShopCart) {
		this.plusShopCart = plusShopCart;
	}

	@Override
	public String toString() {
		return "ProductAnalyseDTO{" +
				"productAnalyseId=" + productAnalyseId +
				",createDate=" + createDate +
				",pageId=" + pageId +
				",systemType=" + systemType +
				",spuId=" + spuId +
				",shopId=" + shopId +
				",visis=" + visis +
				",click=" + click +
				",shareVisit=" + shareVisit +
				",placeOrderAmount=" + placeOrderAmount +
				",payAmount=" + payAmount +
				",plusShopCart=" + plusShopCart +
				'}';
	}
}
