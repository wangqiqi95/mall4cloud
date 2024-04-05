package com.mall4j.cloud.coupon.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 优惠券赠送记录
 *
 * @author FrozenWatermelon
 * @date 2021-04-28 16:25:05
 */
public class CouponGiveLog extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 赠送业务类型 1充值赠送
     */
    private Integer bizType;

    /**
     * 业务id(充值记录id)
     */
    private Long bizId;

	public Integer getBizType() {
		return bizType;
	}

	public void setBizType(Integer bizType) {
		this.bizType = bizType;
	}

	public Long getBizId() {
		return bizId;
	}

	public void setBizId(Long bizId) {
		this.bizId = bizId;
	}

	@Override
	public String toString() {
		return "CouponGiveLog{" +
				"bizType=" + bizType +
				",bizId=" + bizId +
				'}';
	}
}
