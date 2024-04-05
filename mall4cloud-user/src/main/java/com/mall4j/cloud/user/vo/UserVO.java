package com.mall4j.cloud.user.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * 用户表VO
 *
 * @author YXF
 * @date 2020-12-08 11:18:04
 */
public class UserVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    private Long userId;

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("M(男) or F(女)")
    private String sex;

    @ApiModelProperty("例如：2009-11-27")
    private String birthDate;

    @ApiModelProperty("头像图片路径")
    private String pic;

    @ApiModelProperty("状态 1 正常 0 无效")
    private Integer status;

    @ApiModelProperty("会员等级（冗余字段）")
    private Integer level;

    @ApiModelProperty("vip结束时间")
    private Date vipEndTime;

    @ApiModelProperty("等级条件 0 普通会员 1 付费会员")
    private Integer levelType;

	@ApiModelProperty("手机号(冗余)")
    private String phone;

	/**
	 * openId list
	 */
	private List<String> bizUserIdList;

	public List<String> getBizUserIdList() {
		return bizUserIdList;
	}

	public void setBizUserIdList(List<String> bizUserIdList) {
		this.bizUserIdList = bizUserIdList;
	}

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

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
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

	@Override
	public String toString() {
		return "UserApiVO{" +
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
				'}';
	}
}
