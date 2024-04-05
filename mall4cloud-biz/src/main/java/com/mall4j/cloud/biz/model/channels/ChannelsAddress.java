package com.mall4j.cloud.biz.model.channels;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 视频号4.0地址管理关联表
 * 
 */
@Data
@TableName("channels_address")
public class ChannelsAddress implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId(type = IdType.AUTO)
	private Long id;
	/**
	 * 微信侧地址id
	 */
	private String addressId;
	/**
	 * 收货人名称
	 */
	private String receiverName;
	/**
	 * 收货人手机号
	 */
	private String telNumber;
	/**
	 * 国家
	 */
	private String country;
	/**
	 * 省
	 */
	private String province;
	/**
	 * 市
	 */
	private String city;
	/**
	 * 区
	 */
	private String town;
	/**
	 * 详细收货地址
	 */
	private String detailedAddress;
	/**
	 * 邮编
	 */
	private String postCode;
	/**
	 * 是否默认收货地址 0 否 1是
	 */
	private Integer defaultRecv;
	/**
	 * 创建人
	 */
	private String createBy;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新人
	 */
	private String updateBy;
	/**
	 * 更新时间
	 */
	private Date updateTime;
	/**
	 * 是否删除 0 否 1是
	 */
	private Integer isDeleted;

}
