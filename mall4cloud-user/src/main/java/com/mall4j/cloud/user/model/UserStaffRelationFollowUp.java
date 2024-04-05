package com.mall4j.cloud.user.model;

import java.io.Serializable;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

/**
 * 会员跟进记录
 *
 * @author FrozenWatermelon
 * @date 2023-11-13 17:37:14
 */
@Data
public class UserStaffRelationFollowUp extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 好友关联表id
     */
    private Long relationId;

    /**
     * 修改跟进引用的原跟进记录id
     */
    private Long followUpId;

    /**
     * 跟进话术内容
     */
    private String content;

    /**
     * 引用员工ids
     */
    private String staffIds;

    /**
     * 引用订单编号
     */
    private String orderId;

    /**
     * 引用聊天记录ids
     */
    private String chatIds;

    /**
     * 引用图片
     */
    private String imgsUrl;

    /**
     * 数据来源 0管理端 1导购端
     */
    private Integer origin;

    private String createName;

    private String userId;

    private String unionId;

    private Long createById;

}
