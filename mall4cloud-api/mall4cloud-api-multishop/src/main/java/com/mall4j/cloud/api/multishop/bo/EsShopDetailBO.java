package com.mall4j.cloud.api.multishop.bo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 店铺详情VO
 *
 * @author FrozenWatermelon
 * @date 2020-11-23 16:24:29
 */
public class EsShopDetailBO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty("店铺类型1自营店 2普通店")
    private Integer type;

    @ApiModelProperty("店铺名称")
    private String shopName;

    @ApiModelProperty("店铺logo")
    private String shopLogo;

    @ApiModelProperty("店铺状态(-1:已删除 0: 停业中 1:营业中 2:平台下线 3:待审核 4:店铺申请中 5:申请失败)")
    private Integer shopStatus;

	@ApiModelProperty("接收短信号码")
	private String noticeMobile;

	@ApiModelProperty("联系电话")
	private String contactPhone;

	@ApiModelProperty("店铺商品总销量")
	private Integer saleNum;

	@ApiModelProperty("用户总收藏量")
	private Integer collectionNum;

	@ApiModelProperty("签约起始时间")
	private Date contractStartTime;

	@ApiModelProperty("签约终止时间")
	private Date contractEndTime;

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public Date getContractStartTime() {
		return contractStartTime;
	}

	public void setContractStartTime(Date contractStartTime) {
		this.contractStartTime = contractStartTime;
	}

	public Date getContractEndTime() {
		return contractEndTime;
	}

	public void setContractEndTime(Date contractEndTime) {
		this.contractEndTime = contractEndTime;
	}

	public String getNoticeMobile() {
		return noticeMobile;
	}

	public void setNoticeMobile(String noticeMobile) {
		this.noticeMobile = noticeMobile;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getShopLogo() {
		return shopLogo;
	}

	public void setShopLogo(String shopLogo) {
		this.shopLogo = shopLogo;
	}

	public Integer getShopStatus() {
		return shopStatus;
	}

	public void setShopStatus(Integer shopStatus) {
		this.shopStatus = shopStatus;
	}

	public Integer getSaleNum() {
		return saleNum;
	}

	public void setSaleNum(Integer saleNum) {
		this.saleNum = saleNum;
	}

	public Integer getCollectionNum() {
		return collectionNum;
	}

	public void setCollectionNum(Integer collectionNum) {
		this.collectionNum = collectionNum;
	}

	@Override
	public String toString() {
		return "EsShopDetailBO{" +
				"shopId=" + shopId +
				", type=" + type +
				", shopName='" + shopName + '\'' +
				", shopLogo='" + shopLogo + '\'' +
				", shopStatus=" + shopStatus +
				", noticeMobile='" + noticeMobile + '\'' +
				", contactPhone='" + contactPhone + '\'' +
				", saleNum=" + saleNum +
				", collectionNum=" + collectionNum +
				", contractStartTime=" + contractStartTime +
				", contractEndTime=" + contractEndTime +
				'}';
	}
}
