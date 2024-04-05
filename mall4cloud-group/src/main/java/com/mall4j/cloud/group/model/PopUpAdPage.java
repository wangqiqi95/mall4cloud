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
 * 开屏广告触发页面记录标
 * </p>
 *
 * @author ben
 * @since 2023-04-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_pop_up_ad_page")
@ApiModel(value="PopUpAdPage对象", description="开屏广告触发页面记录标")
public class PopUpAdPage extends Model<PopUpAdPage> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "pop_up_ad_page_id", type = IdType.AUTO)
    private Long popUpAdPageId;

    @ApiModelProperty(value = "开屏广告ID")
    private Long popUpAdId;

    @ApiModelProperty(value = "页面路径")
    private String pageUrl;

    @ApiModelProperty(value = "创建时间	")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建人")
    private Long createUser;

    @ApiModelProperty(value = "修改人")
    private Long updateUser;


    @Override
    protected Serializable pkVal() {
        return this.popUpAdPageId;
    }

}
