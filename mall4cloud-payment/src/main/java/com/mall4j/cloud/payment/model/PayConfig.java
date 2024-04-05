package com.mall4j.cloud.payment.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 支付配置
 */
@Data
@TableName("pay_config")
public class PayConfig implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId(type = IdType.AUTO)
	private Long id;
	/**
	 * 选中金额限制(0：未选中/1：选中)
	 */
	private Integer selectedAmountLimit;
	/**
	 * 金额限制类型（0：不限/1：满额）
	 */
	private Integer amountLimitType;
	/**
	 * 限制金额
	 */
	private BigDecimal amountLimitNum;
	/**
	 * 选中会员限制(0：未选中/1：选中)
	 */
	private Integer selectedMemberLimit;
	/**
	 * 会员限制类型（0：不限/1：指定会员）
	 */
	private Integer memberLimitType;
	/**
	 * 创建人
	 */
	private String createName;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 修改人
	 */
	private String updateName;
	/**
	 * 修改时间
	 */
	private Date updateTime;

}
