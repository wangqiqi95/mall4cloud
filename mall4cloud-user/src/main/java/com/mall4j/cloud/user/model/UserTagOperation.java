package com.mall4j.cloud.user.model;

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
 * 
 * </p>
 *
 * @author ben
 * @since 2023-02-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_user_tag_operation")
@ApiModel(value="UserTagOperation对象", description="")
public class UserTagOperation extends Model<UserTagOperation> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "user_tag_operation_id", type = IdType.AUTO)
    private Long userTagOperationId;

    @ApiModelProperty(value = "操作类型，1新增，2删除")
    private Integer operationState;

    @ApiModelProperty(value = "操作人")
    private Long doUser;

    @ApiModelProperty(value = "被操作人")
    private String beVipCode;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "标签ID")
    private Long tagId;

    @ApiModelProperty(value = "标签组ID")
    private Long groupId;


    @Override
    protected Serializable pkVal() {
        return this.userTagOperationId;
    }

}
