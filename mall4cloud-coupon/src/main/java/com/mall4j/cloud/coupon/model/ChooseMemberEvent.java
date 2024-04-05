package com.mall4j.cloud.coupon.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 指定会员活动表（提供最具价值会员活动表）
 * </p>
 *
 * @author ben
 * @since 2022-11-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_choose_member_event")
@ApiModel(value="ChooseMemberEvent对象", description="指定会员活动表（提供最具价值会员活动表）")
public class ChooseMemberEvent extends Model<ChooseMemberEvent> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键（活动ID）")
    @TableId(value = "choose_member_event_id", type = IdType.AUTO)
    private Long chooseMemberEventId;

    @ApiModelProperty(value = "活动title")
    private String eventTitle;

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime eventStartTime;

    @ApiModelProperty(value = "结束时间")
    private LocalDateTime eventEndTime;

    @ApiModelProperty(value = "false部分门店，true全部门店")
    private Boolean isAllShop;

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

    @ApiModelProperty(value = "0未删除，1删除")
    private Integer delFlag;

    @ApiModelProperty(value = "创建人ID（platform的sys_user的主键））")
    private Long createUser;

    @ApiModelProperty(value = "创建人用户名")
    private String createUserName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "编辑人ID（platform的sys_user的主键）")
    private Long updateUser;

    @ApiModelProperty(value = "编辑人用户名")
    private String updateUserName;

    @ApiModelProperty(value = "编辑时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "活动介绍")
    private String presentation;


    @Override
    protected Serializable pkVal() {
        return this.chooseMemberEventId;
    }

}
