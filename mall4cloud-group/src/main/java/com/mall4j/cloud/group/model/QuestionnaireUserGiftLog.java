package com.mall4j.cloud.group.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 问卷调查用户领奖记录表
 * @TableName questionnaire_user_gift_log
 */
@TableName(value ="questionnaire_user_gift_log")
@Data
public class QuestionnaireUserGiftLog implements Serializable {
    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 奖品ID
     */
    @TableField(value = "questionnaire_gift_id")
    private Long questionnaireGiftId;

    /**
     * 奖品信息
     */
    @TableField(value = "questionnaire_gift_value")
    private String questionnaireGiftValue;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}