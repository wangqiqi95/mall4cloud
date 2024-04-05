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
 * 
 * </p>
 *
 * @author ben
 * @since 2023-04-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_pop_up_ad_attachment")
@ApiModel(value="PopUpAdAttachment对象", description="")
public class PopUpAdAttachment extends Model<PopUpAdAttachment> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "pop_up_ad_media_id", type = IdType.AUTO)
    private Long popUpAdMediaId;

    @ApiModelProperty(value = "开屏广告主键")
    private Long popUpAdId;

    @ApiModelProperty(value = "素材链接")
    private String mediaUrl;

    @ApiModelProperty(value = "页面链接")
    private String link;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建人")
    private Long createUser;

    @ApiModelProperty(value = "修改人")
    private Long updateUser;

    @ApiModelProperty(value = "业务ID，与广告设置的业务类型关联")
    private Long businessId;

    @ApiModelProperty(value = "页面链接类型，H5，MINI_PROGRAM")
    private String linkType;


    @Override
    protected Serializable pkVal() {
        return this.popUpAdMediaId;
    }

}
