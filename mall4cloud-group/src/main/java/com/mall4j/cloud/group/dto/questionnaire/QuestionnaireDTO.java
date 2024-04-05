package com.mall4j.cloud.group.dto.questionnaire;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 问卷信息表DTO
 *
 * @author FrozenWatermelon
 * @date 2023-05-08 14:10:59
 */
@Data
public class QuestionnaireDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("问卷id")
    private Long id;

    @ApiModelProperty("状态 0-未启用，1-已启用")
    private Integer status;

    @ApiModelProperty("问卷名称")
    private String name;

    @ApiModelProperty("活动开始时间")
    private Date activityBeginTime;

    @ApiModelProperty("活动截止时间")
    private Date activityEndTime;

    @ApiModelProperty("是否全部门店")
    private Integer isAllShop;

    @ApiModelProperty("备注")
    private String remarks;

    @ApiModelProperty("不在白名单提示")
    private String unInWhite;

    @ApiModelProperty("会员名单类型 0名单 1会员标签")
    private Integer userWhiteType;

    @ApiModelProperty("会员标签")
    private String userTag;

    @ApiModelProperty("会员标签名称`,`隔开（赘余字段）")
    private String userTagName;

    @ApiModelProperty("未注册会员提示")
    private String unRegTip;

    @ApiModelProperty("奖品发放提示")
    private String giftGrantTip;

    @ApiModelProperty("活动开始提示")
    private String beginTip;

    @ApiModelProperty("提交问卷提示")
    private String submitTip;

    @ApiModelProperty("活动海报图片地址")
    private String posterUrl;

    @ApiModelProperty("活动海报按钮图片地址")
    private String posterButtonUrl;

    @ApiModelProperty("背景图片地址")
    private String backgroundUrl;

    @ApiModelProperty("活动描述")
    private String describe;

	@ApiModelProperty("会员名单")
	private List<Long> userIds;

	@ApiModelProperty("适用门店")
	private List<Long> storeIds;

	@ApiModelProperty("问卷表单内容")
	private String content;
    @ApiModelProperty("表单名称集合")
    private List<QuestionnaireFormNames> formNames;


	@ApiModelProperty("状态 礼品类型 -1无奖品 0积分 1优惠券 2抽奖 3实物 ")
    @NotNull(message = "奖品类型不能为空")
	private Integer giftType;

	@ApiModelProperty(value = "奖品 积分直接维护赠送的积分值 优惠券维护赠送的优惠券id 抽奖维护抽奖活动id", required = true)
	private String giftId;

	@ApiModelProperty("奖品名称")
	private String giftName;

	@ApiModelProperty("奖品图片")
	private String giftPic;

    @ApiModelProperty("用户名单列表RedisKey")
    private String redisKey;

    @ApiModelProperty("抽奖游戏类型")
    private Integer gameType;

    /**
     * 是否发送订阅消息
     */
    @ApiModelProperty("是否发送订阅消息")
    private Integer isSendSubscribe;

    /**
     * 表单标题
     */
    @ApiModelProperty("表单标题")
    private String title;

	@ApiModelProperty(hidden = true)
	private Long createId;
	@ApiModelProperty(hidden = true)
	private String createName;
	@ApiModelProperty(hidden = true)
	private Long updateId;
	@ApiModelProperty(hidden = true)
	private String updateName;

}
