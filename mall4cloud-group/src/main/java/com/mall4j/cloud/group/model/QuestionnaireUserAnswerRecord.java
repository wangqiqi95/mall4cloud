package com.mall4j.cloud.group.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

/**
 * 问卷 会员答题记录
 *
 * @author FrozenWatermelon
 * @date 2023-05-08 14:10:59
 */
@Data
public class QuestionnaireUserAnswerRecord extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private Long id;

    /**
     * 
     */
    private Long activityId;

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 关联crm会员id
     */
    private String vipcode;

    /**
     * 手机号 (冗余字段)
     */
    private String phone;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * union_id
     */
    private String unionId;

    /**
     * 是否提交 0否1是
     */
    private Integer submitted;

    /**
     * 提交时间
     */
    private Date submittedTime;

    /**
     * 是否领奖 0否1是
     */
    private Integer awarded;

    /**
     * 领奖时间
     */
    private Date awardedTime;

    /**
     * 是否发货 0否1是
     */
    private Integer shipped;

    /**
     * 发货时间
     */
    private Date shippedTime;

    /**
     * 浏览次数
     */
    private Integer browseCount;

    /**
     * 是否填写收货地址
     */
    private Integer isSetAddr;

    /**
     * 门店ID
     */
    private Long storeId;

    /**
     * 门店CODE
     */
    private String storeCode;

    /**
     * 门店名称
     */
    private String storeName;

}
