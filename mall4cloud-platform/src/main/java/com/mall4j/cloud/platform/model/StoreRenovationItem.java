package com.mall4j.cloud.platform.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author gmq
 * @date 2022-06-09 14:31:51
 */
@Data
@TableName("store_renovation_item")
public class StoreRenovationItem implements Serializable{
    private static final long serialVersionUID = 1L;

	/**
	 *  主键ID
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 *	店铺装修信息表ID
	 */
	private Long renovationId;

	/**
	 *	用户uniId
	 */
	private String uniId;

	/**
	 *	会员ID/会员编号
	 */
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
