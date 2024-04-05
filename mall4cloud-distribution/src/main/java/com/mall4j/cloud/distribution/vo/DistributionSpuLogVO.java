package com.mall4j.cloud.distribution.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

/**
 * 分销商品浏览记录信息VO
 *
 * @author cl
 * @date 2021-08-09 14:14:07
 */
public class DistributionSpuLogVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long distributionSpuLogId;

    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty("商品id")
    private Long spuId;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("扫码手机号")
    private String mobile;

	public Long getDistributionSpuLogId() {
		return distributionSpuLogId;
	}

	public void setDistributionSpuLogId(Long distributionSpuLogId) {
		this.distributionSpuLogId = distributionSpuLogId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Long getSpuId() {
		return spuId;
	}

	public void setSpuId(Long spuId) {
		this.spuId = spuId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Override
	public String toString() {
		return "DistributionSpuLogVO{" +
				"createTime=" + createTime +
				", updateTime=" + updateTime +
				", distributionSpuLogId=" + distributionSpuLogId +
				", shopId=" + shopId +
				", spuId=" + spuId +
				", userId=" + userId +
				", mobile='" + mobile + '\'' +
				'}';
	}
}
