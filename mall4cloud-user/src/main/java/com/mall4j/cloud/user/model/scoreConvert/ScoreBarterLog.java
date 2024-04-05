package com.mall4j.cloud.user.model.scoreConvert;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;
import java.io.Serializable;

/**
 * 积分换物兑换记录表
 *
 * @author shijing
 * @date 2021-12-10 17:18:04
 */

@Data
@TableName("score_barter_log")
public class ScoreBarterLog extends BaseModel implements Serializable {

    /**
     * 主键ID
     */
    @TableId(type= IdType.AUTO)
    private Long logId;

    /**
     * 兑换的用户ID
     */
    private Long userId;

    /**
     * 关联活动ID
     */
    private Long convertId;

    /**
     * 用户卡号
     */
    private String userCardNumber;

    /**
     * 用户手机号
     */
    private String userPhone;

    /**
     * 兑换积分数
     */
    private Long convertScore;

    /**
     * 兑换地址
     */
    private String convertAddress;

    /**
     * 物流编码
     */
    private String courierCode;

    /**
     * 自提门店ID
     */
    private Long shopId;


    /**
     * 自提门店名称
     */
    private String shopName;

    /**
     * 备注
     */
    private String note;

    /**
     * 创建人ID
     */
    private Long createId;

    /**
     * 修改人ID
     */
    private Long updateId;
}
