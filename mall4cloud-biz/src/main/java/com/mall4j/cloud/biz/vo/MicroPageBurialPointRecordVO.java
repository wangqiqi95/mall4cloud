package com.mall4j.cloud.biz.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/*
* 微页面埋点数据返回类
* */
@Data
public class MicroPageBurialPointRecordVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 记录ID
     */
    @ApiModelProperty("记录ID")
    private Long recordId;

    /**
     * 微页面装修ID
     */
    @ApiModelProperty("微页面装修ID")
    private Long renovationId;

    /**
     * 浏览时长
     */
    @ApiModelProperty("浏览时长")
    private Integer browseDuration;

    /**
     * 客户昵称
     */
    @ApiModelProperty("客户昵称")
    private String nikeName;

    /**
     * 备注名称
     */
    @ApiModelProperty("备注名称")
    private String notesName;

    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String mobile;

    /**
     * 浏览时间
     */
    @ApiModelProperty("浏览时间")
    private Date browseTime;
}
