package com.mall4j.cloud.biz.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 微信素材链接表
 *
 * @author FrozenWatermelon
 * @date 2022-01-26 22:53:05
 */
public class WeixinLinksucai extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private String id;

    /**
     * 链接名称
     */
    private String name;

    /**
     * 外部链接
     */
    private String outerLink;

    /**
     * 功能描述
     */
    private String content;

    /**
     * 内部链接
     */
    private String innerLink;

    /**
     * 转换标志
     */
    private Integer transferSign;

    /**
     * 微信账户id
     */
    private String accountid;

    /**
     * 账号邮编
     */
    private String postCode;

    /**
     * 分享状态
     */
    private String shareStatus;

    /**
     * 是否加密（0：不加密，1：加密）
     */
    private Integer isEncrypt;

	/**
	 * 原始公众号id
	 */
	private String appId;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 修改人
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOuterLink() {
		return outerLink;
	}

	public void setOuterLink(String outerLink) {
		this.outerLink = outerLink;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getInnerLink() {
		return innerLink;
	}

	public void setInnerLink(String innerLink) {
		this.innerLink = innerLink;
	}

	public Integer getTransferSign() {
		return transferSign;
	}

	public void setTransferSign(Integer transferSign) {
		this.transferSign = transferSign;
	}

	public String getAccountid() {
		return accountid;
	}

	public void setAccountid(String accountid) {
		this.accountid = accountid;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getShareStatus() {
		return shareStatus;
	}

	public void setShareStatus(String shareStatus) {
		this.shareStatus = shareStatus;
	}

	public Integer getIsEncrypt() {
		return isEncrypt;
	}

	public void setIsEncrypt(Integer isEncrypt) {
		this.isEncrypt = isEncrypt;
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

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	@Override
	public String toString() {
		return "WeixinLinksucai{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", outerLink='" + outerLink + '\'' +
				", content='" + content + '\'' +
				", innerLink='" + innerLink + '\'' +
				", transferSign=" + transferSign +
				", accountid='" + accountid + '\'' +
				", postCode='" + postCode + '\'' +
				", shareStatus='" + shareStatus + '\'' +
				", isEncrypt=" + isEncrypt +
				", appId='" + appId + '\'' +
				", createBy='" + createBy + '\'' +
				", updateBy='" + updateBy + '\'' +
				", delFlag=" + delFlag +
				'}';
	}
}
