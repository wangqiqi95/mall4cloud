package com.mall4j.cloud.api.multishop.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mall4j.cloud.common.serializer.ImgJsonSerializer;
import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 店铺详情VO
 *
 * @author FrozenWatermelon
 * @date 2020-12-05 15:50:25
 */
public class ShopDetailVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty("店铺类型1自营店 2普通店")
    private Integer type;

    @ApiModelProperty("店铺名称")
    private String shopName;

    @ApiModelProperty("店铺简介")
    private String intro;

    @ApiModelProperty("接收短信号码")
    private String noticeMobile;

    @ApiModelProperty("店铺logo(可修改)")
    private String shopLogo;

    @ApiModelProperty("店铺状态(-1:已删除 0: 停业中 1:营业中 2:平台下线 3:开店申请待审核 4:店铺申请中 5:上线申请待审核)")
    private Integer shopStatus;

	@ApiModelProperty("是否优选好店 1.是 0.不是")
	private Integer isPreferred;

	@ApiModelProperty("店铺收藏数量")
	private Long collectionNum;

	@ApiModelProperty("移动端背景图")
	private String mobileBackgroundPic;

	@ApiModelProperty("pc背景图")
	private String pcBackgroundPic;

	@ApiModelProperty("联系人姓名")
	private String contactName;

	@ApiModelProperty("联系方式")
	private String contactPhone;

	@ApiModelProperty("详细地址")
	private String detailAddress;

	@ApiModelProperty("邮箱")
	private String email;

	@ApiModelProperty("商家账号")
	private String merchantAccount;

	@ApiModelProperty("账号状态， 1:启用 0:禁用 -1:删除")
	private Integer accountStatus;

	@ApiModelProperty("商家名称")
	private String merchantName;

	@ApiModelProperty("省ID")
	private Long provinceId;

	@ApiModelProperty("省")
	private String province;

	@ApiModelProperty("城市ID")
	private Long cityId;

	@ApiModelProperty("城市")
	private String city;

	@ApiModelProperty("区ID")
	private Long areaId;

	@ApiModelProperty("区")
	private String area;

	@ApiModelProperty("签约起始时间")
	private Date contractStartTime;

	@ApiModelProperty("签约终止时间")
	private Date contractEndTime;

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

	public String getMerchantAccount() {
		return merchantAccount;
	}

	public void setMerchantAccount(String merchantAccount) {
		this.merchantAccount = merchantAccount;
	}

	public Integer getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(Integer accountStatus) {
		this.accountStatus = accountStatus;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public Long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getMobileBackgroundPic() {
		return mobileBackgroundPic;
	}

	public void setMobileBackgroundPic(String mobileBackgroundPic) {
		this.mobileBackgroundPic = mobileBackgroundPic;
	}

	public String getPcBackgroundPic() {
		return pcBackgroundPic;
	}

	public void setPcBackgroundPic(String pcBackgroundPic) {
		this.pcBackgroundPic = pcBackgroundPic;
	}

	public Long getCollectionNum() {
		return collectionNum;
	}

	public void setCollectionNum(Long collectionNum) {
		this.collectionNum = collectionNum;
	}

	public Integer getIsPreferred() {
		return isPreferred;
	}

	public void setIsPreferred(Integer isPreferred) {
		this.isPreferred = isPreferred;
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

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getNoticeMobile() {
		return noticeMobile;
	}

	public void setNoticeMobile(String noticeMobile) {
		this.noticeMobile = noticeMobile;
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

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getDetailAddress() {
		return detailAddress;
	}

	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	@Override
	public String toString() {
		return "ShopDetailVO{" +
				"shopId=" + shopId +
				", type=" + type +
				", shopName='" + shopName + '\'' +
				", intro='" + intro + '\'' +
				", noticeMobile='" + noticeMobile + '\'' +
				", shopLogo='" + shopLogo + '\'' +
				", shopStatus=" + shopStatus +
				", isPreferred=" + isPreferred +
				", collectionNum=" + collectionNum +
				", mobileBackgroundPic='" + mobileBackgroundPic + '\'' +
				", pcBackgroundPic='" + pcBackgroundPic + '\'' +
				", contactName='" + contactName + '\'' +
				", contactPhone='" + contactPhone + '\'' +
				", detailAddress='" + detailAddress + '\'' +
				", email='" + email + '\'' +
				", merchantAccount='" + merchantAccount + '\'' +
				", accountStatus=" + accountStatus +
				", merchantName='" + merchantName + '\'' +
				", provinceId=" + provinceId +
				", province='" + province + '\'' +
				", cityId=" + cityId +
				", city='" + city + '\'' +
				", areaId=" + areaId +
				", area='" + area + '\'' +
				", contractStartTime=" + contractStartTime +
				", contractEndTime=" + contractEndTime +
				'}';
	}
}
