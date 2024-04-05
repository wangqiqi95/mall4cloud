package com.mall4j.cloud.api.docking.skq_crm.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 类描述：会员基本信息查询响应
 *
 * @date 2022/1/23 14:11：29
 */
public class CustomerGetVo implements Serializable {

	private static final long serialVersionUID = -5429504503767163077L;
	@ApiModelProperty(value = "uni_id")
	private String union_id;

	@ApiModelProperty(value = "openId")
	private String openid;

	@ApiModelProperty(value = "CRM卡号")
	private String vipcode;

	@ApiModelProperty(value = "手机号,1开头的11位数字")
	private String mobilephone;

	@ApiModelProperty(value = "生日,yyyy-MM-dd")
	private String birthday;

	@ApiModelProperty(value = "姓名")
	private String customer_name;

	@ApiModelProperty(value = "性别,F/M")
	private String gender;

	@ApiModelProperty(value = "联系地址")
	private String address;

	@ApiModelProperty(value = "电子邮箱,必须包含@符号")
	private String email;

	@ApiModelProperty(value = "职业")
	private String job;

	@ApiModelProperty(value = "邮编")
	private String zipcode;

	@ApiModelProperty(value = "省份")
	private String province;

	@ApiModelProperty(value = "城市")
	private String city;

	@ApiModelProperty(value = "区县")
	private String area;

	@ApiModelProperty(value = "开卡门店id")
	private String reg_store_id;

	@ApiModelProperty(value = "开卡门店名称")
	private String reg_store_name;

	@ApiModelProperty(value = "活动id")
	private String activity_id;

	@ApiModelProperty(value = "活动名称")
	private String activity_name;

	@ApiModelProperty(value = "注册时间,yyyy-MM-dd HH:mm:ss")
	private String reg_time;

	@ApiModelProperty(value = "当前有效积分")
	private Long current_valid_point;

	/**
	 * 1---新奇（60005）
	 * 2---好奇（60006）
	 * 3---炫奇（60007）
	 * 4---珍奇（60008）
	 */
	@ApiModelProperty(value = "当前等级，1---新奇，2---好奇，3---炫奇，4---珍奇")
	private String current_grade_id;

	@ApiModelProperty(value = "距离下一个升级还需要消费多少金额")
	private BigDecimal next_pay;

	@ApiModelProperty(value = "等级到期时间,yyyy-MM-dd HH:mm:ss")
	private String grade_expire;

	@ApiModelProperty(value = "是否黑名单,Y/N")
	private String is_blacklist;

	@ApiModelProperty(value = "一宝性别,F/M")
	private String have_baby;

	@ApiModelProperty(value = "宝宝生日,yyyy-MM-dd")
	private String baby_birthday;

	@ApiModelProperty(value = "兴趣")
	private String interests;

	@ApiModelProperty(value = "爱好")
	private String hobby;

	@ApiModelProperty(value = "是否员工,Y/N")
	private String isemployee;

	@ApiModelProperty(value = "是否代购，Y/N")
	private String isbuyer;

	@ApiModelProperty(value = "二宝性别，M男宝宝，F女宝宝")
	private String second_baby_sex;

	@ApiModelProperty(value = "二宝生日，yyyy-MM-dd")
	private String second_baby_birth;

	@ApiModelProperty(value = "三宝性别，M男宝宝，F女宝宝")
	private String third_baby_sex;

	@ApiModelProperty(value = "三宝生日，yyyy-MM-dd")
	private String third_baby_birth;

	public String getUnion_id() {
		return union_id;
	}

