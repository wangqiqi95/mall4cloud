package com.mall4j.cloud.coupon.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ChooseMemberEventVO {

    @ApiModelProperty(value = "主键（活动ID）")
    private Long chooseMemberEventId;

    @ApiModelProperty(value = "活动title")
    private String eventTitle;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "开始时间")
    private LocalDateTime eventStartTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "结束时间")
    private LocalDateTime eventEndTime;

//    @ApiModelProperty(value = "false部分门店，true全部门店")
//    private Boolean isAllShop;

    @ApiModelProperty(value = "0兑礼到店，1快递")
    private Integer exchangeType;

    @ApiModelProperty(value = "库存数量，可设置为0，0为不限制库存数量")
    private Integer stockNum;

    @ApiModelProperty(value = "剩余库存数量")
    private Integer lastStockNum;

    @ApiModelProperty(value = "限制数量，可设置为0，0为不限制")
    private Integer restrictNum;

    @ApiModelProperty(value = "封面图url")
    private String coverImageUrl;

    @ApiModelProperty(value = "礼品url")
    private String giftImageUrl;

    @ApiModelProperty(value = "非指定会员提示信息")
    private String nonChooseMemberMessage;

    @ApiModelProperty(value = "非平台注册会员提示信息")
    private String nonMemberMessage;

    @ApiModelProperty(value = "0未开启，1开启")
    private Integer eventEnabledStatus;

//    @ApiModelProperty(value = "0未删除，1删除")
//    private Integer delFlag;

    @ApiModelProperty(value = "创建人ID（platform的sys_user的主键））")
    private Long createUser;

    @ApiModelProperty(value = "创建人用户名")
    private String createUserName;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "编辑人ID（platform的sys_user的主键）")
    private Long updateUser;

    @ApiModelProperty(value = "编辑人用户名")
    private String updateUserName;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "编辑时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "活动绑定优惠券列表")
    private List<MemberEventCouponVO> couponList;

    @ApiModelProperty(value = "活动指定会员个数")
    private Integer memberCount;

    @ApiModelProperty(value = "活动介绍")
    private String presentation;

//    @ApiModelProperty(value = "门店个数")
//    private Long shopNum;

}
