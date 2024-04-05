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
 * 制定会员活动优惠券关联表
 * </p>
 *
 * @author ben
 * @since 2022-11-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_choose_member_event_coupon_relation")
@ApiModel(value="ChooseMemberEventCouponRelation对象", description="制定会员活动优惠券关联表")
public class ChooseMemberEventCouponRelation extends Model<ChooseMemberEventCouponRelation> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键（制定会员活动优惠券关联表ID）")
    @TableId(value = "event_coupon_relation_id", type = IdType.AUTO)
    private Long eventCouponRelationId;

    @ApiModelProperty(value = "活动ID（t_choose_member_event表ID）")
    private Long eventId;

    @ApiModelProperty(value = "优惠券ID")
    private Long couponId;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


    @Override
    protected Serializable pkVal() {
        return this.eventCouponRelationId;
    }

}
