package com.mall4j.cloud.biz.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 微信小程序模板素材表
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 10:53:56
 */
public class WeixinMaTemplate extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private String id;

    /**
     * 小程序名称
     */
    private String maAppName;

    /**
     * 小程序appid
     */
    private String maAppId;

    /**
     * 小程序路径
     */
    private String maAppPath;

	/**
	 * 小程序配置官网地址
	 */
    private String maUrl;

    private String thumbMediaId;

    /**
     * 卡片标题
     */
    private String cardTitle;

    /**
     * 卡片封面
     */
    private String cardImg;

    /**
     * 展示方式： 1文字 2图片 3小程序卡片
     */
    private Integer showType;

    /**
     * 公众号原始id
     */
    private String mpAppId;

	/**
	 * 小程序头像
	 */
    private String headImage;

	/**
	 * 是否授权：0否 1是
	 */
    private Integer authorizationState;

    /**
     * 创建人名称
     */
    private String createBy;

    /**
     * 修改人名称
     */
    private String updateBy;

    /**
     * 删除标识0-正常,1-已删除
     */
    private Integer delFlag;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMaAppName() {
		return maAppName;
	}

	public void setMaAppName(String maAppName) {
		this.maAppName = maAppName;
	}

	public String getMaAppId() {
		return maAppId;
	}

	public void setMaAppId(String maAppId) {
		this.maAppId = maAppId;
	}

	public String getMaAppPath() {
		return maAppPath;
	}

	public void setMaAppPath(String maAppPath) {
		this.maAppPath = maAppPath;
	}

	public String getCardTitle() {
		return cardTitle;
	}

	public void setCardTitle(String cardTitle) {
		this.cardTitle = cardTitle;
	}

	public String getCardImg() {
		return cardImg;
	}

	public void setCardImg(String cardImg) {
		this.cardImg = cardImg;
	}

	public Integer getShowType() {
		return showType;
	}

	public void setShowType(Integer showType) {
		this.showType = showType;
	}

	public String getMpAppId() {
		return mpAppId;
	}

	public void setMpAppId(String mpAppId) {
		this.mpAppId = mpAppId;
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

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	public String getHeadImage() {
		return headImage;
	}

	public void setHeadImage(String headImage) {
		this.headImage = headImage;
	}

	public Integer getAuthorizationState() {
		return authorizationState;
	}

	public void setAuthorizationState(Integer authorizationState) {
		this.authorizationState = authorizationState;
	}

	public String getMaUrl() {
		return maUrl;
	}

	public void setMaUrl(String maUrl) {
		this.maUrl = maUrl;
	}

	public String getThumbMediaId() {
		return thumbMediaId;
	}

	public void setThumbMediaId(String thumbMediaId) {
		this.thumbMediaId = thumbMediaId;
	}

	@Override
	public String toString() {
		return "WeixinMaTemplate{" +
				"id=" + id +
				",maAppName=" + maAppName +
				",maAppId=" + maAppId +
				",maAppPath=" + maAppPath +
				",cardTitle=" + cardTitle +
				",cardImg=" + cardImg +
				",showType=" + showType +
				",mpAppId=" + mpAppId +
				",createBy=" + createBy +
				",createTime=" + createTime +
				",updateBy=" + updateBy +
				",updateTime=" + updateTime +
				",delFlag=" + delFlag +
				'}';
	}
}
