package com.mall4j.cloud.product.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 商品分组表
 *
 * @author lhd
 * @date 2021-02-20 14:28:10
 */
public class SpuTag extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 分组标签id
     */
    private Long id;

    /**
     * 分组标题
     */
    private String title;

    /**
     * 店铺Id
     */
    private Long shopId;

    /**
     * 状态(1为正常,-1 为删除)
     */
    private Integer status;

    /**
     * 默认类型(0:商家自定义,1:系统默认)
     */
    private Integer isDefault;

    /**
     * 商品数量
     */
    private Long prodCount;

    /**
     * 列表样式(0:一列一个,1:一列两个,2:一列三个)
     */
    private Integer style;

    /**
     * 排序
     */
    private Integer seq;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 删除时间
     */
    private Date deleteTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}

	public Long getProdCount() {
		return prodCount;
	}

	public void setProdCount(Long prodCount) {
		this.prodCount = prodCount;
	}

	public Integer getStyle() {
		return style;
	}

	public void setStyle(Integer style) {
		this.style = style;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public Date getDeleteTime() {
		return deleteTime;
	}

	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
	}

	@Override
	public String toString() {
		return "SpuTagVO{" +
				"id=" + id +
				",title=" + title +
				",shopId=" + shopId +
				",status=" + status +
				",isDefault=" + isDefault +
				",prodCount=" + prodCount +
				",style=" + style +
				",seq=" + seq +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",deleteTime=" + deleteTime +
				'}';
	}
}
