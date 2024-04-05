package com.mall4j.cloud.coupon.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author ben
 * @since 2022-11-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_choose_member_event_shop_relation")
@ApiModel(value="ChooseMemberEventShopRelation对象", description="")
public class ChooseMemberEventShopRelation extends Model<ChooseMemberEventShopRelation> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID（指定会员活动门店关联ID）")
    @TableId(value = "event_shop_relation_id", type = IdType.AUTO)
    private Long eventShopRelationId;

    @ApiModelProperty(value = "活动ID（t_choose_member_event表ID）")
    private Long eventId;

    @ApiModelProperty(value = "店铺ID（plafrom库tz_store表主键）")
    private Long shopId;

    @ApiModelProperty(value = "店铺CODE")
    private String storeCode;


    @Override
    protected Serializable pkVal() {
        return this.eventShopRelationId;
    }

}
