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
 * 标签表
 * </p>
 *
 * @author ben
 * @since 2023-02-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_tag")
@ApiModel(value="Tag对象", description="标签表")
public class Tag extends Model<Tag> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "标签ID")
    @TableId(value = "tag_id", type = IdType.AUTO)
    private Long tagId;

    @ApiModelProperty(value = "标签名")
    private String tagName;

    @ApiModelProperty(value = "创建人ID")
    private Long createUser;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "启用状态，0未启用，1启用")
    private Integer enableState;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "导入时间")
    private LocalDateTime importTime;

    @ApiModelProperty(value = "导入状态：0导入中，1导入完成，2导入失败")
    private Integer importStatus;


    @Override
    protected Serializable pkVal() {
        return this.tagId;
    }

}
