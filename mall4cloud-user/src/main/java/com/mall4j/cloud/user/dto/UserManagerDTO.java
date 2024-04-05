package com.mall4j.cloud.user.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 全部会员，请求参数
 *
 * @author cl
 * @data 2020-04-09 16:16:50
 */
@Data
public class UserManagerDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("真实姓名")
    private String realName;

    @ApiModelProperty("手机号码")
    private String phone;

    @ApiModelProperty("性别 null 不限，M男，F女")
    private String sex;

    @ApiModelProperty("状态 1 正常 0 无效")
    private Integer status;

    @ApiModelProperty("客户注册时间-条件开始时间")
    private Date userRegStartTime;
    @ApiModelProperty("客户注册时间-条件结束时间")
    private Date userRegEndTime;

    @ApiModelProperty("客户渠道 null 不限，公众号，小程序，H5，自有App")
    private Integer appType;

    @ApiModelProperty("客户身份 0普通会员，1付费会员")
    private Integer levelType;

    // ------交易信息----------------------------------

    @ApiModelProperty("最近消费时间-开始时间")
    private Date reConsStartTime;
    @ApiModelProperty("最近消费时间-结算时间")
    private Date reconsEndTime;


    @ApiModelProperty("消费金额")
    private Double preConsAmount;
    @ApiModelProperty("消费金额")
    private Double apConsAmount;

    @ApiModelProperty("消费次数")
    private Integer preConsTimes;
    @ApiModelProperty("消费次数")
    private Integer apConsTimes;

    @ApiModelProperty("订单均价")
    private Double preOrderAverAmount;
    @ApiModelProperty("订单均价")
    private Double apOrderAverAmount;

    @ApiModelProperty("平均折扣")
    private Double preAverDiscount;
    @ApiModelProperty("平均折扣")
    private Double apAverDiscount;

    @ApiModelProperty("当前余额")
    private Double preCurrentBalance;

    @ApiModelProperty("用户ids")
    private List<Long> userIds;

    @ApiModelProperty("当前余额")
    private Double apCurrentBalance;


    @ApiModelProperty("当前积分")
    private Integer preCurrentScore;
    @ApiModelProperty("当前积分")
    private Integer apCurrentScore;


    @ApiModelProperty("充值金额")
    private Double preRechargeAmount;
    @ApiModelProperty("充值金额")
    private Double apRechargeAmount;

    @ApiModelProperty("充值次数")
    private Integer preRechargeTimes;
    @ApiModelProperty("充值次数")
    private Integer apRechargeTimes;

    @ApiModelProperty("客户标签")
    private List<Long> tagIds;

    @ApiModelProperty("排序字段的名称")
    private String fieldName;

    @ApiModelProperty("排序规则:一次只能安照一个字段排序; 升序：ascending 降序：descending 不排序: null ")
    private String sortingType;

    @ApiModelProperty("注册时间筛选-注册开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createStartTime;

    @ApiModelProperty("注册时间筛选-注册结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createEndTime;
    /************add by hwy 20220304 商城后台查询****************/
    @ApiModelProperty("服务门店列表")
    private List<Long> serverStoreIdList;

    @ApiModelProperty("注册门店列表")
    private List<Long> registerStoreIdList;

    @ApiModelProperty("所属导购列表")
    private List<Long> staffList ;

    @ApiModelProperty("是否有服务导购 0 没有  1 有  ")
    private Integer hasServiceStaff ;


    /**
     * 当前页
     */
    @NotNull(message = "pageNum 不能为空")
    @ApiModelProperty(value = "当前页", required = true)
    private Integer pageNum;

    @NotNull(message = "pageSize 不能为空")
    @ApiModelProperty(value = "每页大小", required = true)
    private Integer pageSize;

}
