package com.mall4j.cloud.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import java.util.Date;

/**
 * 用户表DTO
 *
 * @author YXF
 * @date 2020-12-08 11:18:04
 */
@Data
@ApiModel("晚上用户信息")
public class UserDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    private Long userId;

    @ApiModelProperty("用户昵称")
	@Length(min = 2, max = 12)
    private String nickName;

	@ApiModelProperty("用户姓名")
	@Length(min = 2, max = 12)
	private String name;

    @ApiModelProperty("M(男) or F(女)")
	@Max(value = 1, message = "性别只能为男，女")
	@Max(value = 0, message = "性别只能为男，女")
    private String sex;

    @ApiModelProperty("例如：2009-11-27")
    private String birthDate;

    @ApiModelProperty("头像图片路径")
    private String pic;

    @ApiModelProperty("状态 1 正常 0 无效")
    private Integer status;

	@ApiModelProperty(value = "邮箱")
	private String email;

	@ApiModelProperty(value = "生日")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date birthday;

	@ApiModelProperty(value = "宝宝性别,M男宝宝，F女宝宝")
	private String haveBaby;
	@ApiModelProperty(value = "宝宝生日,yyyy-MM-dd")
	private String babyBirthday;
	@ApiModelProperty(value = "二宝性别,M男宝宝，F女宝宝")
	private String secondBabySex;
	@ApiModelProperty(value = "二宝生日,yyyy-MM-dd")
	private String secondBabyBirth;
	@ApiModelProperty(value = "三宝性别,M男宝宝，F女宝宝")
	private String thirdBabySex;
	@ApiModelProperty(value = "三宝生日,yyyy-MM-dd")
	private String thirdBabyBirth;
	@ApiModelProperty(value = "省id")
	private Long provinceId;
	@ApiModelProperty(value = "省")
	private String province;
	@ApiModelProperty(value = "市Id")
	private Long cityId;
	@ApiModelProperty(value = "市")
	private String city;
	@ApiModelProperty(value = "区id")
	private Long areaId;
	@ApiModelProperty(value = "区")
	private String area;

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


	@Override
	public String toString() {
		return "UserDTO{" +
				"userId=" + userId +
				",nickName=" + nickName +
				",sex=" + sex +
				",birthDate=" + birthDate +
				",pic=" + pic +
				",status=" + status +
				'}';
	}
}
