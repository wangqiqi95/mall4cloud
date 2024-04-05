package com.mall4j.cloud.api.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 用户表VO
 *
 * @author YXF
 * @date 2020-12-08 11:18:04
 */
@Data
public class UserApiVO {

    @ApiModelProperty("ID")
    private Long userId;

    @ApiModelProperty("用户昵称")
    private String nickName;

	@ApiModelProperty("用户姓名")
	private String name;

	@ApiModelProperty("姓名")
	private String customerName;

    @ApiModelProperty("1(男) or 0(女)")
    private String sex;

    @ApiModelProperty("例如：2009-11-27")
    private String birthDate;

    @ApiModelProperty("头像图片路径")
    private String pic;

    @ApiModelProperty("状态 1 正常 0 无效")
    private Integer status;

    @ApiModelProperty("普通会员等级（冗余字段）")
    private Integer level;

    @ApiModelProperty("普通会员等级id")
    private Long userLevelId;

    @ApiModelProperty("普通会员等级名称")
    private String levelName;

	@ApiModelProperty("付费会员等级")
	private Integer vipLevel;

	@ApiModelProperty("付费会员等级id")
	private Long vipUserLevelId;

	@ApiModelProperty("付费会员等级名称")
	private String vipLevelName;

    @ApiModelProperty("vip结束时间")
    private Date vipEndTime;

    @ApiModelProperty("等级条件 0 普通会员 1 付费会员")
    private Integer levelType;

    @ApiModelProperty("用户手机号")
    private String userMobile;

    @ApiModelProperty("用户手机号")
    private String phone;

	/**
	 * openId list
	 */
	private List<String> bizUserIdList;

	@ApiModelProperty("注册门店ID")
	private Long storeId;

	@ApiModelProperty("服务门店ID")
	private Long serviceStoreId;

	@ApiModelProperty("注册门店名称")
	private String storeName;

	@ApiModelProperty("注册门店编码")
	private String storeCode;

	@ApiModelProperty("所属导购ID")
	private Long staffId;

	@ApiModelProperty("所属导购名称")
	private String staffName;

	@ApiModelProperty("所属导购门店ID")
	private Long staffStoreId;

	@ApiModelProperty("所属导购门店名称")
	private String staffStoreName;

	@ApiModelProperty("所属导购门店编码")
	private String staffStoreCode;

	@ApiModelProperty("所属导购工号")
	private String staffNo;

	@ApiModelProperty("所属导购状态 0:正常 1:离职")
	private Integer staffStatus;

	@ApiModelProperty("是否添加微信 0-否 1-是")
	private Integer addWechat;

	@ApiModelProperty("x天回访过")
	private Integer visitDay;

	@ApiModelProperty("x天消费过")
	private Integer consumeDay;

	@ApiModelProperty("创建时间")
	protected Date createTime;

	@ApiModelProperty("更新时间")
	protected Date updateTime;

	@ApiModelProperty("微客审核状态 -1-初始化 0-待审核 1-已同意 2-已拒绝")
	private Integer veekerAuditStatus;

	@ApiModelProperty("微客状态 -1-初始化 0-禁用 1-启用 2-拉黑")
	private Integer veekerStatus;

	@ApiModelProperty(value = "宝宝性别,M男宝宝，F女宝宝")
	private String haveBaby;
	@ApiModelProperty(value = "宝宝生日,yyyy-MM-dd")
	private String babyBirthday;
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
	@ApiModelProperty(value = "关联crm会员id")
	private String vipcode;
    @ApiModelProperty(value = "小程序openId")
    private String openId;
	@ApiModelProperty(value = "unionId")
	private String unionId;

    @ApiModelProperty("身份 0会员 1威客 2导购")
    private Integer identity;
    @ApiModelProperty(value = "当前用户导购Id")
    private Long currentStaffId;

	@ApiModelProperty(value = "会员unionid")
	private String userUnionId;

	@ApiModelProperty(value = "会员企微id")
	private String qiWeiUserId;

	/**
	 * 群发任务需要返回参数
	 */
	@ApiModelProperty(value = "导购企业微信ID")
	private String staffCpUserId;
	@ApiModelProperty("0-进行中 1-已发送 2-因客户不是好友导致发送失败 3-因客户已经收到其他群发消息导致发送失败")
	private Integer reachStatus;

	@ApiModelProperty(value = "rec开关")
	private Boolean recSwitch;

}
