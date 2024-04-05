package com.mall4j.cloud.group.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 问卷信息表
 *
 * @author FrozenWatermelon
 * @date 2023-05-08 14:10:59
 */
@Data
public class Questionnaire extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 问卷id
     */
    private Long id;

    /**
     * 状态 0-未启用，1-已启用
     */
    private Integer status;

    /**
     * 问卷名称
     */
    private String name;

    /**
     * 活动开始时间
     */
    private Date activityBeginTime;

    /**
     * 活动截止时间
     */
    private Date activityEndTime;

    /**
     * 是否全部门店
     */
    private Integer isAllShop;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 不在白名单提示
     */
    private String unInWhite;

	/**
	 * 会员名单类型 0名单 1会员标签
	 */
	private Integer userWhiteType;

	/**
	 * 会员标签
	 */
	private String userTag;

    /**
     * 会员标签名称`,`隔开（赘余字段）
     */
    private String userTagName;

    /**
     * 未注册会员提示
     */
    private String unRegTip;

    /**
     * 奖品发放提示
     */
    private String giftGrantTip;

    /**
     * 活动开始提示
     */
    private String beginTip;

    /**
     * 提交问卷提示
     */
    private String submitTip;

    /**
     * 活动海报图片地址
     */
    private String posterUrl;

    /**
     * 活动海报按钮图片地址
     */
    private String posterButtonUrl;

    /**
     * 背景图片地址
     */
    private String backgroundUrl;

    /**
     * 活动描述
     */
    private String describe;

    /**
     * 是否首次启用过
     */
    private Integer isFirstEnabled;

    /**
     * 
     */
    private Long createId;

    /**
     * 
     */
    private String createName;

    /**
     * 
     */
    private Long updateId;

    /**
     * 
     */
    private String updateName;

    /**
     * 是否发送订阅消息
     */
    private Integer isSendSubscribe;

    /**
     * 表单标题
     */
    private String title;
}
