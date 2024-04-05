package com.mall4j.cloud.distribution.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 门店活动
 *
 * @author gww
 * @date 2021-12-26 21:17:59
 */
public class DistributionStoreActivity extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

	@ApiModelProperty("主键ID")
    private Long id;

	@ApiModelProperty("活动名称")
    private String name;

	@ApiModelProperty("宣传图片")
    private String img;

	@ApiModelProperty("组织ID")
    private Long orgId;

	@ApiModelProperty("状态 0-禁用 1-启用")
    private Integer status;

	@ApiModelProperty("限定报名人数")
    private Integer limitApplyNum;

	@ApiModelProperty("已报名人数")
	private Integer applyNum;

	@ApiModelProperty("已签到人数")
	private Integer signNum;

	@ApiModelProperty("省编码")
    private String provinceCode;

	@ApiModelProperty("省名称")
    private String provinceName;

	@ApiModelProperty("市编码")
	private String cityCode;

	@ApiModelProperty("市名称")
	private String cityName;

	@ApiModelProperty("区编码")
	private String districtCode;

	@ApiModelProperty("区名称")
	private String districtName;

	@ApiModelProperty("经度")
    private Double longitude;

	@ApiModelProperty("纬度")
    private Double latitude;

	@ApiModelProperty("详细地址")
	private String address;

	@ApiModelProperty("活动开始时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date startTime;

	@ApiModelProperty("活动结束时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date endTime;

	@ApiModelProperty("活动开始时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
	private Date applyStartTime;

	@ApiModelProperty("活动结束时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
	private Date applyEndTime;

	@ApiModelProperty("是否需要填写年龄 0-否 1-是")
	private Integer needAge;

	@ApiModelProperty("是否需要填写证件号 0-否 1-是")
	private Integer needIdCard;

	@ApiModelProperty("是否需要填写衣服尺码 0-否 1-是")
	private Integer needClothes;

	@ApiModelProperty("是否需要填写鞋子尺码 0-否 1-是")
	private Integer needShoes;

	@ApiModelProperty("活动描述")
    private String desc;

	@ApiModelProperty("活动描述图片")
	private String descImg;

	@ApiModelProperty("报名状态 0:未报名 1:已报名 2-已取消")
	private Integer applyStatus;

	@ApiModelProperty("活动状态 0:未开始 1:进行中 2:已结束")
	private Integer activityStatus;

	@ApiModelProperty("活动开始前x小时提醒")
	private Integer startRemind;

	@ApiModelProperty("活动结束后x小时提醒")
	private Integer endRemind;

	@ApiModelProperty("活动结束后提醒是否开启 0-否 1-是")
	private Integer endRemindStatus;

	@ApiModelProperty("活动结束后提醒跳转链接")
	private String endRemindUrl;

	@ApiModelProperty("活动开始前提醒时间")
	private Date startRemindTime;

	@ApiModelProperty("活动结束后提醒时间")
	private Date endRemindTime;

	@ApiModelProperty("签到状态 0-否 1-是")
	private Integer signStatus;

	@ApiModelProperty("报名用户信息")
	private DistributionStoreActivityUser distributionStoreActivityUser;

	@ApiModelProperty("活动衣服尺码集")
	private List<String> clothesSizes;

	@ApiModelProperty("活动鞋子尺码集")
	private List<String> shoesSizes;

	@ApiModelProperty("活动上新是否发送订阅消息提醒 0否1是")
	private Integer newNotice;
	@ApiModelProperty("活动上新 温馨提示文案")
	private String newReminder;
	@ApiModelProperty("活动开始是否发送订阅消息提醒 0否1是")
	private Integer startNotice;
	@ApiModelProperty("活动开始 温馨提示文案")
	private String startReminder;
	@ApiModelProperty("活动结束是否发送订阅消息提醒 0否1是")
	private Integer endNotice;
	@ApiModelProperty("活动结束 温馨提示文案")
	private String endReminder;

	public Integer getNewNotice() {
		return newNotice;
	}

	public void setNewNotice(Integer newNotice) {
		this.newNotice = newNotice;
	}

	public String getNewReminder() {
		return newReminder;
	}

	public void setNewReminder(String newReminder) {
		this.newReminder = newReminder;
	}

	public Integer getStartNotice() {
		return startNotice;
	}

	public void setStartNotice(Integer startNotice) {
		this.startNotice = startNotice;
	}

	public String getStartReminder() {
		return startReminder;
	}

	public void setStartReminder(String startReminder) {
		this.startReminder = startReminder;
	}

	public Integer getEndNotice() {
		return endNotice;
	}

	public void setEndNotice(Integer endNotice) {
		this.endNotice = endNotice;
	}

	public String getEndReminder() {
		return endReminder;
	}

	public void setEndReminder(String endReminder) {
		this.endReminder = endReminder;
	}

	public List<String> getClothesSizes() {
		return clothesSizes;
	}

	public void setClothesSizes(List<String> clothesSizes) {
		this.clothesSizes = clothesSizes;
	}

	public List<String> getShoesSizes() {
		return shoesSizes;
	}

	public void setShoesSizes(List<String> shoesSizes) {
		this.shoesSizes = shoesSizes;
	}

	public String getDescImg() {
		return descImg;
	}

	public void setDescImg(String descImg) {
		this.descImg = descImg;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Integer getStatus() {
		return status;
	}

	public Date getApplyStartTime() {
		return applyStartTime;
	}

	public void setApplyStartTime(Date applyStartTime) {
		this.applyStartTime = applyStartTime;
	}

	public Date getApplyEndTime() {
		return applyEndTime;
	}

	public void setApplyEndTime(Date applyEndTime) {
		this.applyEndTime = applyEndTime;
	}

	public Integer getNeedClothes() {
		return needClothes;
	}

	public void setNeedClothes(Integer needClothes) {
		this.needClothes = needClothes;
	}

	public Integer getNeedShoes() {
		return needShoes;
	}

	public void setNeedShoes(Integer needShoes) {
		this.needShoes = needShoes;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getLimitApplyNum() {
		return limitApplyNum;
	}

	public void setLimitApplyNum(Integer limitApplyNum) {
		this.limitApplyNum = limitApplyNum;
	}

	public Integer getApplyNum() {
		return applyNum;
	}

	public void setApplyNum(Integer applyNum) {
		this.applyNum = applyNum;
	}
	public Integer getSignNum() {
		return signNum;
	}

	public void setSignNum(Integer signNum) {
		this.signNum = signNum;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getNeedAge() {
		return needAge;
	}

	public void setNeedAge(Integer needAge) {
		this.needAge = needAge;
	}

	public Integer getNeedIdCard() {
		return needIdCard;
	}

	public void setNeedIdCard(Integer needIdCard) {
		this.needIdCard = needIdCard;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Integer getApplyStatus() {
		return applyStatus;
	}

	public void setApplyStatus(Integer applyStatus) {
		this.applyStatus = applyStatus;
	}

	public Integer getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(Integer activityStatus) {
		this.activityStatus = activityStatus;
	}

	public Integer getStartRemind() {
		return startRemind;
	}

	public void setStartRemind(Integer startRemind) {
		this.startRemind = startRemind;
	}

	public Integer getEndRemind() {
		return endRemind;
	}

	public void setEndRemind(Integer endRemind) {
		this.endRemind = endRemind;
	}

	public Integer getEndRemindStatus() {
		return endRemindStatus;
	}

	public void setEndRemindStatus(Integer endRemindStatus) {
		this.endRemindStatus = endRemindStatus;
	}

	public String getEndRemindUrl() {
		return endRemindUrl;
	}

	public void setEndRemindUrl(String endRemindUrl) {
		this.endRemindUrl = endRemindUrl;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public Date getStartRemindTime() {
		return startRemindTime;
	}

	public void setStartRemindTime(Date startRemindTime) {
		this.startRemindTime = startRemindTime;
	}

	public Date getEndRemindTime() {
		return endRemindTime;
	}

	public void setEndRemindTime(Date endRemindTime) {
		this.endRemindTime = endRemindTime;
	}

	public Integer getSignStatus() {
		return signStatus;
	}

	public void setSignStatus(Integer signStatus) {
		this.signStatus = signStatus;
	}

	public DistributionStoreActivityUser getDistributionStoreActivityUser() {
		return distributionStoreActivityUser;
	}

	public void setDistributionStoreActivityUser(DistributionStoreActivityUser distributionStoreActivityUser) {
		this.distributionStoreActivityUser = distributionStoreActivityUser;
	}
}
