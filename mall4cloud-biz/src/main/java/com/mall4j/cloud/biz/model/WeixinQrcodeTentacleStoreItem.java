package com.mall4j.cloud.biz.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 *
 * @author gmq
 * @date 2022-06-09 14:31:51
 */
@Data
@TableName("weixin_qrcode_tentacle_store_item")
public class WeixinQrcodeTentacleStoreItem implements Serializable{
	private static final long serialVersionUID = 1L;

	/**
	 *  主键ID
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 *	查看触点记录表ID
	 */
	private String tentacleStoreId;

	/**
	 *	用户uniId
	 */
	@TableField(value = "uniid")
	private String uniId;

	/**
	 *	会员ID/会员编号
	 */
	@TableField(value = "vipcode")
	private String vipCode;

	/**
	 *	会员名称/用户昵称
	 */
	private String nickName;

	/**
	 *	查看时间
	 */
	private Date checkTime;

}