	public void setUnion_id(String union_id) {
		this.union_id = union_id;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getVipcode() {
		return vipcode;
	}

	public void setVipcode(String vipcode) {
		this.vipcode = vipcode;
	}

	public String getMobilephone() {
		return mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getCustomer_name() {
		return customer_name;
	}

	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getReg_store_id() {
		return reg_store_id;
	}

	public void setReg_store_id(String reg_store_id) {
		this.reg_store_id = reg_store_id;
	}

	public String getReg_store_name() {
		return reg_store_name;
	}

	public void setReg_store_name(String reg_store_name) {
		this.reg_store_name = reg_store_name;
	}

	public String getActivity_id() {
		return activity_id;
	}

	public void setActivity_id(String activity_id) {
		this.activity_id = activity_id;
	}

	public String getActivity_name() {
		return activity_name;
	}

	public void setActivity_name(String activity_name) {
		this.activity_name = activity_name;
	}

	public String getReg_time() {
		return reg_time;
	}

	public void setReg_time(String reg_time) {
		this.reg_time = reg_time;
	}

	public Long getCurrent_valid_point() {
		return current_valid_point;
	}

	public void setCurrent_valid_point(Long current_valid_point) {
		this.current_valid_point = current_valid_point;
	}

	public String getCurrent_grade_id() {
		return current_grade_id;
	}

	public void setCurrent_grade_id(String current_grade_id) {
		this.current_grade_id = current_grade_id;
	}

	public BigDecimal getNext_pay() {
		return next_pay;
	}

	public void setNext_pay(BigDecimal next_pay) {
		this.next_pay = next_pay;
	}

	public String getGrade_expire() {
		return grade_expire;
	}

	public void setGrade_expire(String grade_expire) {
		this.grade_expire = grade_expire;
	}

	public String getIs_blacklist() {
		return is_blacklist;
	}

	public void setIs_blacklist(String is_blacklist) {
		this.is_blacklist = is_blacklist;
	}

	public String getHave_baby() {
		return have_baby;
	}

	public void setHave_baby(String have_baby) {
		this.have_baby = have_baby;
	}

	public String getBaby_birthday() {
		return baby_birthday;
	}

	public void setBaby_birthday(String baby_birthday) {
		this.baby_birthday = baby_birthday;
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

	public String getIsemployee() {
		return isemployee;
	}

	public void setIsemployee(String isemployee) {
		this.isemployee = isemployee;
	}

	public String getIsbuyer() {
		return isbuyer;
	}

	public void setIsbuyer(String isbuyer) {
		this.isbuyer = isbuyer;
	}

	public String getSecond_baby_sex() {
		return second_baby_sex;
	}

	public void setSecond_baby_sex(String second_baby_sex) {
		this.second_baby_sex = second_baby_sex;
	}

	public String getSecond_baby_birth() {
		return second_baby_birth;
	}

	public void setSecond_baby_birth(String second_baby_birth) {
		this.second_baby_birth = second_baby_birth;
	}

	public String getThird_baby_sex() {
		return third_baby_sex;
	}

	public void setThird_baby_sex(String third_baby_sex) {
		this.third_baby_sex = third_baby_sex;
	}

	public String getThird_baby_birth() {
		return third_baby_birth;
	}

	public void setThird_baby_birth(String third_baby_birth) {
		this.third_baby_birth = third_baby_birth;
	}

	@Override
	public String toString() {
		return "CustomerGetVo{" + "union_id='" + union_id + '\'' + ", openid='" + openid + '\'' + ", vipcode='" + vipcode + '\'' + ", mobilephone='"
				+ mobilephone + '\'' + ", birthday='" + birthday + '\'' + ", customer_name='" + customer_name + '\'' + ", gender='" + gender + '\''
				+ ", address='" + address + '\'' + ", email='" + email + '\'' + ", job='" + job + '\'' + ", zipcode='" + zipcode + '\'' + ", province='"
				+ province + '\'' + ", city='" + city + '\'' + ", area='" + area + '\'' + ", reg_store_id='" + reg_store_id + '\'' + ", reg_store_name='"
				+ reg_store_name + '\'' + ", activity_id='" + activity_id + '\'' + ", activity_name='" + activity_name + '\'' + ", reg_time='" + reg_time + '\''
				+ ", current_valid_point=" + current_valid_point + ", current_grade_id='" + current_grade_id + '\'' + ", next_pay=" + next_pay
				+ ", grade_expire='" + grade_expire + '\'' + ", is_blacklist='" + is_blacklist + '\'' + ", have_baby='" + have_baby + '\'' + ", baby_birthday='"
				+ baby_birthday + '\'' + ", interests='" + interests + '\'' + ", hobby='" + hobby + '\'' + ", isemployee='" + isemployee + '\'' + ", isbuyer='"
				+ isbuyer + '\'' + ", second_baby_sex='" + second_baby_sex + '\'' + ", second_baby_birth='" + second_baby_birth + '\'' + ", third_baby_sex='"
				+ third_baby_sex + '\'' + ", third_baby_birth='" + third_baby_birth + '\'' + '}';
	}
}
