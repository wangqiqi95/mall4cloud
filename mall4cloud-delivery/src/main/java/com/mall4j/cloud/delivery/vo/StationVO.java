package com.mall4j.cloud.delivery.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

/**
 * 自提点信息VO
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:01
 */
public class StationVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("自提点id")
    private Long stationId;

    @ApiModelProperty("关联店铺id")
    private Long shopId;

    @ApiModelProperty("自提点名称")
    private String stationName;

    @ApiModelProperty("自提点图片")
    private String imgUrl;

    @ApiModelProperty("电话区号")
    private String mobilePrefix;

    @ApiModelProperty("手机/电话号码")
    private String mobile;

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

    @ApiModelProperty("时间数据")
    private String timeInfo;

    @ApiModelProperty("0:关闭 1:营业 2:强制关闭 3:审核中 4:审核失败")
    private Integer status;

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getMobilePrefix() {
		return mobilePrefix;
	}

	public void setMobilePrefix(String mobilePrefix) {
		this.mobilePrefix = mobilePrefix;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
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

	public String getTimeInfo() {
		return timeInfo;
	}

	public void setTimeInfo(String timeInfo) {
		this.timeInfo = timeInfo;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "StationVO{" +
				"stationId=" + stationId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",shopId=" + shopId +
				",stationName=" + stationName +
				",imgUrl=" + imgUrl +
				",mobilePrefix=" + mobilePrefix +
				",mobile=" + mobile +
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
				",timeInfo=" + timeInfo +
				",status=" + status +
				'}';
	}
}
