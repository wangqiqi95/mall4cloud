package com.mall4j.cloud.distribution.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 分销推广-朋友圈商品VO
 *
 * @author ZengFanChang
 * @date 2021-12-22 22:05:57
 */
public class DistributionMomentsProductVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("朋友圈ID")
    private Long momentsId;

    @ApiModelProperty("商品ID")
    private Long productId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getMomentsId() {
		return momentsId;
	}

	public void setMomentsId(Long momentsId) {
		this.momentsId = momentsId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	@Override
	public String toString() {
		return "DistributionMomentsProductVO{" +
				"id=" + id +
				",momentsId=" + momentsId +
				",productId=" + productId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				'}';
	}
}
