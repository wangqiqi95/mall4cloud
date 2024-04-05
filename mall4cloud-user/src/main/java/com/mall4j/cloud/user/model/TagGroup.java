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
 * 标签组表
 * </p>
 *
 * @author ben
 * @since 2023-02-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_tag_group")
@ApiModel(value="TagGroup对象", description="标签组表")
public class TagGroup extends Model<TagGroup> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "标签组ID")
    @TableId(value = "tag_group_id", type = IdType.AUTO)
    private Long tagGroupId;

    @ApiModelProperty(value = "标签组名称")
    private String groupName;

    @ApiModelProperty(value = "组标识，1导购助手打标，2导购助手显示，3管理后台打标")
    private String authFlag;

    @ApiModelProperty(value = "0未启用，1启用")
    private Integer enableState;

    @ApiModelProperty(value = "0多个，1单个")
    private Integer singleState;

    @ApiModelProperty(value = "1手动标签，2导购标签")
    private Integer groupType;

    @ApiModelProperty(value = "父级组ID")
    private Long parentId;

    @ApiModelProperty(value = "创建人ID")
    private Long createUser;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;


    @Override
    protected Serializable pkVal() {
        return this.tagGroupId;
    }

}
