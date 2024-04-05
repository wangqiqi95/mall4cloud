package com.mall4j.cloud.payment.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 支付配置会员关联
 */
@Data
@TableName("pay_config_member")
public class PayConfigMember implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId(type = IdType.AUTO)
	private Long configMemberId;
	/**
	 * 会员手机号
	 */
	private String phone;
	/**
	 * 创建时间
	 */
	private Date createTime;

}
