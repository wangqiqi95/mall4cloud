package com.mall4j.cloud.user.model.scoreConvert;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 积分换券
 *
 * @author shijing
 */

@Data
@TableName("score_coupon_log")
public class ScoreCouponLog implements Serializable {
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
     * 积分换券活动类型（0：积分兑礼/1：积分换券）
     */
    private Short type;

    /**
     * 备注
     */
    private String note;

    /**
     * 创建人
     */
    private Long createUserId;

    /**
     * 创建时间
     */
    private Date createTime;


    /**
     * 原始兑换积分数
     */
    private Long originalConvertScore;
    /**
     * 折扣兑换积分数
     */
    private Long discountConvertScore;
    /**
     * 积分活动id
     */
    private Long scoreActivityId;
    /**
     * 积分活动来源
     */
    private Integer scoreActivitySrc;
    @ApiModelProperty("中奖状态，0：待领取，1：待发放，2：已发放")
    private Integer status;
    @ApiModelProperty("用户收货地址")
    private String userAddr;
    @ApiModelProperty("用户手机号码")
    private String phone;
    @ApiModelProperty("发货物流单号")
    private String logisticsNo;
    @ApiModelProperty("发货物流公司")
    private String company;
    @ApiModelProperty("收件人姓名")
    private String userName;
    @ApiModelProperty("兑礼门店")
    private String storeName;
    @ApiModelProperty("导入状态，0：待确认，1：已确认")
    private Integer exportStatus;
}
