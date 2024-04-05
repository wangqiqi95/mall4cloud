package com.mall4j.cloud.group.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

/**
 * 问卷奖品清单 实物奖品维护
 *
 * @author FrozenWatermelon
 * @date 2023-05-08 14:10:59
 */
@Data
public class QuestionnaireGift implements Serializable{
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
     * 礼品类型 0积分 1优惠券 2抽奖 3实物
     */
    private Integer giftType;

    /**
     * 奖品 积分直接维护赠送的积分值 优惠券维护赠送的优惠券id 抽奖维护抽奖活动id 多个`,`分隔
     */
    private String giftId;

    /**
     * 奖品名称
     */
    private String giftName;

    /**
     * 奖品图片
     */
    private String giftPic;

    /**
     * 库存
     */
    private Integer stock;

    /**
     * 抽奖游戏类型
     */
    private Integer gameType;

}
