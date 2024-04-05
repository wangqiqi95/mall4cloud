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
 * 指定会员活动关系表
 * </p>
 *
 * @author ben
 * @since 2022-11-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_choose_member_event_mobile_relation")
@ApiModel(value="ChooseMemberEventUserRelation对象", description="指定会员活动关系表")
public class ChooseMemberEventMobileRelation extends Model<ChooseMemberEventMobileRelation> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键（会员活动关联表ID）")
    @TableId(value = "event_user_relation_id", type = IdType.AUTO)
    private Long eventUserRelationId;

    @ApiModelProperty(value = "制定会员活动ID")
    private Long chooseMemberEventId;

    @ApiModelProperty(value = "用户绑定手机号")
    private String mobile;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


    @Override
    protected Serializable pkVal() {
        return this.eventUserRelationId;
    }

}
