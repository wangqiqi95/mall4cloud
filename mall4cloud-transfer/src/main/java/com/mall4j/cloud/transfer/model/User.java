package com.mall4j.cloud.transfer.model;

import com.mall4j.cloud.common.model.BaseModel;

import java.io.Serializable;
import java.util.Date;
/**
 * 用户表
 *
 * @author FrozenWatermelon
 * @date 2022-04-06 14:24:19
 */
public class User extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long userId;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 1(男) or 0(女)
     */
    private String sex;

    /**
     * 例如：2009-11-27
     */
    private String birthDate;

    /**
     * 头像图片路径
     */
    private String pic;

    /**
     * 状态 1 正常 0 无效
     */
    private Integer status;

    /**
     * 会员等级（冗余字段）
     */
    private Long level;

    /**
     * vip结束时间
     */
    private Date vipEndTime;

    /**
     * 等级条件 0 普通会员 1 付费会员
     */
    private Integer levelType;

    /**
     * 手机号 (冗余字段)
     */
    private String phone;

    /**
     * 用户vip等级，为空则非付费会员
     */
    private Integer vipLevel;

    /**
     * 组织ID
     */
    private Long orgId;

    /**
     * 门店ID
     */
    private Long storeId;

    /**
     * 关联crm会员id
     */
    private String vipcode;

    /**
     * 所属导购ID
     */
    private Long staffId;

    /**
     * 微客状态 -1-初始化 0-禁用 1-启用 2-拉黑
     */
    private Integer veekerStatus;

    /**
     * 微客审核状态 -1-初始化 0-待审核 1-已同意 2-已拒绝
     */
    private Integer veekerAuditStatus;

    /**
     * 是否添加微信 0-否 1-是(已废弃,是否添加通过关系表判断)
     */
    private Integer addWechat;

    /**
     * 微客申请时间
     */
    private Date veekerApplyTime;

    /**
     * 
     */
    private String createBy;

    /**
     * 
     */
    private String updateBy;

    /**
     * union_id
     */
    private String unionId;

    /**
     * 小程序openId
     */
    private String openId;

    /**
     * 推荐人手机号
     */
    private String refereePhone;

    /**
     * 姓名
     */
    private String customerName;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 职业
     */
    private String job;

    /**
     * 宝宝性别,M男宝宝，F女宝宝
     */
    private String haveBaby;

    /**
     * 宝宝生日,yyyy-MM-dd
     */
    private String babyBirthday;

    /**
     * 兴趣
     */
    private String interests;

    /**
     * 爱好
     */
    private String hobby;

    /**
     * 二宝性别,M男宝宝，F女宝宝
     */
    private String secondBabySex;

    /**
     * 二宝生日,yyyy-MM-dd
     */
    private String secondBabyBirth;

    /**
     * 三宝性别,M男宝宝，F女宝宝
     */
    private String thirdBabySex;

    /**
     * 三宝生日,yyyy-MM-dd
     */
    private String thirdBabyBirth;

    /**
     * 省ID
     */
    private Long provinceId;

    /**
     * 省
     */
    private String province;

    /**
     * 城市ID
     */
    private Long cityId;

    /**
     * 城市
     */
    private String city;

    /**
     * 区ID
     */
    private Long areaId;

    /**
     * 区
     */
    private String area;

    /**
     * 会员姓名
     */
    private String name;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getLevel() {
		return level;
	}

	public void setLevel(Long level) {
		this.level = level;
	}

	public Date getVipEndTime() {
		return vipEndTime;
	}

	public void setVipEndTime(Date vipEndTime) {
		this.vipEndTime = vipEndTime;
	}

	public Integer getLevelType() {
		return levelType;
	}

	public void setLevelType(Integer levelType) {
		this.levelType = levelType;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(Integer vipLevel) {
		this.vipLevel = vipLevel;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public String getVipcode() {
		return vipcode;
	}

	public void setVipcode(String vipcode) {
		this.vipcode = vipcode;
	}

	public Long getStaffId() {
		return staffId;
	}

	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}

	public Integer getVeekerStatus() {
		return veekerStatus;
	}

	public void setVeekerStatus(Integer veekerStatus) {
		this.veekerStatus = veekerStatus;
	}

	public Integer getVeekerAuditStatus() {
		return veekerAuditStatus;
	}

	public void setVeekerAuditStatus(Integer veekerAuditStatus) {
		this.veekerAuditStatus = veekerAuditStatus;
	}

	public Integer getAddWechat() {
		return addWechat;
	}

	public void setAddWechat(Integer addWechat) {
		this.addWechat = addWechat;
	}

	public Date getVeekerApplyTime() {
		return veekerApplyTime;
	}

	public void setVeekerApplyTime(Date veekerApplyTime) {
		this.veekerApplyTime = veekerApplyTime;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public String getUnionId() {
		return unionId;
	}

	public void setUnionId(String unionId) {
		this.unionId = unionId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getRefereePhone() {
		return refereePhone;
	}

	public void setRefereePhone(String refereePhone) {
		this.refereePhone = refereePhone;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getHaveBaby() {
		return haveBaby;
	}

	public void setHaveBaby(String haveBaby) {
		this.haveBaby = haveBaby;
	}

	public String getBabyBirthday() {
		return babyBirthday;
	}

	public void setBabyBirthday(String babyBirthday) {
		this.babyBirthday = babyBirthday;
	}

	public String getInterests() {
		return interests;
	}

	public void setInterests(String interests) {
		this.interests = interests;
	}

	public String getHobby() {
		return hobby;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
	}

	public String getSecondBabySex() {
		return secondBabySex;
	}

	public void setSecondBabySex(String secondBabySex) {
		this.secondBabySex = secondBabySex;
	}

	public String getSecondBabyBirth() {
		return secondBabyBirth;
	}

	public void setSecondBabyBirth(String secondBabyBirth) {
		this.secondBabyBirth = secondBabyBirth;
	}

	public String getThirdBabySex() {
		return thirdBabySex;
	}

	public void setThirdBabySex(String thirdBabySex) {
		this.thirdBabySex = thirdBabySex;
	}

	public String getThirdBabyBirth() {
		return thirdBabyBirth;
	}

	public void setThirdBabyBirth(String thirdBabyBirth) {
		this.thirdBabyBirth = thirdBabyBirth;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "User{" +
				"userId=" + userId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",nickName=" + nickName +
				",sex=" + sex +
				",birthDate=" + birthDate +
				",pic=" + pic +
				",status=" + status +
				",level=" + level +
				",vipEndTime=" + vipEndTime +
				",levelType=" + levelType +
				",phone=" + phone +
				",vipLevel=" + vipLevel +
				",orgId=" + orgId +
				",storeId=" + storeId +
				",vipcode=" + vipcode +
				",staffId=" + staffId +
				",veekerStatus=" + veekerStatus +
				",veekerAuditStatus=" + veekerAuditStatus +
				",addWechat=" + addWechat +
				",veekerApplyTime=" + veekerApplyTime +
				",createBy=" + createBy +
				",updateBy=" + updateBy +
				",unionId=" + unionId +
				",openId=" + openId +
				",refereePhone=" + refereePhone +
				",customerName=" + customerName +
				",email=" + email +
				",job=" + job +
				",haveBaby=" + haveBaby +
				",babyBirthday=" + babyBirthday +
				",interests=" + interests +
				",hobby=" + hobby +
				",secondBabySex=" + secondBabySex +
				",secondBabyBirth=" + secondBabyBirth +
				",thirdBabySex=" + thirdBabySex +
				",thirdBabyBirth=" + thirdBabyBirth +
				",provinceId=" + provinceId +
				",province=" + province +
				",cityId=" + cityId +
				",city=" + city +
				",areaId=" + areaId +
				",area=" + area +
				",name=" + name +
				'}';
	}
}
