package com.mall4j.cloud.group.vo.questionnaire;

import com.mall4j.cloud.group.dto.questionnaire.QuestionnaireFormNames;
import com.mall4j.cloud.group.dto.questionnaire.QuestionnaireUserAnswer;
import com.mall4j.cloud.group.model.QuestionnaireGift;
import com.mall4j.cloud.group.model.QuestionnaireUserAnswerRecord;
import com.mall4j.cloud.group.model.QuestionnaireUserGiftAddr;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class QuestionnaireDetailVO {

    @ApiModelProperty("问卷id")
    private String id;
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

    @ApiModelProperty("")
    private Long createId;
    @ApiModelProperty("创建人")
    private String createName;
    @ApiModelProperty("修改人")
    private Long updateId;
    @ApiModelProperty("创建时间")
    private String createTime;
    @ApiModelProperty("")
    private String updateName;


    @ApiModelProperty("问卷表单内容")
    private String content;
    @ApiModelProperty("表单名称集合")
    private List<QuestionnaireFormNames> formNames;

    @ApiModelProperty("问卷用户答题内容")
    private List<QuestionnaireUserAnswer> answerRecordContent;

    @ApiModelProperty("适用门店")
    private List<Long> storeIds;
    @ApiModelProperty("适用用户")
    private List<Long> userIds;

    @ApiModelProperty("查看人数")
    private Integer browseCount;
    @ApiModelProperty("提交人数")
    private Integer submitCount;

    /**
     * 未提交人数
     */
    @ApiModelProperty("未提交人数")
    private Integer unSubmitCount;

    /**
     * 总人数
     */
    @ApiModelProperty("总人数")
    private Integer allUserCount;

    @ApiModelProperty("用户答卷记录")
    private QuestionnaireUserAnswerRecord answerRecord;
    @ApiModelProperty("实物奖品的收货地址")
    private QuestionnaireUserGiftAddr addr;


    @ApiModelProperty("奖品信息")
    private QuestionnaireGift questionnaireGift;

    /**
     * 提交问卷提示
     */
    @ApiModelProperty("提交问卷提示")
    private String submitTip;

    /**
     * 活动海报图片地址
     */
    @ApiModelProperty("活动海报图片地址")
    private String posterUrl;

    /**
     * 活动海报按钮图片地址
     */
    @ApiModelProperty("活动海报按钮图片地址")
    private String posterButtonUrl;

    /**
     * 背景图片地址
     */
    @ApiModelProperty("背景图片地址")
    private String backgroundUrl;

    /**
     * 活动描述
     */
    @ApiModelProperty("活动描述")
    private String describe;

    /**
     * 设置的门店数量
     */
    @ApiModelProperty("设置的门店数量")
    private Long applyShopNum;
    /**
     * 门店Id `,`号分隔
     */
    @ApiModelProperty("门店Id`,`分隔")
    private String shopIds;

    /**
     * 活动当前状态 活动状态0未启用，2未开始，3已开始，4，已结束
     */
    @ApiModelProperty("活动状态0未启用，2未开始，3已开始，4，已结束")
    private Integer activityStatus;

    /**
     * 是否首次启用过
     */
    @ApiModelProperty("是否首次启用过")
    private Integer isFirstEnabled;

    /**
     * 是否提交过答题
     */
    @ApiModelProperty("是否提交过答题")
    private Integer isSubmitted;

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
}
