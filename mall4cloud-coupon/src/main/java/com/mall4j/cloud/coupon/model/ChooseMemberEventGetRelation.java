package com.mall4j.cloud.coupon.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户领取优惠券关联表t_choose_member_event和t_coupon_user关联表
 * </p>
 *
 * @author ben
 * @since 2022-11-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_choose_member_event_get_relation")
@ApiModel(value="ChooseMemberEventGetRelation对象", description="用户领取优惠券关联表t_choose_member_event和t_coupon_user关联表")
public class ChooseMemberEventGetRelation extends Model<ChooseMemberEventGetRelation> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户领取优惠券活动关联ID")
    @TableId(value = "event_coupon_user_relation_id", type = IdType.AUTO)
    private Long eventCouponUserRelationId;

    @ApiModelProperty(value = "t_coupon_user表ID")
    private Long couponUserId;

    @ApiModelProperty(value = "t_choose_member_event_id")
    private Long eventId;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


    @Override
    protected Serializable pkVal() {
        return this.eventCouponUserRelationId;
    }

}
