package com.mall4j.cloud.api.openapi.ipuhuo.vo;

import com.mall4j.cloud.api.openapi.ipuhuo.dto.BaseResultDto;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class UserInfoVO implements BaseResultDto,Serializable {

	private static final long serialVersionUID = -1206833518339288751L;
	@ApiModelProperty(value = "用户Id")
	private Long userid;
	@ApiModelProperty(value = "用户名称")
	private String username;
	@ApiModelProperty(value = "用户名称,可选")
	private String nickname;

	public UserInfoVO() {
	}

	public UserInfoVO(Long userid, String username, String nickname) {
		this.userid = userid;
		this.username = username;
		this.nickname = nickname;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	@Override
	public String toString() {
		return "UserInfoVO{" + "userid=" + userid + ", username='" + username + '\'' + ", nickname='" + nickname + '\'' + '}';
	}
}
