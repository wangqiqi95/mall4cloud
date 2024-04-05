package com.mall4j.cloud.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author FrozenWatermelon
 * @date 2021/2/25
 */
@Data
public class UserInfoVO {

    /**
     * 用户积分
     */
    @ApiModelProperty(value = "用户积分")
    private Long score;
    /**
     * 年度即将过期积分值
     */
    @ApiModelProperty(value = "年度即将过期积分值")
    private Long pointValue;
    /**
     * 用户余额
     */
    @ApiModelProperty(value = "用户余额")
    private Long actualBalance;

    /**
     * 累计余额
     */
    @ApiModelProperty(value = "累计余额")
    private Long balance;
    /**
     * 优惠券数量
     */
    @ApiModelProperty(value = "优惠券数量")
    private Integer couponNum;
    /**
     * 消息
     */
    @ApiModelProperty(value = "消息数量")
    private Integer notifyNum;

    @ApiModelProperty("商品收藏数量")
    private Integer collections;

    @ApiModelProperty("总累计消费")
    private Long totalConsume;

    @ApiModelProperty("当月累计消费")
    private Long monthConsume;

    @ApiModelProperty("当年累计消费")
    private Long yearConsume;

    @ApiModelProperty("所属导购ID")
    private Long staffId;

    @ApiModelProperty("所属导购名称")
    private String staffName;

    @ApiModelProperty("所属导购门店ID")
    private Long staffStoreId;

    @ApiModelProperty("所属导购门店名称")
    private String staffStoreName;

    @ApiModelProperty("所属导购工号")
    private String staffNo;

    @ApiModelProperty("所属导购状态 0:正常 1:离职")
    private Integer staffStatus;

    @ApiModelProperty("会员升级提示语")
    private String upgradeHint;

    @ApiModelProperty(value = "crm会员 当前等级，1---新奇，2---好奇，3---炫奇，4---珍奇")
    private String current_grade_id;

    @ApiModelProperty(value = "会员名称")
    private String levelName;

    @ApiModelProperty(value = "crm会员 距离下一个升级还需要消费多少金额")
    private BigDecimal next_pay;

}
