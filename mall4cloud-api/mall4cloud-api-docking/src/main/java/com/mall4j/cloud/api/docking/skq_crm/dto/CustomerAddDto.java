package com.mall4j.cloud.api.docking.skq_crm.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 类描述：会员新增接口请求参数
 *
 * @date 2022/1/23 15:11：21
 */
public class CustomerAddDto implements Serializable {

	private static final long serialVersionUID = -4593891351862398865L;
	@ApiModelProperty(value = "openid", required = true)
	private String openid;

	@ApiModelProperty(value = "union_id", required = true)
	private String union_id;

	@ApiModelProperty(value = "手机号", required = true)
	private String mobilephone;

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

	@ApiModelProperty(value = "开卡门店id,通过扫活动二维码注册的，开卡门店设置为虚拟门店", required = true)
	private String reg_store_id;

	@ApiModelProperty(value = "开卡门店名称")
	private String reg_store_name;

	@ApiModelProperty(value = "活动id")
	private String activity_id;

	@ApiModelProperty(value = "注册时间,yyyy-MM-dd HH:mm:ss", required = true)
	private String reg_time;

	@ApiModelProperty(value = "销售员")
	private String salesman;

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

	@ApiModelProperty(value = "注册公众号")
	private String register_officialaccount;

	@ApiModelProperty(value = "所属门店编号")
	private String servShop;

	@ApiModelProperty(value = "所属导购员工编号")
	private String servUser;

	@ApiModelProperty(value = "开卡导购编码")
	private String regSaler;

	@ApiModelProperty(value = "虚拟门店名称")
	private String regShopNickName;
	@ApiModelProperty(value = "虚拟门店Code")
	private String virtualShopCode;



	public String getRegShopNickName() {
		return regShopNickName;
	}

	public void setRegShopNickName(String regShopNickName) {
		this.regShopNickName = regShopNickName;
	}

	public String getVirtualShopCode() {
		return virtualShopCode;
	}

	public void setVirtualShopCode(String virtualShopCode) {
		this.virtualShopCode = virtualShopCode;
	}

	public String getServShop() {
		return servShop;
	}

	public void setServShop(String servShop) {
		this.servShop = servShop;
	}

	public String getServUser() {
		return servUser;
	}

	public void setServUser(String servUser) {
		this.servUser = servUser;
	}

	public String getRegSaler() {
		return regSaler;
	}

	public void setRegSaler(String regSaler) {
		this.regSaler = regSaler;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getUnion_id() {
		return union_id;
	}

	public void setUnion_id(String union_id) {
		this.union_id = union_id;
	}

	public String getMobilephone() {
		return mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
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

	public String getReg_time() {
		return reg_time;
	}

	public void setReg_time(String reg_time) {
		this.reg_time = reg_time;
	}

	public String getSalesman() {
		return salesman;
	}

	public void setSalesman(String salesman) {
		this.salesman = salesman;
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

	public String getRegister_officialaccount() {
		return register_officialaccount;
	}

	public void setRegister_officialaccount(String register_officialaccount) {
		this.register_officialaccount = register_officialaccount;
	}

	@Override
	public String toString() {
		return "CustomerAddDto{" +
				"openid='" + openid + '\'' +
				", union_id='" + union_id + '\'' +
				", mobilephone='" + mobilephone + '\'' +
				", referee_phone='" + referee_phone + '\'' +
				", customer_name='" + customer_name + '\'' +
				", gender='" + gender + '\'' +
				", birthday='" + birthday + '\'' +
				", province='" + province + '\'' +
				", city='" + city + '\'' +
				", area='" + area + '\'' +
				", email='" + email + '\'' +
				", zipcode='" + zipcode + '\'' +
				", job='" + job + '\'' +
				", address='" + address + '\'' +
				", reg_store_id='" + reg_store_id + '\'' +
				", reg_store_name='" + reg_store_name + '\'' +
				", activity_id='" + activity_id + '\'' +
				", reg_time='" + reg_time + '\'' +
				", salesman='" + salesman + '\'' +
				", have_baby='" + have_baby + '\'' +
				", baby_birthday='" + baby_birthday + '\'' +
				", interests='" + interests + '\'' +
				", hobby='" + hobby + '\'' +
				", second_baby_sex='" + second_baby_sex + '\'' +
				", second_baby_birth='" + second_baby_birth + '\'' +
				", third_baby_sex='" + third_baby_sex + '\'' +
				", third_baby_birth='" + third_baby_birth + '\'' +
				", register_officialaccount='" + register_officialaccount + '\'' +
				", servShop='" + servShop + '\'' +
				", servUser='" + servUser + '\'' +
				", regSaler='" + regSaler + '\'' +
				'}';
	}
}
