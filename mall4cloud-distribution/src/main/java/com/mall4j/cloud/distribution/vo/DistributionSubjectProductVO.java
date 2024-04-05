package com.mall4j.cloud.distribution.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 分销推广-专题门店VO
 *
 * @author ZengFanChang
 * @date 2021-12-22 22:05:57
 */
public class DistributionSubjectProductVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("专题ID")
    private Long subjectId;

    @ApiModelProperty("商品ID")
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
		return "DistributionSubjectProductVO{" +
				"id=" + id +
				",subjectId=" + subjectId +
				",productId=" + productId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				'}';
	}
}
