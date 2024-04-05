package com.mall4j.cloud.user.model.scoreConvert;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * @description 积分活动券过期提醒
 * @author shijing
 * @date 2022-02-08
 */
@Data
public class UserScoreCouponOverdueNotice implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * id
     */
    private Integer id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 积分活动id
     */
    private Long activityId;

    /**
     * 提醒开关
     */
    private int noticeSwitch;

    /**
     * 用户通知formid
     */
    private String formId;

}
