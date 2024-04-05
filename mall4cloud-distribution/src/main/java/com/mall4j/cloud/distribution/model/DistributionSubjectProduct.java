package com.mall4j.cloud.distribution.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 分销推广-专题门店
 *
 * @author ZengFanChang
 * @date 2021-12-22 22:05:57
 */
public class DistributionSubjectProduct extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 专题ID
     */
    private Long subjectId;

    /**
     * 商品ID
     */
    private Long productId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Long subjectId) {
		this.subjectId = subjectId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	@Override
	public String toString() {
		return "DistributionSubjectProduct{" +
				"id=" + id +
				",subjectId=" + subjectId +
				",productId=" + productId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				'}';
	}
}
