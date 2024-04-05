package com.mall4j.cloud.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author ZengFanChang
 * @Date 2022/02/13
 */
@Data
public class StaffTagVo {

//    @ApiModelProperty("导购标签ID")
//    private Long staffTagId;

    @ApiModelProperty("用户标签ID")
    private Long userTagId;

    @ApiModelProperty("用户标签名称")
    private String tagName;

    @ApiModelProperty("用户标签类型 2系统 3导购自定义")
    private Integer tagType;

    @ApiModelProperty("用户数量")
    private Integer userNum;

    @ApiModelProperty("是否已添加")
    private Boolean alreadyAdd;

}
