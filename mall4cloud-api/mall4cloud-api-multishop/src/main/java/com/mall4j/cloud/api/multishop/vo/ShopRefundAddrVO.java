package com.mall4j.cloud.api.multishop.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

/**
 * 商家退货地址VO
 *
 * @author FrozenWatermelon
 * @date 2021-03-09 15:03:18
 */
public class ShopRefundAddrVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("商家退货地址id")
    private Long shopRefundAddrId;

    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty("手机")
    private String mobile;

    @ApiModelProperty("是否默认地址 1是")
    private Integer isDefault;

    @ApiModelProperty("收货人")
    private String consignee;

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

    @ApiModelProperty("邮编")
    private String postCode;

    @ApiModelProperty("地址")
    private String addr;

    @ApiModelProperty("经度")
    private Double lng;

    @ApiModelProperty("纬度")
    private Double lat;

	public Long getShopRefundAddrId() {
		return shopRefundAddrId;
	}

	public void setShopRefundAddrId(Long shopRefundAddrId) {
		this.shopRefundAddrId = shopRefundAddrId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
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

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	@Override
	public String toString() {
		return "ShopRefundAddrVO{" +
				"shopRefundAddrId=" + shopRefundAddrId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",shopId=" + shopId +
				",mobile=" + mobile +
				",isDefault=" + isDefault +
				",consignee=" + consignee +
				",provinceId=" + provinceId +
				",province=" + province +
				",cityId=" + cityId +
				",city=" + city +
				",areaId=" + areaId +
				",area=" + area +
				",postCode=" + postCode +
				",addr=" + addr +
				",lng=" + lng +
				",lat=" + lat +
				'}';
	}
}
