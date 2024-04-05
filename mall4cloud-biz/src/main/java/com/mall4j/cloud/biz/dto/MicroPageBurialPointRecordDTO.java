package com.mall4j.cloud.biz.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 微页面埋点数据参数类
 */
@Data
public class MicroPageBurialPointRecordDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /*
    * 微信Code
    * */
    @ApiModelProperty("微信Code")
    private String code;

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
     * 短链Key
     */
	@ApiModelProperty("短链Key")
    private String shortKey;

    /**
     * 浏览时长
     */
	@ApiModelProperty("浏览时长")
    private Integer browseDuration;
}
