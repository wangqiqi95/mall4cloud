package com.mall4j.cloud.user.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 *
 * @author FrozenWatermelon
 * @date 2022-02-15 19:39:12
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserStaffCpRelation extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 会员Id
     */
    private Long userId;

	/**
	 * 会员unionid
	 */
	private String userUnionId;

    /**
     * 会员企微id
     */
    private String qiWeiUserId;

    /**
     * 员工id
     */
    private Long staffId;

    /**
     * 员工企微id
     */
    private String qiWeiStaffId;

    /**
     * 绑定关系 1-绑定 2- 解绑中
     */
    private Integer status;

	/**
	 * 企业微信昵称
	 */
	private String qiWeiNickName;

    /**
     * 渠道标识id
     */
    private String codeChannelId;

    /**
     * 客户与员工执行事件(企微事件类型)
     */
    private String contactChange;

    private Integer contactChangeType;

    /**
     * 外部联系人所在企业的简称
     */
    private String corpName;

    /**
     * 外部联系人所在企业的主体名称
     */
    private String corpFullName;

    /**
     * 成员对客户备注的企业名称
     */
    private String cpRemarkCorpName;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 外部联系人的类型: 1表示该外部联系人是微信用户，2表示该外部联系人是企业微信用户
     */
    private Integer type;

    /**
     * 0自动通过/1手动通过
     */
    private Integer autoType;

    /**
     * 该成员对此外部联系人的备注
     */
    private String cpRemark;

    /**
     * 该成员对此外部联系人的描述
     */
    private String cpDescription;

    /**
     * 该成员添加此外部联系人的时间
     */
    private Date cpCreateTime;

    /**
     * 该成员对此客户备注的手机号码
     */
    private String cpRemarkMobiles;

    /**
     * https://developer.work.weixin.qq.com/document/path/92114#%E6%9D%A5%E6%BA%90%E5%AE%9A%E4%B9%89
     * 该成员添加此客户的来源，具体含义详见来源定义
     */
    private String cpAddWay;

    private String cpOperUserId;

    /**
     * 企微渠道标识
     */
    private String cpState;

    /**
     * 阶段id
     */
    private Long stageId;

    /**
     * 渠道源分组id
     */
    private Long codeGroupId;

    /**
     * 外部联系人性别 0-未知 1-男性 2-女性
     */
    private Integer gender;

    private String updateBy;

}
