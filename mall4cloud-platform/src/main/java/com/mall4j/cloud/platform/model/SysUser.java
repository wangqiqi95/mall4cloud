package com.mall4j.cloud.platform.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 平台用户
 *
 * @author lhd
 * @date 2020-12-21 14:16:34
 */
@Data
public class SysUser extends BaseModel implements Serializable{

    /**
     * 平台用户id
     */
	@TableId(type = IdType.AUTO)
    private Long sysUserId;

	/**
	 * 昵称
	 */
	private String nickName;

	/**
	 * 账号
	 */
	private String username;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 是否开通导购：0否/1是
	 */
	private Integer openPlatform;

	/**
	 * 状态：0-禁用/1-启用/2-离职
	 */
	private Integer status;

	/**
	 * 头像
	 */
	private String avatar;

	/**
	 * 员工编号
	 */
	private String code;

	/**
	 * 联系方式
	 */
	private String phoneNum;

	/**
	 * 是否已经设置账号
	 */
	private Integer hasAccount;

	/**
	 * 组织节点id
	 */
	private String orgId;

	/**
	 *
	 */
	private String createBy;

	/**
	 *
	 */
	private String updateBy;

	/**
	 * 是否删除：0正常/1删除
	 */
	private Integer isDelete;

	@TableField(exist = false)
	private String password;
//



}
