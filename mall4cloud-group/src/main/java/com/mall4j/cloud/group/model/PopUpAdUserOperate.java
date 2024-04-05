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
@TableName("t_pop_up_ad_user_operate")
@ApiModel(value="PopUpAdUserOperate对象", description="")
public class PopUpAdUserOperate extends Model<PopUpAdUserOperate> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "pop_up_ad_user_operate_id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long popUpAdUserOperateId;

    @ApiModelProperty(value = "开屏广告主键")
    private Long popUpAdId;

    @ApiModelProperty(value = "用户ID")
    private Long vipUserId;

    @ApiModelProperty(value = "微信union_id")
    private String unionId;

    @ApiModelProperty(value = "1浏览，2点击")
    private Integer operate;

    @ApiModelProperty(value = "门店ID")
    private Long storeId;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "操作入口页面")
    private String entrance;


    @Override
    protected Serializable pkVal() {
        return this.popUpAdUserOperateId;
    }

}
