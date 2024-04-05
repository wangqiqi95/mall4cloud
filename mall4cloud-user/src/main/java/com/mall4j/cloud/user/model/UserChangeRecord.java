package com.mall4j.cloud.user.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户变更记录
 *
 * @author chaoge
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserChangeRecord extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value="id",type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "0-服务门店变更 1-导购变更")
    private Integer type;

    @ApiModelProperty(value = "0-关店迁移 1-导购变更 2-手动变更导购")
    private Integer source;

    @ApiModelProperty(value = "变更前id")
    private Long beforeId;

    @ApiModelProperty(value = "变更前name")
    private String beforeName;

    @ApiModelProperty(value = "变更后id")
    private Long afterId;

    @ApiModelProperty(value = "变更后name")
    private String afterName;

    @ApiModelProperty(value = "创建人")
    private String creator;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    /**
     * 逻辑删除（1不可用，0可用）
     */
    private Integer disable;
}
