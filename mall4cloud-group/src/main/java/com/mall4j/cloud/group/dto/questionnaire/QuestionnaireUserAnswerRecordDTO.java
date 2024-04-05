package com.mall4j.cloud.group.dto.questionnaire;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 问卷 会员答题记录DTO
 *
 * @author FrozenWatermelon
 * @date 2023-05-08 14:10:59
 */
@Data
public class QuestionnaireUserAnswerRecordDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Integer id;

    @ApiModelProperty("")
    private Integer activityId;

    @ApiModelProperty("用户编号")
    private Long userId;

    @ApiModelProperty("关联crm会员id")
    private String vipcode;

    @ApiModelProperty("手机号 (冗余字段)")
    private String phone;

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("union_id")
    private String unionId;

    @ApiModelProperty("是否提交 0否1是")
    private Integer submitted;

    @ApiModelProperty("提交时间")
    private Integer submittedTime;

    @ApiModelProperty("是否领奖 0否1是")
    private Integer awarded;

    @ApiModelProperty("领奖时间")
    private Integer awardedTime;

    @ApiModelProperty("是否发货 0否1是")
    private Integer shipped;

    @ApiModelProperty("发货时间")
    private Integer shippedTime;

}
