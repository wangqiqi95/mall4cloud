package com.mall4j.cloud.api.user.dto;

import io.swagger.annotations.ApiModelProperty;

public class MemberUpdateDto {

	private static final long serialVersionUID = 4051268170049280691L;
	@ApiModelProperty(value = "vipcode")
	private String vipcode;

	@ApiModelProperty(value = "union_id", required = true)
	private String union_id;

	@ApiModelProperty(value = "推荐人手机号,校验非空and格式11位数字")
	private String referee_phone;

	@ApiModelProperty(value = "姓名", required = true)
	private String customer_name;

	@ApiModelProperty(value = "性别,校验非空，M：男；F：女；", required = true)
	private String gender;

	@ApiModelProperty(value = "生日,校验非空, yyyy-MM-dd", required = true)
	private String birthday;

	@ApiModelProperty(value = "省")
	private String province;

	@ApiModelProperty(value = "市")
	private String city;

	@ApiModelProperty(value = "区")
	private String area;

	@ApiModelProperty(value = "邮箱,校验包含“@”、“.”")
	private String email;

	@ApiModelProperty(value = "邮编")
	private String zipcode;

	@ApiModelProperty(value = "职业")
	private String job;

	@ApiModelProperty(value = "地址")
	private String address;

	@ApiModelProperty(value = "是否有宝宝,M男宝宝，F女宝宝 ")
	private String have_baby;

	@ApiModelProperty(value = "宝宝生日,yyyy-MM-dd")
	private String baby_birthday;

	@ApiModelProperty(value = "兴趣")
	private String interests;

	@ApiModelProperty(value = "爱好")
	private String hobby;

	@ApiModelProperty(value = "二宝性别,M男宝宝，F女宝宝")
	private String second_baby_sex;

	@ApiModelProperty(value = "二宝生日,yyyy-MM-dd")
	private String second_baby_birth;

	@ApiModelProperty(value = "三宝性别,M男宝宝，F女宝宝")
	private String third_baby_sex;

	@ApiModelProperty(value = "三宝生日,yyyy-MM-dd")
	private String third_baby_birth;

	@ApiModelProperty(value = "当前有效积分")
	private String current_valid_point;

	@ApiModelProperty(value = "当前等级")
	private String current_grade_id;

	@ApiModelProperty(value = "距离下一个升级还需要消费多少金额")
	private String next_pay;

	@ApiModelProperty(value = "等级名称")
	private String level_name;

	public String getCurrent_valid_point() {
		return current_valid_point;
	}

	public void setCurrent_valid_point(String current_valid_point) {
		this.current_valid_point = current_valid_point;
	}

	public String getCurrent_grade_id() {
		return current_grade_id;
	}

	public void setCurrent_grade_id(String current_grade_id) {
		this.current_grade_id = current_grade_id;
	}

	public String getNext_pay() {
		return next_pay;
	}

	public void setNext_pay(String next_pay) {
		this.next_pay = next_pay;
	}

	public String getLevel_name() {
		return level_name;
	}

	public void setLevel_name(String level_name) {
		this.level_name = level_name;
	}

	public String getVipcode() {
		return vipcode;
	}

	public void setVipcode(String vipcode) {
		this.vipcode = vipcode;
	}

	public String getUnion_id() {
		return union_id;
	}

	public void setUnion_id(String union_id) {
		this.union_id = union_id;
	}

	public String getReferee_phone() {
		return referee_phone;
	}

	public void setReferee_phone(String referee_phone) {
		this.referee_phone = referee_phone;
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

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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
		return "CustomerUpdateDto{" + "vipcode='" + vipcode + '\'' + ", union_id='" + union_id + '\'' + ", referee_phone='" + referee_phone + '\''
				+ ", customer_name='" + customer_name + '\'' + ", gender='" + gender + '\'' + ", birthday='" + birthday + '\'' + ", province='" + province
				+ '\'' + ", city='" + city + '\'' + ", area='" + area + '\'' + ", email='" + email + '\'' + ", zipcode='" + zipcode + '\'' + ", job='" + job
				+ '\'' + ", address='" + address + '\'' + ", have_baby='" + have_baby + '\'' + ", baby_birthday='" + baby_birthday + '\'' + ", interests='"
				+ interests + '\'' + ", hobby='" + hobby + '\'' + ", second_baby_sex='" + second_baby_sex + '\'' + ", second_baby_birth='" + second_baby_birth
				+ '\'' + ", third_baby_sex='" + third_baby_sex + '\'' + ", third_baby_birth='" + third_baby_birth + '\'' + '}';
	}
}
