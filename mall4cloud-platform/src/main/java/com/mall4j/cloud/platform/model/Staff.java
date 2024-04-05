package com.mall4j.cloud.platform.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 员工信息
 *
 * @author ZengFanChang
 * @date 2021-12-18 18:05:05
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("staff")
public class Staff extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("员工id")
	@TableId(type = IdType.AUTO)
    private Long id;

	@ApiModelProperty("orgId")
    private String orgId;

	@ApiModelProperty("手机号")
    private String mobile;

	@ApiModelProperty("员工编码")
    private String staffCode;

	@ApiModelProperty("员工工号")
	private String staffNo;

	@ApiModelProperty("员工名称")
    private String staffName;

	@ApiModelProperty("状态: 0 正常 1注销")
    private Integer status;

	@ApiModelProperty("员工所属门店ID")
    private Long storeId;

	@ApiModelProperty("员工邮箱")
    private String email;

	@ApiModelProperty("员工职位")
    private String position;

	@ApiModelProperty("企微用户id")
	@TableField("qiwei_user_id")
	private String qiWeiUserId;

	@ApiModelProperty("企微用户状态 1-已激活 2-已禁用 4-未激活 5-退出企业")
	@TableField("qiwei_user_status")
	private Integer qiWeiUserStatus;

	@ApiModelProperty("角色类型 1-导购 2-店长 3-店务")
	private Integer roleType;

	@ApiModelProperty("头像")
	private String avatar;

	@ApiModelProperty("企微离职时间")
	private Date dimissionTime;

	@ApiModelProperty("微信号")
	private String weChatNo;

	@ApiModelProperty("是否开启会话存档: 0否/1是")
	private Integer cpMsgAudit;

	private String openUserId;

	private String gender;

	@ApiModelProperty("员工个人二维码")
	private String qrCode;

	private String createBy;

	private String updateBy;

	/**
	 * 系统账户id，对应sys_user表id
	 */
	private String sysUserId;

	private Integer isDelete;

	@TableField(exist = false)
	@ApiModelProperty("是否开通导购：0否/1是")
	private Integer openPlatform;
}
