package com.mall4j.cloud.api.user.vo;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mall4j.cloud.common.serializer.ImgJsonSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 全部会员VO
 *
 * @author cl
 * @data 2020-04-09 16:16:53
 */
@Data
public class UserManagerVO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("用户姓名")
    private String name;

    @ApiModelProperty("用户邮箱")
    private String email;

    @ApiModelProperty("手机号码")
    private String phone;

    @ApiModelProperty("修改时间")
    private Date updateTime;

    @ApiModelProperty("注册时间")
    private Date createTime;

    @ApiModelProperty("M(男) or F(女)")
    private String sex;

    @ApiModelProperty("出生日期。例如：2009-11-27")
    private String birthDate;

    @ApiModelProperty("头像图片路径")
    @JsonSerialize(using = ImgJsonSerializer.class)
    private String pic;

    @ApiModelProperty("状态 1 正常 0 无效")
    private Integer status;

    @ApiModelProperty("积分")
    private Long score;

    @ApiModelProperty("会员成长值")
    private Long growth;

    @ApiModelProperty("会员等级")
    private Integer level;

    @ApiModelProperty("等级条件 0 普通会员 1 付费会员")
    private Integer levelType;

    @ApiModelProperty("vip结束时间")
    private Date vipEndTime;

    @ApiModelProperty("会员等级名称")
    private String levelName;

    @ApiModelProperty("bizUserId")
    private String bizUserId;

    @ApiModelProperty("付费会员等级")
    private Integer vipLevel;

    @ApiModelProperty("付费会员等级名称")
    private String vipLevelName;

    // -----------------------------------------------------以上是user 的信息

    @ApiModelProperty("最近消费时间")
    private Date reConsTime;

    @ApiModelProperty("消费金额")
    private BigDecimal consAmount;

    @ApiModelProperty("实付金额")
    private BigDecimal actualAmount;

    @ApiModelProperty("消费次数")
    private Integer consTimes;

    @ApiModelProperty("平均折扣")
    private BigDecimal averDiscount;

    @ApiModelProperty("充值金额")
    private BigDecimal rechargeAmount;

    @ApiModelProperty("充值次数")
    private Integer rechargeTimes;

    @ApiModelProperty("售后金额")
    private BigDecimal afterSaleAmount;

    @ApiModelProperty("售后次数")
    private Integer afterSaleTimes;

    @ApiModelProperty("当前积分")
    private Long currentScore;

    @ApiModelProperty("累积积分")
    private Long sumScore;

    @ApiModelProperty("当前余额")
    private BigDecimal currentBalance;

    @ApiModelProperty("累计余额")
    private BigDecimal sumBalance;

    @ApiModelProperty("分销等级")
    private Integer distributionLevel;

    @ApiModelProperty("成为分销员的时间")
    private Date distributorTime;

    // ----以下用户详情----------------------------------------------------------------

    @ApiModelProperty("标签")
    private List<UserTagApiVO> userTagList;


    // -----以下是商城后台会员详情-----------------------------------
    @ApiModelProperty("会员卡号")
    private String vipcode;

    @ApiModelProperty("union_id")
    private String unionId;

    @ApiModelProperty("客户姓名")
    private String customerName;

    @ApiModelProperty(value = "是否有宝宝,M男宝宝，F女宝宝 ")
    private String haveBaby;

    @ApiModelProperty(value = "宝宝生日,yyyy-MM-dd")
    private String babyBirthday;

    @ApiModelProperty(value = "二宝性别,M男宝宝，F女宝宝")
    private String secondBabySex;

    @ApiModelProperty(value = "二宝生日,yyyy-MM-dd")
    private String secondBabyBirth;

    @ApiModelProperty(value = "三宝性别,M男宝宝，F女宝宝")
    private String thirdBabySex;

    @ApiModelProperty(value = "三宝生日,yyyy-MM-dd")
    private String thirdBabyBirth;

    @ApiModelProperty(value = "兴趣")
    private String interests;

    @ApiModelProperty(value = "爱好")
    private String hobby;

    @ApiModelProperty(value = "注册门店id")
    private Long storeId;

    @ApiModelProperty(value = "注册门店")
    private String storeName;

    @ApiModelProperty(value = "注册门店code")
    private String storeCode;

    @ApiModelProperty(value = "所属导购ID")
    private Long staffId;

    @ApiModelProperty(value = "服务门店id")
    private Long staffStoreId;

    @ApiModelProperty(value = "服务门店")
    private String staffStoreName;

    @ApiModelProperty(value = "服务门店Code")
    private String staffStoreCode;

    @ApiModelProperty(value = "所属导购名称")
    private String staffName;
    @ApiModelProperty(value = "所属导购工号")
    private String staffNameNo;

    @ApiModelProperty("注册来源:0-小程序 1-friendly walk")
    private Integer regSource;
}
