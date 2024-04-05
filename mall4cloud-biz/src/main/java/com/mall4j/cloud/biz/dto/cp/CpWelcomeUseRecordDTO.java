package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 欢迎语 使用记录DTO
 *
 * @author FrozenWatermelon
 * @date 2023-10-27 18:13:07
 */
public class CpWelcomeUseRecordDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("欢迎语id")
    private Long welId;

    @ApiModelProperty("导购编号")
    private Long staffId;

    @ApiModelProperty("导购名称")
    private String staffName;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("用户手机号")
    private String phone;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getWelId() {
		return welId;
	}

	public void setWelId(Long welId) {
		this.welId = welId;
	}

	public Long getStaffId() {
		return staffId;
	}

	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "CpWelcomeUseRecordDTO{" +
				"id=" + id +
				",welId=" + welId +
				",staffId=" + staffId +
				",staffName=" + staffName +
				",userId=" + userId +
				",nickName=" + nickName +
				",phone=" + phone +
				'}';
	}
}
