package com.mall4j.cloud.group.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


@Data
public class PromotionColumnPageVO {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("活动名称")
    private String activityName;

    @ApiModelProperty("活动开始时间")
    private Date activityBeginTime;

    @ApiModelProperty("活动结束时间")
    private Date activityEndTime;

    @ApiModelProperty("是否全部门店 0 否 1是")
    private Integer isAllShop;

    @ApiModelProperty("状态 0 未启用 1已启用")
    private Integer status;

    @ApiModelProperty("是否删除")
    private Integer deleted;

    @ApiModelProperty("促销位图片")
    private String imgs;

    @ApiModelProperty("截止时间")
    private Date deadline;

    @ApiModelProperty("创建人名称")
    private String createUserName;

    @ApiModelProperty("更新人名称")
    private String updateUserName;

    @ApiModelProperty("创建时间")
    protected Date createTime;

    @ApiModelProperty("更新时间")
    protected Date updateTime;
}
