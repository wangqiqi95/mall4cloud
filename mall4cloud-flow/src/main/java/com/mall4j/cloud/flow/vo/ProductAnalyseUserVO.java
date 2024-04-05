package com.mall4j.cloud.flow.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 流量分析—页面数据统计表VO
 *
 * @author YXF
 * @date 2021-05-21 15:25:19
 */
public class ProductAnalyseUserVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long productAnalyseUserId;

    @ApiModelProperty("页面分析id")
    private Long productAnalyseId;

    @ApiModelProperty("uuid")
    private String uuid;

    @ApiModelProperty("创建日期")
    private Date createDate;

    @ApiModelProperty("商品id")
    private String spuId;

    @ApiModelProperty("访问")
    private Integer isVisit;

    @ApiModelProperty("页面点击")
    private Integer isClick;

    @ApiModelProperty("分享访问")
    private Integer isShareVisit;

    @ApiModelProperty("下单")
    private Integer isPlaceOrder;

    @ApiModelProperty("支付")
    private Integer isPay;

    @ApiModelProperty("加购")
    private Integer isPlusShopCart;

	public Long getProductAnalyseUserId() {
		return productAnalyseUserId;
	}

	public void setProductAnalyseUserId(Long productAnalyseUserId) {
		this.productAnalyseUserId = productAnalyseUserId;
	}

	public Long getProductAnalyseId() {
		return productAnalyseId;
	}

	public void setProductAnalyseId(Long productAnalyseId) {
		this.productAnalyseId = productAnalyseId;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getSpuId() {
		return spuId;
	}

	public void setSpuId(String spuId) {
		this.spuId = spuId;
	}

	public Integer getIsVisit() {
		return isVisit;
	}

	public void setIsVisit(Integer isVisit) {
		this.isVisit = isVisit;
	}

	public Integer getIsClick() {
		return isClick;
	}

	public void setIsClick(Integer isClick) {
		this.isClick = isClick;
	}

	public Integer getIsShareVisit() {
		return isShareVisit;
	}

	public void setIsShareVisit(Integer isShareVisit) {
		this.isShareVisit = isShareVisit;
	}

	public Integer getIsPlaceOrder() {
		return isPlaceOrder;
	}

	public void setIsPlaceOrder(Integer isPlaceOrder) {
		this.isPlaceOrder = isPlaceOrder;
	}

	public Integer getIsPay() {
		return isPay;
	}

	public void setIsPay(Integer isPay) {
		this.isPay = isPay;
	}

	public Integer getIsPlusShopCart() {
		return isPlusShopCart;
	}

	public void setIsPlusShopCart(Integer isPlusShopCart) {
		this.isPlusShopCart = isPlusShopCart;
	}

	@Override
	public String toString() {
		return "ProductAnalyseUserVO{" +
				"productAnalyseUserId=" + productAnalyseUserId +
				",productAnalyseId=" + productAnalyseId +
				",uuid=" + uuid +
				",createDate=" + createDate +
				",spuId=" + spuId +
				",isVisit=" + isVisit +
				",isClick=" + isClick +
				",isShareVisit=" + isShareVisit +
				",isPlaceOrder=" + isPlaceOrder +
				",isPay=" + isPay +
				",isPlusShopCart=" + isPlusShopCart +
				'}';
	}
}
