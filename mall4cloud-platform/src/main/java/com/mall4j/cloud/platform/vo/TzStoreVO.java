package com.mall4j.cloud.platform.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * VO
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 10:46:32
 */
public class TzStoreVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("门店id")
    private Long storeId;

    @ApiModelProperty("门店id")
    private Long orgId;

    @ApiModelProperty("门店名称")
    private String stationName;

    @ApiModelProperty("门店图片")
    private String pic;

    @ApiModelProperty("电话区号")
    private String phonePrefix;

    @ApiModelProperty("手机/电话号码")
    private String phone;

    @ApiModelProperty("0:关闭 1:营业 2:强制关闭 3:审核中 4:审核失败")
    private Integer status;

	@ApiModelProperty("门店类型1-自营，2-经销，3-代销，4-电商，5-其他")
	private Integer type;

	@ApiModelProperty("省id")
    private Long provinceId;

    @ApiModelProperty("省")
    private String province;

    @ApiModelProperty("市id")
    private Long cityId;

    @ApiModelProperty("市")
    private String city;

    @ApiModelProperty("区id")
    private Long areaId;

    @ApiModelProperty("区")
    private String area;

    @ApiModelProperty("地址")
    private String addr;

    @ApiModelProperty("经度")
    private Double lng;

    @ApiModelProperty("纬度")
    private Double lat;

    @ApiModelProperty("第三方门店编码")
    private String storeCode;

    @ApiModelProperty("是否支持自提 -1-支持 0-不支持")
    private Integer isSelf;

    @ApiModelProperty("是否支持配送 1-支持 0 -不支持")
    private Integer isDelivery;

    @ApiModelProperty("0-正常 1-新店")
    private Integer isNew;

    @ApiModelProperty("是否支持外卖 0-不支持 1-支持")
    private Integer isTakeOut;

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getPhonePrefix() {
		return phonePrefix;
	}

	public void setPhonePrefix(String phonePrefix) {
		this.phonePrefix = phonePrefix;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public String getStoreCode() {
		return storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}

	public Integer getIsSelf() {
		return isSelf;
	}

	public void setIsSelf(Integer isSelf) {
		this.isSelf = isSelf;
	}

	public Integer getIsDelivery() {
		return isDelivery;
	}

	public void setIsDelivery(Integer isDelivery) {
		this.isDelivery = isDelivery;
	}

	public Integer getIsNew() {
		return isNew;
	}

	public void setIsNew(Integer isNew) {
		this.isNew = isNew;
	}

	public Integer getIsTakeOut() {
		return isTakeOut;
	}

	public void setIsTakeOut(Integer isTakeOut) {
		this.isTakeOut = isTakeOut;
	}

	@Override
	public String toString() {
		return "TzStoreVO{" +
				"storeId=" + storeId +
				",orgId=" + orgId +
				",stationName=" + stationName +
				",pic=" + pic +
				",phonePrefix=" + phonePrefix +
				",phone=" + phone +
				",status=" + status +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",provinceId=" + provinceId +
				",province=" + province +
				",cityId=" + cityId +
				",city=" + city +
				",areaId=" + areaId +
				",area=" + area +
				",addr=" + addr +
				",lng=" + lng +
				",lat=" + lat +
				",storeCode=" + storeCode +
				",isSelf=" + isSelf +
				",isDelivery=" + isDelivery +
				",isNew=" + isNew +
				",isTakeOut=" + isTakeOut +
				'}';
	}
}
