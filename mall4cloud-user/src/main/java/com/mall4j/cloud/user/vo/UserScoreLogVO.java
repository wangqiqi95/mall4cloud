package com.mall4j.cloud.user.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mall4j.cloud.common.serializer.ImgJsonSerializer;
import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

/**
 * 用户积分记录VO
 *
 * @author YXF
 * @date 2020-12-08 10:16:53
 */
public class UserScoreLogVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("日志id")
    private Long logId;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("积分类型 0.注册送积分1.购物 2.会员等级提升加积分 3.签到加积分 4.购物抵扣使用积分 5.积分过期 6.余额充值 7.系统更改积分 8.购物抵扣使用积分退还")
    private Integer source;

    @ApiModelProperty("变动积分数额")
    private Long score;

    @ApiModelProperty("业务id")
    private Long bizId;

    @ApiModelProperty("出入类型 0=支出 1=收入")
    private Integer ioType;

    @ApiModelProperty("商品名称")
    private String spuName;

    @ApiModelProperty("商品主图")
	@JsonSerialize(using = ImgJsonSerializer.class)
    private String mainImgUrl;

	public Long getLogId() {
		return logId;
	}

	public void setLogId(Long logId) {
		this.logId = logId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public Long getScore() {
		return score;
	}

	public void setScore(Long score) {
		this.score = score;
	}

	public Long getBizId() {
		return bizId;
	}

	public void setBizId(Long bizId) {
		this.bizId = bizId;
	}

	public Integer getIoType() {
		return ioType;
	}

	public void setIoType(Integer ioType) {
		this.ioType = ioType;
	}

	public String getSpuName() {
		return spuName;
	}

	public void setSpuName(String spuName) {
		this.spuName = spuName;
	}

	public String getMainImgUrl() {
		return mainImgUrl;
	}

	public void setMainImgUrl(String mainImgUrl) {
		this.mainImgUrl = mainImgUrl;
	}

	@Override
	public String toString() {
		return "UserScoreLogVO{" +
				"logId=" + logId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",userId=" + userId +
				",source=" + source +
				",score=" + score +
				",bizId=" + bizId +
				",ioType=" + ioType +
				",spuName=" + spuName +
				",mainImgUrl=" + mainImgUrl +
				'}';
	}
}
