package com.mall4j.cloud.user.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 群发任务店铺关联表
 * </p>
 *
 * @author ben
 * @since 2023-02-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_group_push_task_store_relation")
@ApiModel(value="GroupPushTaskStoreRelation对象", description="群发任务店铺关联表")
public class GroupPushTaskStoreRelation extends Model<GroupPushTaskStoreRelation> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "task_shop_relation_id", type = IdType.AUTO)
    private Long taskShopRelationId;

    @ApiModelProperty(value = "群发任务ID")
    private Long groupPushTaskId;

    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


    @Override
    protected Serializable pkVal() {
        return this.taskShopRelationId;
    }

}
