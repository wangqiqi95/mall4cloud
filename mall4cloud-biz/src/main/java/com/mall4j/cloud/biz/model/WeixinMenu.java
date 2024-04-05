package com.mall4j.cloud.biz.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 微信菜单表
 *
 * @author FrozenWatermelon
 * @date 2022-01-26 23:14:17
 */
public class WeixinMenu extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 序号
     */
    private String id;

    /**
     * 父id
     */
    private String fatherId;

    /**
     * 菜单KEY
     */
    private String menuKey;

    /**
     * 菜单类型
     */
    private String menuType;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 网页链接
     */
    private String url;

    /**
     * 相应消息类型
     */
    private String msgtype;

    /**
     * 菜单位置
     */
    private Integer orders;

    /**
     * 关联素材id
     */
    private String templateId;

    /**
     * 公众号原始id
     */
    private String appId;

    /**
     * 小程序appid
     */
    private String miniprogramAppid;

    /**
     * 小程序页面路径
     */
    private String miniprogramPagepath;

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

	public String getFatherId() {
		return fatherId;
	}

	public void setFatherId(String fatherId) {
		this.fatherId = fatherId;
	}

	public String getMenuKey() {
		return menuKey;
	}

	public void setMenuKey(String menuKey) {
		this.menuKey = menuKey;
	}

	public String getMenuType() {
		return menuType;
	}

	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMsgtype() {
		return msgtype;
	}

	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}

	public Integer getOrders() {
		return orders;
	}

	public void setOrders(Integer orders) {
		this.orders = orders;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getMiniprogramAppid() {
		return miniprogramAppid;
	}

	public void setMiniprogramAppid(String miniprogramAppid) {
		this.miniprogramAppid = miniprogramAppid;
	}

	public String getMiniprogramPagepath() {
		return miniprogramPagepath;
	}

	public void setMiniprogramPagepath(String miniprogramPagepath) {
		this.miniprogramPagepath = miniprogramPagepath;
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

	@Override
	public String toString() {
		return "WeixinMenu{" +
				"id=" + id +
				",fatherId=" + fatherId +
				",menuKey=" + menuKey +
				",menuType=" + menuType +
				",menuName=" + menuName +
				",url=" + url +
				",msgtype=" + msgtype +
				",orders=" + orders +
				",templateId=" + templateId +
				",appId=" + appId +
				",miniprogramAppid=" + miniprogramAppid +
				",miniprogramPagepath=" + miniprogramPagepath +
				",createBy=" + createBy +
				",createTime=" + createTime +
				",updateBy=" + updateBy +
				",updateTime=" + updateTime +
				",delFlag=" + delFlag +
				'}';
	}
}
