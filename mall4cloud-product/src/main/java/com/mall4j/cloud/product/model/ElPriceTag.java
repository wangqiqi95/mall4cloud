package com.mall4j.cloud.product.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 电子价签管理
 *
 * @author FrozenWatermelon
 * @date 2022-03-27 22:23:15
 */
public class ElPriceTag extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
	@TableId
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 商品数量
     */
    private Integer prodCount;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 0正常 1删除
     */
    private Integer isDeleted;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getProdCount() {
		return prodCount;
	}

	public void setProdCount(Integer prodCount) {
		this.prodCount = prodCount;
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

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	@Override
	public String toString() {
		return "ElPriceTag{" +
				"id=" + id +
				",name=" + name +
				",prodCount=" + prodCount +
				",createBy=" + createBy +
				",createTime=" + createTime +
				",updateBy=" + updateBy +
				",updateTime=" + updateTime +
				",isDeleted=" + isDeleted +
				'}';
	}
}
