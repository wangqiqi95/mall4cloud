package com.mall4j.cloud.group.model;

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
 * 开屏广告用户关联表
 * </p>
 *
 * @author ben
 * @since 2023-04-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_pop_up_ad_user_relation")
@ApiModel(value="PopUpAdUserRelation对象", description="开屏广告用户关联表")
public class PopUpAdUserRelation extends Model<PopUpAdUserRelation> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "pop_up_ad_user_relation_id", type = IdType.AUTO)
    private Long popUpAdUserRelationId;

    @ApiModelProperty(value = "开屏广告主键")
    private Long popUpAdId;

    @ApiModelProperty(value = "会员ID")
    private Long vipUserId;

    @ApiModelProperty(value = "会员卡号")
    private String vipCode;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    private Long createUser;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "门店ID")
    private Long storeId;


    @Override
    protected Serializable pkVal() {
        return this.popUpAdUserRelationId;
    }

}
