package com.mall4j.cloud.user.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.EqualsAndHashCode;

/**
 * 用户表
 *
 * @author YXF
 * @date 2020-12-08 11:18:04
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class User extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long userId;

    /**
     * 用户昵称
     */
    private String nickName;

	/**
	 * 用户姓名
	 */
	private String name;

    /**
     * M(男) or F(女)
     */
    private String sex;

    /**
     * 例如：2009-11-27
     */
    private String birthDate;

    /**
     * 头像图片路径
     */
    private String pic;

    /**
     * 状态 1 正常 0 无效
     */
    private Integer status;

    /**
     * 会员等级（冗余字段）
     */
    private Integer level;

	/**
	 * 会员等级名称
	 */
	private String levelName;

	/**
	 * 会当前有效积分
	 */
	private Long currentValidPoint;

	/**
	 * 还差多少金额升级
	 */
	private BigDecimal nextPay;

    /**
     * vip结束时间
     */
    private Date vipEndTime;

    /**
     * 等级条件 0 普通会员 1 付费会员
     */
    private Integer levelType;

	/**
	 * 手机号 (冗余字段)
	 */
	private String phone;

	/**
	 * 付费会员等级
	 */
	private Integer vipLevel;

	/**
	 * 所属门店ID
	 */
	private Long storeId;

	/**
	 * 所属服务门店ID
	 */
	private Long serviceStoreId;

	/**
	 * 所属导购ID
	 */
	private Long staffId;

	/**
	 * 所属导购ID
	 */
	private Long cardStaffId;

	/**
	 * 微客状态 -1-初始化 0-禁用 1-启用 2-拉黑
	 */
	private Integer veekerStatus;

	/**
	 * 微客审核状态 -1-初始化 0-待审核 1-已同意 2-已拒绝"
	 */
	private Integer veekerAuditStatus;

	/**
	 * 是否添加微信 0-否 1-是
	 */
	private Integer addWechat;

	/**
	 * 微客申请时间
	 */
	private Date veekerApplyTime;

	private String unionId;

	@ApiModelProperty(value = "推荐人手机号,校验非空and格式11位数字")
	private String refereePhone;

	@ApiModelProperty(value = "姓名", required = true)
	private String customerName;

	@ApiModelProperty(value = "邮箱,校验包含“@”、“.”")
	private String email;

	@ApiModelProperty(value = "职业")
	private String job;

	@ApiModelProperty(value = "是否有宝宝,M男宝宝，F女宝宝 ")
	private String haveBaby;

	@ApiModelProperty(value = "宝宝生日,yyyy-MM-dd")
	private String babyBirthday;

	@ApiModelProperty(value = "兴趣")
	private String interests;

	@ApiModelProperty(value = "爱好")
	private String hobby;

	@ApiModelProperty(value = "二宝性别,M男宝宝，F女宝宝")
	private String secondBabySex;

	@ApiModelProperty(value = "二宝生日,yyyy-MM-dd")
	private String secondBabyBirth;

	@ApiModelProperty(value = "三宝性别,M男宝宝，F女宝宝")
	private String thirdBabySex;

	@ApiModelProperty(value = "三宝生日,yyyy-MM-dd")
	private String thirdBabyBirth;

	@ApiModelProperty(value = "省id")
	private Long provinceId;
	@ApiModelProperty(value = "省")
	private String province;
	@ApiModelProperty(value = "市Id")
	private Long cityId;
	@ApiModelProperty(value = "市")
	private String city;
	@ApiModelProperty(value = "区id")
	private Long areaId;
	@ApiModelProperty(value = "区")
	private String area;
	private String openId;

	private Integer regSource;
	/**
	 * 关联crm会员编号
	 */
	private String vipcode;

	@ApiModelProperty(value = "rec开关")
	private Boolean recSwitch;

	private String createBy;
}
