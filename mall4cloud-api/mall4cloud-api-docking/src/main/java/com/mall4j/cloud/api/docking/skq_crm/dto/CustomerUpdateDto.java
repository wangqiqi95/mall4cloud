package com.mall4j.cloud.api.docking.skq_crm.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import lombok.Data;

/**
 * 类描述：会员修改接口请求参数
 *
 * @date 2022/1/23 19:20：14
 */
@Data
public class CustomerUpdateDto implements Serializable {

	private static final long serialVersionUID = -1795072875223847520L;
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

	@ApiModelProperty(value = "所属门店编号")
	private String servShop;

	@ApiModelProperty(value = "所属导购员工编号")
	private String servUser;

}
